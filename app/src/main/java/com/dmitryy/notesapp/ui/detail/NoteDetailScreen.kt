package com.dmitryy.notesapp.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.ui.theme.DarkCard
import com.dmitryy.notesapp.ui.theme.NeonCyan
import com.dmitryy.notesapp.ui.theme.NeonPink
import com.dmitryy.notesapp.ui.theme.TextPrimary
import com.dmitryy.notesapp.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
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
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onSave, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = NeonCyan)
                    }
                },
                actions = {
                    IconButton(onClick = onTogglePin) {
                        Icon(
                            imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                            contentDescription = if (isPinned) "Unpin" else "Pin",
                            tint = if (isPinned) NeonPink else NeonCyan
                        )
                    }
                    
                    // Save button
                    IconButton(onClick = onSave, modifier = Modifier.testTag("save_button")) {
                        Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.save), tint = NeonCyan)
                    }
                    
                    // Export PDF button (disabled if note is empty)
                    val isNoteEmpty = title.isBlank() && content.isBlank()
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
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete), tint = NeonPink)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            BasicTextField(
                value = title,
                onValueChange = onTitleChange,
                textStyle = TextStyle(
                    color = NeonCyan,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                cursorBrush = SolidColor(NeonCyan),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(stringResource(R.string.title_hint), color = TextSecondary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth().testTag("title_field")
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            BasicTextField(
                value = content,
                onValueChange = onContentChange,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(NeonCyan),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text(stringResource(R.string.content_hint), color = TextSecondary, fontSize = 16.sp)
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("content_field")
            )
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(stringResource(R.string.delete_note_title), color = NeonCyan, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(stringResource(R.string.delete_note_message), color = TextPrimary)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text(stringResource(R.string.delete), color = NeonPink)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(stringResource(R.string.cancel), color = TextSecondary)
                }
            },
            containerColor = DarkCard
        )
    }
}
