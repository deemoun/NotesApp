package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class NoteDetailPage(private val composeTestRule: ComposeTestRule) {

    fun enterTitle(title: String) {
        composeTestRule.onNodeWithTag("title_field").performTextInput(title)
    }

    fun enterContent(content: String) {
        composeTestRule.onNodeWithTag("content_field").performTextInput(content)
    }

    fun clickSave() {
        composeTestRule.onNodeWithTag("save_button").performClick()
    }

    fun clickBack() {
        composeTestRule.onNodeWithTag("back_button").performClick()
    }

    fun assertTitle(title: String) {
        composeTestRule.onNodeWithTag("title_field").assertTextEquals(title)
    }

    fun assertContent(content: String) {
        composeTestRule.onNodeWithTag("content_field").assertTextEquals(content)
    }
}
