package com.dmitryy.notesapp.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
            onClick = {
                onDismiss()
                onToggleViewMode()
            }
        )
        
        // About
        DropdownMenuItem(
            text = { Text(stringResource(R.string.about)) },
            onClick = {
                onDismiss()
                onAbout()
            }
        )
        
        // Export JSON
        DropdownMenuItem(
            text = { Text(stringResource(R.string.export_json)) },
            onClick = {
                onDismiss()
                onExportJson()
            }
        )
        
        // Import JSON
        DropdownMenuItem(
            text = { Text(stringResource(R.string.import_json)) },
            onClick = {
                onDismiss()
                onImportJson()
            }
        )
        
        // Pinned Notes
        DropdownMenuItem(
            text = { Text(stringResource(R.string.pinned_notes)) },
            onClick = {
                onDismiss()
                onPinned()
            }
        )
        
        // Notes with URLs
        DropdownMenuItem(
            text = { Text(stringResource(R.string.notes_with_urls)) },
            onClick = {
                onDismiss()
                onUrlNotes()
            }
        )
        
        // Trash Bin (conditional)
        if (showTrashBin) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.trash_bin)) },
                onClick = {
                    onDismiss()
                    onTrashBin()
                }
            )
        }
        
        // Settings
        DropdownMenuItem(
            text = { Text(stringResource(R.string.settings)) },
            onClick = {
                onDismiss()
                onSettings()
            }
        )
    }
}
