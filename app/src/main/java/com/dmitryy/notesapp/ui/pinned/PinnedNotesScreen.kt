package com.dmitryy.notesapp.ui.pinned

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.ui.list.NoteItem
import com.dmitryy.notesapp.ui.theme.DarkBg
import com.dmitryy.notesapp.ui.theme.NeonCyan
import com.dmitryy.notesapp.ui.theme.TextSecondary
import com.dmitryy.notesapp.utils.SwipeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinnedNotesScreen(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onUnpinNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.pinned_notes), color = NeonCyan) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = NeonCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBg
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_pinned_notes),
                        color = TextSecondary,
                        fontSize = 18.sp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes, key = { it.id }) { note ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                SwipeUtils.handleSwipeAction(
                                    dismissValue = dismissValue,
                                    note = note,
                                    onDelete = onDeleteNote,
                                    onTogglePin = onUnpinNote
                                )
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                SwipeUtils.SwipeBackground(
                                    direction = dismissState.dismissDirection,
                                    note = note
                                )
                            },
                            content = {
                                NoteItem(note = note, onClick = { onNoteClick(note) }, isGrid = false)
                            },
                            enableDismissFromStartToEnd = true
                        )
                    }
                }
            }
        }
    }
}
