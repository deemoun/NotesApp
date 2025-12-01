package com.dmitryy.notesapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dmitryy.notesapp.ui.theme.NeonCyan
import com.dmitryy.notesapp.ui.theme.TextPrimary
import com.dmitryy.notesapp.ui.theme.TextSecondary

/**
 * Styled text field for note title input.
 */
@Composable
fun NoteTitleField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = NeonCyan,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        cursorBrush = SolidColor(NeonCyan),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = TextSecondary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            innerTextField()
        },
        modifier = modifier
            .fillMaxWidth()
            .testTag("title_field")
    )
}

/**
 * Styled text field for note content input.
 */
@Composable
fun NoteContentField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = TextPrimary,
            fontSize = 16.sp
        ),
        cursorBrush = SolidColor(NeonCyan),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = TextSecondary,
                    fontSize = 16.sp
                )
            }
            innerTextField()
        },
        modifier = modifier
            .fillMaxWidth()
            .testTag("content_field")
    )
}
