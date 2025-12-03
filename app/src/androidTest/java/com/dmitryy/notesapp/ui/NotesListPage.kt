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

    fun openMenu() {
        composeTestRule.clickTag("menu_button")
    }

    fun clickToggleViewMode() {
        composeTestRule.clickTag("toggle_view_menu_item")
    }

    fun clickAbout() {
        composeTestRule.clickTag("about_menu_item")
    }

    fun clickExportJson() {
        composeTestRule.clickTag("export_json_menu_item")
    }

    fun clickImportJson() {
        composeTestRule.clickTag("import_json_menu_item")
    }

    fun clickPinnedNotes() {
        composeTestRule.clickTag("pinned_notes_menu_item")
    }

    fun clickUrlNotes() {
        composeTestRule.clickTag("url_notes_menu_item")
    }

    fun clickTrashBin() {
        composeTestRule.clickTag("trash_bin_menu_item")
    }

    fun clickSettings() {
        composeTestRule.clickTag("settings_menu_item")
    }
}
