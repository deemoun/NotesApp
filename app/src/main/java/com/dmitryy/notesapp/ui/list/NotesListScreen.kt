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
import com.dmitryy.notesapp.ui.components.NotesDropdownMenu
import com.dmitryy.notesapp.ui.components.NotesSearchBar
import com.dmitryy.notesapp.ui.components.EmptyNotesState
import com.dmitryy.notesapp.ui.components.NoSearchResultsState
import com.dmitryy.notesapp.utils.SwipeUtils
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
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.testTag("menu_button")) {
                        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.menu), tint = NeonCyan)
                    }
                    NotesDropdownMenu(
                        expanded = showMenu,
                        viewMode = viewMode,
                        showTrashBin = showTrashBin,
                        onDismiss = { showMenu = false },
                        onToggleViewMode = onToggleViewMode,
                        onAbout = onAbout,
                        onExportJson = onExportJson,
                        onImportJson = onImportJson,
                        onPinned = onPinned,
                        onUrlNotes = onUrlNotes,
                        onTrashBin = onTrashBin,
                        onSettings = onSettings
                    )
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
                NotesSearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = {
                        searchQuery = it
                        onSearchQueryChanged(it)
                    },
                    onClearSearch = {
                        searchQuery = ""
                        onClearSearch()
                    }
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    notes.isEmpty() && searchQuery.isEmpty() -> EmptyNotesState()
                    notes.isEmpty() && searchQuery.isNotEmpty() -> NoSearchResultsState()
                    else -> NotesCollection(
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

@OptIn(ExperimentalMaterial3Api::class)
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
                confirmValueChange = { dismissValue ->
                    SwipeUtils.handleSwipeAction(
                        dismissValue = dismissValue,
                        note = note,
                        onDelete = onDeleteNote,
                        onTogglePin = onPinNote
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
