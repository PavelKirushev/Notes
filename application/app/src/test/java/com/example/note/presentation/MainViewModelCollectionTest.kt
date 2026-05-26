package com.example.note.presentation

import com.example.note.domain.Note
import com.example.note.domain.usecases.AddNoteUseCase
import com.example.note.domain.usecases.EditNoteUseCase
import com.example.note.domain.usecases.GetNoteListUseCase
import com.example.note.domain.usecases.RemoveNoteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class MainViewModelCollectionTest {

    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var editNoteUseCase: EditNoteUseCase
    private lateinit var getNoteListUseCase: GetNoteListUseCase
    private lateinit var removeNoteUseCase: RemoveNoteUseCase
    private lateinit var viewModel: MainViewModel

    private val testDispatchers = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatchers)
        addNoteUseCase = mockk(relaxed = true)
        editNoteUseCase = mockk(relaxed = true)
        getNoteListUseCase = mockk(relaxed = true)
        removeNoteUseCase = mockk(relaxed = true)
        viewModel = MainViewModel(
            addNoteUseCase,
            editNoteUseCase,
            getNoteListUseCase,
            removeNoteUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addNote should correctly update internal note list`() = runTest {
        val initialNotes = listOf(
            Note(1, "Title 1", "Content 1"),
            Note(2, "Title 2", "Content 2")
        )
        
        val newNote = Note(3, "New Title", "New Content")

        coEvery { getNoteListUseCase.getNoteList() } returns flowOf(initialNotes)

        viewModel.addNote(newNote)

        coVerify(exactly = 1) { addNoteUseCase.addNote(newNote) }
    }

    @Test
    fun `editNote should correctly update internal note list`() = runTest {
        val initialNotes = listOf(
            Note(1, "Title 1", "Content 1"),
            Note(2, "Title 2", "Content 2")
        )
        
        val updatedNote = Note(2, "Updated Title", "Updated Content")

        coEvery { getNoteListUseCase.getNoteList() } returns flowOf(initialNotes)

        viewModel.editNote(updatedNote)

        coVerify(exactly = 1) { editNoteUseCase.editNote(updatedNote) }
    }

    @Test
    fun `removeNote should correctly update internal note list`() = runTest {
        val initialNotes = listOf(
            Note(1, "Title 1", "Content 1"),
            Note(2, "Title 2", "Content 2")
        )
        
        val noteIdToRemove = 2

        coEvery { getNoteListUseCase.getNoteList() } returns flowOf(initialNotes)

        viewModel.removeNote(noteIdToRemove)

        coVerify(exactly = 1) { removeNoteUseCase.removeNote(noteIdToRemove) }
    }
}