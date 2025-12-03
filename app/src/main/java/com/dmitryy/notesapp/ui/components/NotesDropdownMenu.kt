package com.dmitryy.notesapp.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.ui.list.ViewMode

/**
 * Reusable dropdown menu for the notes list screen.
 */
@Composable
fun NotesDropdownMenu(
    expanded: Boolean,
    viewMode: ViewMode,
    showTrashBin: Boolean,
    onDismiss: () -> Unit,
    onToggleViewMode: () -> Unit,
    onAbout: () -> Unit,
    onExportJson: () -> Unit,
    onImportJson: () -> Unit,
    onPinned: () -> Unit,
    onUrlNotes: () -> Unit,
    onTrashBin: () -> Unit,
    onSettings: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        // View mode toggle
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(
                        if (viewMode == ViewMode.GRID) {
                            R.string.switch_to_list_view
                        } else {
                            R.string.switch_to_grid_view
                        }
                    )
                )
            },
            modifier = Modifier.testTag("toggle_view_menu_item"),
            onClick = {
                onDismiss()
                onToggleViewMode()
            }
        )
        
        // About
        DropdownMenuItem(
            text = { Text(stringResource(R.string.about)) },
            modifier = Modifier.testTag("about_menu_item"),
            onClick = {
                onDismiss()
                onAbout()
            }
        )
        
        // Export JSON
        DropdownMenuItem(
            text = { Text(stringResource(R.string.export_json)) },
            modifier = Modifier.testTag("export_json_menu_item"),
            onClick = {
                onDismiss()
                onExportJson()
            }
        )
        
        // Import JSON
        DropdownMenuItem(
            text = { Text(stringResource(R.string.import_json)) },
            modifier = Modifier.testTag("import_json_menu_item"),
            onClick = {
                onDismiss()
                onImportJson()
            }
        )
        
        // Pinned Notes
        DropdownMenuItem(
            text = { Text(stringResource(R.string.pinned_notes)) },
            modifier = Modifier.testTag("pinned_notes_menu_item"),
            onClick = {
                onDismiss()
                onPinned()
            }
        )
        
        // Notes with URLs
        DropdownMenuItem(
            text = { Text(stringResource(R.string.notes_with_urls)) },
            modifier = Modifier.testTag("url_notes_menu_item"),
            onClick = {
                onDismiss()
                onUrlNotes()
            }
        )
        
        // Trash Bin (conditional)
        if (showTrashBin) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.trash_bin)) },
                modifier = Modifier.testTag("trash_bin_menu_item"),
                onClick = {
                    onDismiss()
                    onTrashBin()
                }
            )
        }
        
        // Settings
        DropdownMenuItem(
            text = { Text(stringResource(R.string.settings)) },
            modifier = Modifier.testTag("settings_menu_item"),
            onClick = {
                onDismiss()
                onSettings()
            }
        )
    }
}
