package com.dmitryy.notesapp.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.ui.theme.DarkBg
import com.dmitryy.notesapp.ui.theme.DarkCard
import com.dmitryy.notesapp.ui.theme.NeonCyan
import com.dmitryy.notesapp.ui.theme.NeonPink
import com.dmitryy.notesapp.ui.theme.TextPrimary
import com.dmitryy.notesapp.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    notes: List<Note>,
    showTrashBin: Boolean,
    viewMode: ViewMode,
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit,
    onDeleteNote: (Note) -> Unit,
    onPinNote: (Note) -> Unit,
    onToggleViewMode: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onAbout: () -> Unit,
    onExportJson: () -> Unit,
    onImportJson: () -> Unit,
    onTrashBin: () -> Unit,
    onPinned: () -> Unit,
    onUrlNotes: () -> Unit,
    onSettings: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    val showSearch = notes.isNotEmpty() || searchQuery.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name), color = NeonCyan) },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.menu), tint = NeonCyan)
                    }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
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
                            leadingIcon = {
                                val icon = if (viewMode == ViewMode.GRID) {
                                    Icons.Filled.ViewList
                                } else {
                                    Icons.Filled.ViewModule
                                }
                                Icon(
                                    imageVector = icon,
                                    contentDescription = stringResource(
                                        if (viewMode == ViewMode.GRID) R.string.list_view else R.string.grid_view
                                    )
                                )
                            },
                            onClick = {
                                showMenu = false
                                onToggleViewMode()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.about)) },
                            onClick = {
                                showMenu = false
                                onAbout()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.export_json)) },
                            onClick = {
                                showMenu = false
                                onExportJson()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.import_json)) },
                            onClick = {
                                showMenu = false
                                onImportJson()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.pinned_notes)) },
                            onClick = {
                                showMenu = false
                                onPinned()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.notes_with_urls)) },
                            onClick = {
                                showMenu = false
                                onUrlNotes()
                            }
                        )
                        if (showTrashBin) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.trash_bin)) },
                                onClick = {
                                    showMenu = false
                                    onTrashBin()
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.settings)) },
                            onClick = {
                                showMenu = false
                                onSettings()
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBg
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = NeonCyan,
                contentColor = Color.Black,
                modifier = Modifier.testTag("add_note_fab")
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_note))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showSearch) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearchQueryChanged(it)
                    },
                    placeholder = { Text(stringResource(R.string.search_notes), color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.search), tint = NeonCyan) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                onClearSearch()
                            }) {
                                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.clear), tint = NeonPink)
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkCard,
                        unfocusedContainerColor = DarkCard,
                        focusedTextColor = NeonCyan,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = NeonCyan,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (notes.isEmpty() && searchQuery.isEmpty()) {
                    Text(
                        text = stringResource(R.string.add_first_note),
                        color = NeonPink,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (notes.isEmpty() && searchQuery.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.no_results_found),
                        color = TextSecondary,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    NotesCollection(
                        notes = notes,
                        viewMode = viewMode,
                        onNoteClick = onNoteClick,
                        onDeleteNote = onDeleteNote,
                        onPinNote = onPinNote
                    )
                }
            }
        }
    }
}

@Composable
private fun NotesCollection(
    notes: List<Note>,
    viewMode: ViewMode,
    onNoteClick: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onPinNote: (Note) -> Unit,
) {
    when (viewMode) {
        ViewMode.LIST -> NotesList(
            notes = notes,
            onNoteClick = onNoteClick,
            onDeleteNote = onDeleteNote,
            onPinNote = onPinNote
        )

        ViewMode.GRID -> NotesGrid(
            notes = notes,
            onNoteClick = onNoteClick
        )
    }
}

@Composable
private fun NotesList(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onPinNote: (Note) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notes, key = { it.id }) { note ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    when (it) {
                        SwipeToDismissBoxValue.EndToStart -> {
                            onDeleteNote(note)
                            true
                        }

                        SwipeToDismissBoxValue.StartToEnd -> {
                            onPinNote(note)
                            false
                        }

                        else -> false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val color = when (dismissState.dismissDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> NeonCyan
                        SwipeToDismissBoxValue.EndToStart -> Color.Red
                        else -> Color.Transparent
                    }
                    val alignment = when (dismissState.dismissDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        else -> Alignment.Center
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color, RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        contentAlignment = alignment
                    ) {
                        when (dismissState.dismissDirection) {
                            SwipeToDismissBoxValue.StartToEnd -> Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = stringResource(R.string.pin_note),
                                tint = Color.Black
                            )

                            SwipeToDismissBoxValue.EndToStart -> Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete),
                                tint = Color.White
                            )

                            else -> {}
                        }
                    }
                },
                content = {
                    NoteItem(note = note, onClick = { onNoteClick(note) }, isGrid = false)
                },
                enableDismissFromStartToEnd = true
            )
        }
    }
}

@Composable
private fun NotesGrid(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(notes, key = { it.id }) { note ->
            NoteItem(note = note, onClick = { onNoteClick(note) }, isGrid = true)
        }
    }
}

@Composable
fun NoteItem(note: Note, onClick: () -> Unit, isGrid: Boolean) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(note.createdAt))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(DarkCard)
            .border(2.dp, NeonCyan, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(if (isGrid) 12.dp else 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = note.title,
                color = NeonCyan,
                fontSize = if (isGrid) 16.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (note.isPinned) {
                Icon(
                    imageVector = Icons.Filled.PushPin,
                    contentDescription = stringResource(R.string.pin_note),
                    tint = NeonPink,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Text(
            text = note.content,
            color = TextPrimary,
            fontSize = if (isGrid) 13.sp else 14.sp,
            maxLines = if (isGrid) 3 else 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = formattedDate,
            color = TextSecondary,
            fontSize = if (isGrid) 11.sp else 12.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
        )
    }
}
