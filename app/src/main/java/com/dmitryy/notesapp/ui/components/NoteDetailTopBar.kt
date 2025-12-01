package com.dmitryy.notesapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.ui.theme.NeonCyan
import com.dmitryy.notesapp.ui.theme.NeonPink
import com.dmitryy.notesapp.ui.theme.TextSecondary

/**
 * Top app bar for note detail screen with all action buttons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailTopBar(
    isPinned: Boolean,
    showDelete: Boolean,
    isNoteEmpty: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onTogglePin: () -> Unit,
    onExportPdf: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("back_button")
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = NeonCyan
                )
            }
        },
        actions = {
            // Pin/Unpin button
            IconButton(onClick = onTogglePin) {
                Icon(
                    imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    contentDescription = if (isPinned) "Unpin" else "Pin",
                    tint = if (isPinned) NeonPink else NeonCyan
                )
            }

            // Save button
            IconButton(
                onClick = onSave,
                modifier = Modifier.testTag("save_button")
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = stringResource(R.string.save),
                    tint = NeonCyan
                )
            }

            // Export PDF button (disabled if note is empty)
            IconButton(
                onClick = onExportPdf,
                enabled = !isNoteEmpty
            ) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = stringResource(R.string.export_pdf),
                    tint = if (isNoteEmpty) TextSecondary else NeonCyan
                )
            }

            // Delete button (only if note exists)
            if (showDelete) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = NeonPink
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}
