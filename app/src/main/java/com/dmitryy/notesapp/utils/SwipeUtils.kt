package com.dmitryy.notesapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.data.Note

/**
 * Utility object for handling swipe-to-dismiss actions on notes.
 */
object SwipeUtils {

    /**
     * Determines the action to take when a swipe gesture is confirmed.
     * 
     * @param dismissValue The direction of the swipe
     * @param note The note being swiped
     * @param onDelete Callback to delete the note
     * @param onTogglePin Callback to toggle the pin state
     * @return true if the item should be dismissed, false otherwise
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun handleSwipeAction(
        dismissValue: SwipeToDismissBoxValue,
        note: Note,
        onDelete: (Note) -> Unit,
        onTogglePin: (Note) -> Unit
    ): Boolean {
        return when (dismissValue) {
            SwipeToDismissBoxValue.EndToStart -> {
                // Swipe left-to-right: Delete
                onDelete(note)
                true // Dismiss the item
            }
            SwipeToDismissBoxValue.StartToEnd -> {
                // Swipe right-to-left: Toggle pin
                onTogglePin(note)
                false // Don't dismiss, just toggle
            }
            else -> false
        }
    }

    /**
     * Gets the background color for the swipe action.
     * 
     * @param direction The swipe direction
     * @param note The note being swiped
     * @return The color to display
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun getSwipeBackgroundColor(
        direction: SwipeToDismissBoxValue,
        note: Note
    ): Color {
        return when (direction) {
            SwipeToDismissBoxValue.StartToEnd -> {
                // Pin/Unpin: Cyan for pin, Orange for unpin
                if (note.isPinned) Color(0xFFFFA500) else Color(0xFF00BCD4)
            }
            SwipeToDismissBoxValue.EndToStart -> {
                // Delete: Red
                Color.Red
            }
            else -> Color.Transparent
        }
    }

    /**
     * Gets the alignment for the swipe background icon.
     * 
     * @param direction The swipe direction
     * @return The alignment for the icon
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun getSwipeAlignment(direction: SwipeToDismissBoxValue): Alignment {
        return when (direction) {
            SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
            SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    }

    /**
     * Composable function to render the swipe background with appropriate icon.
     * 
     * @param direction The swipe direction
     * @param note The note being swiped
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SwipeBackground(
        direction: SwipeToDismissBoxValue,
        note: Note
    ) {
        val color = getSwipeBackgroundColor(direction, note)
        val alignment = getSwipeAlignment(direction)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color, RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = alignment
        ) {
            when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    // Pin/Unpin icon
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = if (note.isPinned) "Unpin" else stringResource(R.string.pin_note),
                        tint = if (note.isPinned) Color.White else Color.Black
                    )
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    // Delete icon
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = Color.White
                    )
                }
                else -> {}
            }
        }
    }
}
