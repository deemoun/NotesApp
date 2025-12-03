package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.dmitryy.notesapp.NotesApplication
import com.dmitryy.notesapp.ui.list.NotesActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<NotesActivity>()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val app = context.applicationContext as NotesApplication
        app.database.clearAllTables()
        
        try {
            context.cacheDir.deleteRecursively()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun testCreateAndVerifyNote() {
        val listPage = NotesListPage(composeTestRule)
        val detailPage = NoteDetailPage(composeTestRule)

        // 1. Create a new note
        listPage.clickAddNote()
        
        val testTitle = "Test Note ${System.currentTimeMillis()}"
        val testContent = "This is a test note content."

        detailPage.enterTitle(testTitle)
        detailPage.enterContent(testContent)
        
        // 2. Save and go back
        detailPage.clickSave()

        // 3. Observe that it's visible in the list view
        listPage.assertNoteVisible(testTitle)

        // 4. Open the note again
        listPage.clickNote(testTitle)

        // Verify content
        detailPage.assertTitle(testTitle)
        detailPage.assertContent(testContent)

        // 5. Pressing the back button
        detailPage.clickBack()
        
        // Verify we are back in list
        listPage.assertNoteVisible(testTitle)
    }
}
