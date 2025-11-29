package com.dmitryy.notesapp.ui.trash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.ui.list.NoteItem
import com.dmitryy.notesapp.ui.theme.DarkBg
import com.dmitryy.notesapp.ui.theme.NeonCyan
import com.dmitryy.notesapp.ui.theme.NeonPink
import com.dmitryy.notesapp.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashBinScreen(
    notes: List<Note>,
    onRestoreNote: (Note) -> Unit,
    onDeleteForever: (Note) -> Unit,
    onRestoreAll: () -> Unit,
    onEmptyTrash: () -> Unit,
    onBack: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.trash_bin), color = NeonCyan) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = NeonCyan)
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.menu), tint = NeonCyan)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.restore_all)) },
                            onClick = {
                                showMenu = false
                                onRestoreAll()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.empty_trash)) },
                            onClick = {
                                showMenu = false
                                onEmptyTrash()
                            }
                        )
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
                        text = stringResource(R.string.trash_empty),
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
                            confirmValueChange = {
                                when (it) {
                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        onRestoreNote(note)
                                        true
                                    }
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        onDeleteForever(note)
                                        true
                                    }
                                    else -> false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val color = when (dismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                                    else -> Color.Transparent
                                }
                                val alignment = when (dismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                                    else -> Alignment.CenterStart
                                }
                                val icon = when (dismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Refresh
                                    SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                                    else -> Icons.Default.Delete
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color, RoundedCornerShape(8.dp))
                                        .padding(16.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            },
                            content = {
                                NoteItem(note = note, onClick = {}) // No click action in trash
                            }
                        )
                    }
                }
            }
        }
    }
}
