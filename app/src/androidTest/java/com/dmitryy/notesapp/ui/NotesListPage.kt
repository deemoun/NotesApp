package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.dmitryy.notesapp.ui.ComposeTestUtils.clickTag
import com.dmitryy.notesapp.ui.ComposeTestUtils.clickText
import com.dmitryy.notesapp.ui.ComposeTestUtils.assertTextIsDisplayed

class NotesListPage(private val composeTestRule: ComposeTestRule) {

    fun clickAddNote() {
        composeTestRule.clickTag("add_note_fab")
    }

    fun assertNoteVisible(title: String) {
        composeTestRule.assertTextIsDisplayed(title)
    }

    fun clickNote(title: String) {
        composeTestRule.clickText(title)
    }
}
