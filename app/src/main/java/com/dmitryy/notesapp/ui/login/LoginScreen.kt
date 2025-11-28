package com.dmitryy.notesapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.ui.theme.DarkBg
import com.dmitryy.notesapp.ui.theme.NeonCyan
import com.dmitryy.notesapp.ui.theme.NeonPink
import com.dmitryy.notesapp.ui.theme.TextPrimary
import com.dmitryy.notesapp.ui.theme.TextSecondary
import kotlinx.coroutines.delay

enum class LoginMode {
    SETUP, CONFIRM, VALIDATE
}

@Composable
fun LoginScreen(
    mode: LoginMode,
    onPasscodeEntered: (String) -> Unit
) {
    var passcode by remember(mode) { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(mode) {
        delay(100) // Small delay to ensure layout is complete
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(passcode) {
        if (passcode.length == 4) {
            onPasscodeEntered(passcode)
            passcode = ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusRequester.requestFocus()
                keyboardController?.show()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Title
            Text(
                text = when (mode) {
                    LoginMode.SETUP -> stringResource(R.string.setup_passcode)
                    LoginMode.CONFIRM -> stringResource(R.string.confirm_passcode)
                    LoginMode.VALIDATE -> stringResource(R.string.enter_passcode)
                },
                color = NeonCyan,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.passcode_hint),
                color = TextSecondary,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // PIN Display
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in 0 until 4) {
                    PinDigitBox(
                        digit = passcode.getOrNull(i)?.toString(),
                        isFilled = i < passcode.length
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Hidden TextField for input
            BasicTextField(
                value = passcode,
                onValueChange = { newValue ->
                    if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                        passcode = newValue
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
                cursorBrush = SolidColor(Color.Transparent),
                modifier = Modifier
                    .width(1.dp)
                    .height(1.dp)
                    .focusRequester(focusRequester)
            )
        }
    }
}

@Composable
fun PinDigitBox(
    digit: String?,
    isFilled: Boolean
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                color = if (isFilled) NeonCyan.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 2.dp,
                color = if (isFilled) NeonCyan else TextSecondary,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (digit != null) {
            Text(
                text = "●",
                color = NeonPink,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
