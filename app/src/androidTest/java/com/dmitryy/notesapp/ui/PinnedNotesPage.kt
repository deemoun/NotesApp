package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.dmitryy.notesapp.ui.ComposeTestUtils.clickTag
import com.dmitryy.notesapp.ui.ComposeTestUtils.assertTagIsDisplayed

class PinnedNotesPage(private val composeTestRule: ComposeTestRule) {

    fun clickBack() {
        composeTestRule.clickTag("back_button")
    }

    fun assertListVisible() {
        composeTestRule.assertTagIsDisplayed("pinned_notes_list")
    }

    fun assertEmptyStateVisible() {
        composeTestRule.assertTagIsDisplayed("no_pinned_notes_text")
    }
}
