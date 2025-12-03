package com.dmitryy.notesapp.ui

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.dmitryy.notesapp.ui.ComposeTestUtils.clickTag
import com.dmitryy.notesapp.ui.ComposeTestUtils.assertTagIsDisplayed

class SettingsPage(private val composeTestRule: ComposeTestRule) {

    fun clickPasscodeToggle() {
        composeTestRule.clickTag("passcode_checkbox")
    }

    fun clickClearCache() {
        composeTestRule.clickTag("clear_cache_button")
    }

    fun clickNukeDatabase() {
        composeTestRule.clickTag("nuke_database_button")
    }

    fun clickBack() {
        composeTestRule.clickTag("back_button")
    }

    fun assertPasscodeCheckboxVisible() {
        composeTestRule.assertTagIsDisplayed("passcode_checkbox")
    }
}
