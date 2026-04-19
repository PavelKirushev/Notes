package com.example.note.presentation.vosk

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.note.R
import com.example.note.domain.Note
import com.example.note.presentation.MainViewModel
import com.example.note.presentation.noteWindow.SimpleTextField
import com.example.note.presentation.theme.NoteTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.concurrent.thread

class VoskTranscriptionScreen : ComponentActivity() {

    private lateinit var voskHelper: VoskHelper
    private var audioRecord: AudioRecord? = null
    private var recordingThread: Thread? = null
    private var outputFile: String? = null
    @Volatile
    private var isRecording = false
    private val handler = Handler(Looper.getMainLooper())
    private var autoStopRunnable: Runnable? = null

    private var recordingStateRef: MutableState<Boolean>? = null
    private var iconStateRef: MutableState<ImageVector>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        voskHelper = VoskHelper(this)
        voskHelper.initializeModel { success ->
            if (!success) {
                runOnUiThread {
                    Toast.makeText(this, "Ошибка загрузки модели Vosk", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Failed to initialize model")
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Модель загружена успешно", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Model initialized successfully")
                }
            }
        }

        setContent {
            val mainViewModel = koinViewModel<MainViewModel>()
            NoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TranscriptionScreen(mainViewModel = mainViewModel)
                }
            }
        }
    }

    @Composable
    fun TranscriptionScreen(mainViewModel: MainViewModel) {
        val scope = rememberCoroutineScope()
        val recordingState = remember { mutableStateOf(false) }
        var recording by recordingState
        val iconState = remember { mutableStateOf(Icons.Default.Mic) }
        var recordButtonIcon by iconState
        var noteTitle by remember { mutableStateOf("") }
        var transcriptionResult by remember { mutableStateOf("") }

        DisposableEffect(recordingState, iconState) {
            recordingStateRef = recordingState
            iconStateRef = iconState
            onDispose {
                recordingStateRef = null
                iconStateRef = null
            }
        }

        val scheme = MaterialTheme.colorScheme

        Column(Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = scheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                shadowElevation = 0.dp
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .clickable { finish() }
                            .padding(dimensionResource(R.dimen.padding_10)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_white),
                            contentDescription = stringResource(R.string.back_button),
                            Modifier.size(dimensionResource(R.dimen.padding_30)),
                            tint = scheme.primary
                        )
                        Text(
                            text = stringResource(R.string.back_button),
                            style = MaterialTheme.typography.titleMedium,
                            color = scheme.onSurface
                        )
                    }
                    Text(
                        text = stringResource(R.string.screen_transcription_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = scheme.onSurface,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = {
                            val body = transcriptionResult.trim()
                            if (body.isEmpty() || body.startsWith("Ошибка")) {
                                Toast.makeText(
                                    this@VoskTranscriptionScreen,
                                    "Нет текста для сохранения",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@IconButton
                            }
                            scope.launch(Dispatchers.IO) {
                                val id = mainViewModel.getNextNoteId()
                                val title = noteTitle.trim().ifEmpty { "Голосовая заметка" }
                                mainViewModel.addNote(Note(id, title, body))
                                runOnUiThread { finish() }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.done),
                            contentDescription = stringResource(R.string.ready_button),
                            modifier = Modifier.size(30.dp),
                            tint = scheme.primary
                        )
                    }
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        top = dimensionResource(R.dimen.padding_20),
                        start = dimensionResource(R.dimen.padding_30),
                        end = dimensionResource(R.dimen.padding_20),
                        bottom = dimensionResource(R.dimen.padding_30)
                    )
            ) {
                SimpleTextField(
                    text = noteTitle,
                    onValueChange = { noteTitle = it },
                    fontSize = 40.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.padding_20))
                )
                SimpleTextField(
                    text = transcriptionResult,
                    onValueChange = { transcriptionResult = it },
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = scheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                shadowElevation = 0.dp
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (recording) {
                                stopRecording()
                                recordButtonIcon = Icons.Default.Mic
                                recording = false
                            } else {
                                if (startRecording()) {
                                    recordButtonIcon = Icons.Default.Stop
                                    recording = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = recordButtonIcon,
                            contentDescription = if (recording) "Остановить запись" else "Начать запись",
                            modifier = Modifier.size(32.dp),
                            tint = scheme.primary
                        )
                    }
                    FilledTonalButton(
                        onClick = {
                            if (outputFile.isNullOrEmpty()) {
                                Toast.makeText(
                                    this@VoskTranscriptionScreen,
                                    "Сначала сделайте запись",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@FilledTonalButton
                            }

                            val file = File(outputFile!!)
                            if (!file.exists()) {
                                Toast.makeText(
                                    this@VoskTranscriptionScreen,
                                    "Файл не найден",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@FilledTonalButton
                            }

                            if (!voskHelper.isModelReady()) {
                                Toast.makeText(
                                    this@VoskTranscriptionScreen,
                                    "Модель еще не загружена, подождите...",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@FilledTonalButton
                            }

                            thread(name = "vosk-transcribe") {
                                voskHelper.transcribeFromFile(outputFile!!) { result ->
                                    runOnUiThread {
                                        transcriptionResult = result
                                        if (result.startsWith("Ошибка")) {
                                            Toast.makeText(
                                                this@VoskTranscriptionScreen,
                                                result,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            }
                        },
                        enabled = !recording && outputFile != null,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(stringResource(R.string.transcribe_action))
                    }
                    Spacer(Modifier.size(8.dp))
                }
            }
        }
    }

    private fun buildWavPcm16Mono(pcmData: ByteArray, sampleRate: Int): ByteArray {
        val bitsPerSample = 16
        val channels = 1
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val blockAlign = channels * bitsPerSample / 8
        val header = ByteArray(44)
        ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).apply {
            put("RIFF".toByteArray(Charsets.US_ASCII))
            putInt(36 + pcmData.size)
            put("WAVE".toByteArray(Charsets.US_ASCII))
            put("fmt ".toByteArray(Charsets.US_ASCII))
            putInt(16)
            putShort(1)
            putShort(channels.toShort())
            putInt(sampleRate)
            putInt(byteRate)
            putShort(blockAlign.toShort())
            putShort(bitsPerSample.toShort())
            put("data".toByteArray(Charsets.US_ASCII))
            putInt(pcmData.size)
        }
        return header + pcmData
    }

    private fun startRecording(): Boolean {
        if (!checkPermission()) {
            requestPermission()
            return false
        }
        try {
            val fileName = "recording_${System.currentTimeMillis()}.wav"
            outputFile = "${externalCacheDir?.absolutePath}/$fileName"

            val bufferSize = AudioRecord.getMinBufferSize(
                16000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            if (bufferSize == AudioRecord.ERROR_BAD_VALUE || bufferSize == AudioRecord.ERROR) {
                Toast.makeText(this, "Не удалось инициализировать запись", Toast.LENGTH_LONG).show()
                return false
            }

            val record = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                16000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize * 2
            )
            if (record.state != AudioRecord.STATE_INITIALIZED) {
                record.release()
                Toast.makeText(this, "AudioRecord не инициализирован", Toast.LENGTH_LONG).show()
                return false
            }

            audioRecord = record
            isRecording = true
            record.startRecording()

            val path = outputFile!!
            recordingThread = thread(name = "audio-recorder") {
                val buffer = ByteArray(bufferSize)
                val baos = ByteArrayOutputStream()
                while (isRecording) {
                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    when {
                        read > 0 -> baos.write(buffer, 0, read)
                        read < 0 -> break
                    }
                }
                val pcm = baos.toByteArray()
                try {
                    val wav = buildWavPcm16Mono(pcm, 16000)
                    FileOutputStream(path).use { it.write(wav) }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to write WAV", e)
                }
            }

            autoStopRunnable = Runnable {
                if (isRecording) {
                    stopRecording()
                    runOnUiThread {
                        recordingStateRef?.value = false
                        iconStateRef?.value = Icons.Default.Mic
                    }
                }
            }
            handler.postDelayed(autoStopRunnable!!, 30_000)
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при записи: ${e.message}", e)
            Toast.makeText(this, "Ошибка записи: ${e.message ?: e.toString()}", Toast.LENGTH_LONG).show()
            return false
        }
    }

    private fun stopRecording() {
        autoStopRunnable?.let { handler.removeCallbacks(it) }
        autoStopRunnable = null

        if (!isRecording) return
        isRecording = false
        try {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            recordingThread?.join(10_000)
            recordingThread = null
            Toast.makeText(this, "Запись остановлена", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при остановке записи: ${e.message}", e)
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            Companion.PERMISSIONS_REQUEST_RECORD_AUDIO
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        autoStopRunnable?.let { handler.removeCallbacks(it) }
        voskHelper.shutdown()
        stopRecording()
    }

    companion object {
        private const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
        private const val TAG = "VoskTranscription"
    }
}
