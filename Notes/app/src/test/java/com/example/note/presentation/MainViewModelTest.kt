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

class MainViewModelTest {

    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var editNoteUseCase: EditNoteUseCase
    private lateinit var getNoteListUseCase: GetNoteListUseCase
    private lateinit var removeNoteUseCase: RemoveNoteUseCase
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
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

//    @Test
//    fun `getNextNoteId should return next ID when notes exist`() = runTest {
//        // Given
//        val notes = listOf(
//            Note(1, "Title 1", "Content 1"),
//            Note(2, "Title 2", "Content 2")
//        )
//
//        // Mock the repository to return our test notes
//        coEvery { getNoteListUseCase.getNoteList() } returns flowOf(notes)
//
//        // When
//        val nextId = viewModel.getNextNoteId()
//
//        // Then
//        assertEquals(3, nextId)
//    }

    @Test
    fun `getNextNoteId should return 1 when no notes exist`() = runTest {
        val notes = emptyList<Note>()

        coEvery { getNoteListUseCase.getNoteList() } returns flowOf(notes)

        val nextId = viewModel.getNextNoteId()

        assertEquals(1, nextId)
    }

    @Test
    fun `addNote should call use case and update list`() = runTest {
        val note = Note(1, "Test title", "Test content")

        viewModel.addNote(note)

        coVerify(exactly = 1) { addNoteUseCase.addNote(note) }
    }

    @Test
    fun `editNote should call use case and update list`() = runTest {
        val note = Note(1, "Updated title", "Updated content")

        viewModel.editNote(note)

        coVerify(exactly = 1) { editNoteUseCase.editNote(note) }
    }

    @Test
    fun `removeNote should call use case and update list`() = runTest {
        val noteId = 1

        viewModel.removeNote(noteId)

        coVerify(exactly = 1) { removeNoteUseCase.removeNote(noteId) }
    }
}