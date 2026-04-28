package com.example.note.presentation.vosk

import android.content.Context
import android.util.Log
import org.json.JSONObject
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.StorageService
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class VoskHelper(private val context: Context) {
    private var model: Model? = null
    private var isModelInitialized = false

    fun isModelReady(): Boolean {
        return isModelInitialized && model != null
    }

    fun getModel(): Model? = model

    @Suppress("CyclomaticComplexMethod")
    fun initializeModel(callback: (Boolean) -> Unit) {
        try {
            StorageService.unpack(context, "model-ru", "model",
                { model ->
                    this.model = model
                    this.isModelInitialized = true
                    Log.d(TAG, "Model unpacked successfully via StorageService")
                    callback(true)
                },
                { exception ->
                    Log.e(TAG, "Failed to unpack model: ${exception.message}")
                    try {
                        Log.d(TAG, "Trying to load model directly from assets://model-ru")
                        val model = Model("assets://model-ru")
                        this.model = model
                        this.isModelInitialized = true
                        Log.d(TAG, "Model loaded directly from assets")
                        callback(true)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to load model directly from assets: ${e.message}")
                        try {
                            val modelPath = "model-ru"
                            val modelFile = File(context.filesDir, "model")
                            if (!modelFile.exists()) {
                                modelFile.mkdirs()
                            }

                            val assetManager = context.assets
                            val assetFiles = assetManager.list(modelPath)
                            Log.d(TAG, "Available files in model-ru: ${assetFiles?.joinToString(", ") ?: "none"}")
                            
                            if (assetFiles != null) {
                                for (assetFile in assetFiles) {
                                    val assetFilePath = "$modelPath/$assetFile"
                                    val destFile = File(modelFile, assetFile)
                                    
                                    if (destFile.exists()) {
                                        destFile.delete()
                                    }
                                    
                                    val inputStream = assetManager.open(assetFilePath)
                                    val outputStream = FileOutputStream(destFile)
                                    
                                    inputStream.use { input ->
                                        outputStream.use { output ->
                                            input.copyTo(output)
                                        }
                                    }
                                    Log.d(TAG, "Copied $assetFilePath to $destFile")
                                }
                            }

                            val amFiles = assetManager.list("$modelPath/am")
                            Log.d(TAG, "Available files in model-ru/am: ${amFiles?.joinToString(", ") ?: "none"}")
                            if (amFiles != null) {
                                val amDestDir = File(modelFile, "am")
                                if (!amDestDir.exists()) {
                                    amDestDir.mkdirs()
                                }
                                for (amFile in amFiles) {
                                    val assetFilePath = "$modelPath/am/$amFile"
                                    val destFile = File(amDestDir, amFile)
                                    
                                    if (destFile.exists()) {
                                        destFile.delete()
                                    }
                                    
                                    val inputStream = assetManager.open(assetFilePath)
                                    val outputStream = FileOutputStream(destFile)
                                    
                                    inputStream.use { input ->
                                        outputStream.use { output ->
                                            input.copyTo(output)
                                        }
                                    }
                                    Log.d(TAG, "Copied $assetFilePath to $destFile")
                                }
                            }

                            val model = Model(modelFile.absolutePath)
                            this.model = model
                            this.isModelInitialized = true
                            Log.d(TAG, "Model loaded from local file")
                            callback(true)
                        } catch (e2: Exception) {
                            Log.e(TAG, "All methods failed to initialize model: ${e2.message}")
                            Log.e(TAG, "Stack trace:", e2)
                            callback(false)
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, "Exception during model initialization: ${e.message}")
            Log.e(TAG, "Stack trace:", e)
            callback(false)
        }
    }

    private fun transcribeAudio(inputStream: InputStream, callback: (String) -> Unit) {
        try {
            @Suppress("UseCheckOrError")
            val model = this.model ?: throw IllegalStateException("Model not initialized")

            val recognizer = Recognizer(model, SAMPLE_RATE)

            val data = inputStream.readBytes()
            
            if (data.isNotEmpty()) {
                Log.d(TAG, "Processing audio data, size: ${data.size} bytes")
                recognizer.acceptWaveForm(data, data.size)
                val result = recognizer.getFinalResult()
                recognizer.close()
                val text = parseResultText(result)
                Log.d(TAG, "Raw result: $result")
                Log.d(TAG, "Parsed text: '$text'")
                callback(text)
            } else {
                recognizer.close()
                Log.d(TAG, "Empty data received")
                callback("")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during transcription: ${e.message}")
            Log.e(TAG, "Full stack trace:", e)
            callback("Ошибка при транскрибации: ${e.message ?: e.toString()}")
        }
    }

    
    private fun parseResultText(jsonResult: String?): String {
        if (jsonResult.isNullOrBlank()) return ""
        val trimmed = jsonResult.trim()
        if (!trimmed.startsWith("{")) return trimmed
        return try {
            JSONObject(trimmed).optString("text", "")
        } catch (e: Exception) {
            Log.w(TAG, "parseResultText: ${e.message}")
            trimmed
        }
    }

    fun transcribeFromFile(filePath: String, callback: (String) -> Unit) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                callback("Файл не найден: $filePath")
                return
            }

            val inputStream = FileInputStream(file)
            Log.d(TAG, "Transcribing file from path: $filePath, size: ${file.length()} bytes")
            transcribeAudio(inputStream) { result ->
                Log.d(TAG, "Final result from file: $result")
                callback(result)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading file: ${e.message}")
            callback("Ошибка чтения файла: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error reading file: ${e.message}")
            Log.e(TAG, "Stack trace:", e)
            callback("Ошибка чтения файла: ${e.message}")
        }
    }

    fun shutdown() {
        model?.close()
    }

    companion object {
        private const val TAG = "VoskHelper"
        private const val SAMPLE_RATE = 16000.0f
    }
}
