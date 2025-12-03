package com.dmitryy.notesapp.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.ui.theme.DarkBg
import com.dmitryy.notesapp.ui.theme.DarkCard
import com.dmitryy.notesapp.ui.theme.NeonCyan
import com.dmitryy.notesapp.ui.theme.NeonPink
import com.dmitryy.notesapp.ui.theme.TextPrimary
import com.dmitryy.notesapp.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onClearCache: () -> Unit,
    onNukeDatabase: () -> Unit,
    onPasscodeToggle: (Boolean) -> Unit,
    isPasscodeEnabled: Boolean,
    showDebugSection: Boolean = false,
    debugIsRooted: Boolean = false,
    debugHasEmulator: Boolean = false,
    debugHasMagisk: Boolean = false
) {
    var showNukeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings), color = NeonCyan) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
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
                .padding(16.dp)
        ) {
            // Require Passcode Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = isPasscodeEnabled,
                    onCheckedChange = { onPasscodeToggle(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = NeonCyan,
                        uncheckedColor = TextSecondary,
                        checkmarkColor = Color.Black
                    ),
                    modifier = Modifier.testTag("passcode_checkbox")
                )
                Text(
                    text = stringResource(R.string.require_passcode),
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clear Cache Button
            Button(
                onClick = onClearCache,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkCard,
                    contentColor = NeonCyan
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .testTag("clear_cache_button")
            ) {
                Text(stringResource(R.string.clear_cache))
            }

            // Nuke Database Button
            Button(
                onClick = { showNukeDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkCard,
                    contentColor = NeonPink
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .testTag("nuke_database_button")
            ) {
                Text(stringResource(R.string.nuke_database), fontWeight = FontWeight.Bold)
            }

            // Debug Section (only shown if any flag is true)
            if (showDebugSection) {
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = stringResource(R.string.debug_section),
                    color = NeonPink,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Text(
                    text = "isRooted: $debugIsRooted",
                    color = if (debugIsRooted) NeonPink else TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Text(
                    text = "hasEmulator: $debugHasEmulator",
                    color = if (debugHasEmulator) NeonPink else TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Text(
                    text = "hasMagisk: $debugHasMagisk",
                    color = if (debugHasMagisk) NeonPink else TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    // Nuke Confirmation Dialog
    if (showNukeDialog) {
        AlertDialog(
            onDismissRequest = { showNukeDialog = false },
            title = {
                Text(stringResource(R.string.nuke_confirmation_title), color = NeonPink, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(stringResource(R.string.nuke_confirmation_message), color = TextPrimary)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNukeDialog = false
                        onNukeDatabase()
                    }
                ) {
                    Text(stringResource(R.string.nuke_database), color = NeonPink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showNukeDialog = false }
                ) {
                    Text(stringResource(R.string.cancel), color = TextSecondary)
                }
            },
            containerColor = DarkCard
        )
    }
}
