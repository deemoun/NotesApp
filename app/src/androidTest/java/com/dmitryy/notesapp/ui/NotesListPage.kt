package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class NotesListPage(private val composeTestRule: ComposeTestRule) {

    fun clickAddNote() {
        composeTestRule.onNodeWithTag("add_note_fab").performClick()
    }

    fun assertNoteVisible(title: String) {
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    fun clickNote(title: String) {
        composeTestRule.onNodeWithText(title).performClick()
    }
}
