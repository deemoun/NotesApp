package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.dmitryy.notesapp.ui.ComposeTestUtils.clickTag
import com.dmitryy.notesapp.ui.ComposeTestUtils.enterText
import com.dmitryy.notesapp.ui.ComposeTestUtils.assertTagTextEquals

class NoteDetailPage(private val composeTestRule: ComposeTestRule) {

    fun enterTitle(title: String) {
        composeTestRule.enterText("title_field", title)
    }

    fun enterContent(content: String) {
        composeTestRule.enterText("content_field", content)
    }

    fun clickSave() {
        composeTestRule.clickTag("save_button")
    }

    fun clickBack() {
        composeTestRule.clickTag("back_button")
    }

    fun assertTitle(title: String) {
        composeTestRule.assertTagTextEquals("title_field", title)
    }

    fun assertContent(content: String) {
        composeTestRule.assertTagTextEquals("content_field", content)
    }
}
