package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.dmitryy.notesapp.ui.ComposeTestUtils.clickTag
import com.dmitryy.notesapp.ui.ComposeTestUtils.assertTagIsDisplayed

class UrlNotesPage(private val composeTestRule: ComposeTestRule) {

    fun clickBack() {
        composeTestRule.clickTag("back_button")
    }

    fun assertListVisible() {
        composeTestRule.assertTagIsDisplayed("url_notes_list")
    }

    fun assertEmptyStateVisible() {
        composeTestRule.assertTagIsDisplayed("no_notes_with_urls_text")
    }
}
