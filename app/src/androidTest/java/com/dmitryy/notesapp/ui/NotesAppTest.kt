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
    val listPage = NotesListPage(composeTestRule)
    val detailPage = NoteDetailPage(composeTestRule)
    val settingsPage = SettingsPage(composeTestRule)
    val pinnedNotesPage = PinnedNotesPage(composeTestRule)
    val trashBinPage = TrashBinPage(composeTestRule)

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
    fun openCreateScreenAndLeave(){
        listPage.clickAddNote()
        detailPage.clickBack()
    }

    @Test
    fun checkSettingsScreen(){
        listPage.openMenu()
        listPage.clickSettings()
        settingsPage.assertPasscodeCheckboxVisible()
        settingsPage.clickClearCache()
        settingsPage.clickBack()
    }

    @Test
    fun pressPinnedNotesAndNavigateBack(){
        listPage.openMenu()
        listPage.clickPinnedNotes()
        pinnedNotesPage.assertEmptyStateVisible()
        pinnedNotesPage.clickBack()
    }

    @Test
    fun emptyTrashCheck(){
        listPage.clickAddNote()
        detailPage.enterTitle("Remove Me")
        detailPage.enterContent("To be removed...")
        detailPage.clickSave()
        listPage.openMenu()
        listPage.clickTrashBin()
        trashBinPage.clickMenu()
        trashBinPage.clickEmptyTrash()
        trashBinPage.clickBack()
    }

    @Test
    fun testCreateAndVerifyNote() {
        listPage.clickAddNote()
        
        val testTitle = "Test Note ${System.currentTimeMillis()}"
        val testContent = "This is a test note content."

        detailPage.enterTitle(testTitle)
        detailPage.enterContent(testContent)

        detailPage.clickSave()

        listPage.assertNoteVisible(testTitle)

        listPage.clickNote(testTitle)

        detailPage.assertTitle(testTitle)
        detailPage.assertContent(testContent)

        detailPage.clickBack()

        listPage.assertNoteVisible(testTitle)
    }
}
