package com.dmitryy.notesapp.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.ui.components.DeleteNoteDialog
import com.dmitryy.notesapp.ui.components.NoteContentField
import com.dmitryy.notesapp.ui.components.NoteDetailTopBar
import com.dmitryy.notesapp.ui.components.NoteTitleField

@Composable
fun NoteDetailScreen(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    onExportPdf: () -> Unit,
    showDelete: Boolean,
    isPinned: Boolean = false,
    onTogglePin: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    BackHandler {
        onSave()
    }

    Scaffold(
        topBar = {
            NoteDetailTopBar(
                isPinned = isPinned,
                showDelete = showDelete,
                isNoteEmpty = title.isBlank() && content.isBlank(),
                onBack = onSave, // Save on back navigation
                onSave = onSave,
                onTogglePin = onTogglePin,
                onExportPdf = onExportPdf,
                onDelete = { showDeleteDialog = true }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            NoteTitleField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = stringResource(R.string.title_hint)
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            NoteContentField(
                value = content,
                onValueChange = onContentChange,
                placeholder = stringResource(R.string.content_hint),
                modifier = Modifier.weight(1f)
            )
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        DeleteNoteDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            }
        )
    }
}
