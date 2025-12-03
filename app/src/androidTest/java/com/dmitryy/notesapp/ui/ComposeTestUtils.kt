package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

object ComposeTestUtils {
    fun ComposeTestRule.clickTag(tag: String) {
        onNodeWithTag(tag).performClick()
    }

    fun ComposeTestRule.clickText(text: String) {
        onNodeWithText(text).performClick()
    }

    fun ComposeTestRule.assertTagIsDisplayed(tag: String) {
        onNodeWithTag(tag).assertIsDisplayed()
    }

    fun ComposeTestRule.assertTextIsDisplayed(text: String) {
        onNodeWithText(text).assertIsDisplayed()
    }

    fun ComposeTestRule.enterText(tag: String, text: String) {
        onNodeWithTag(tag).performTextInput(text)
    }

    fun ComposeTestRule.assertTagTextEquals(tag: String, text: String) {
        onNodeWithTag(tag).assertTextEquals(text)
    }
}
