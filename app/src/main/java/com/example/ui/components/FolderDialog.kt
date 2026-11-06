package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppItem
import com.example.data.model.FolderItem
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.GaramondFontFamily
import com.example.ui.theme.SacredGold

@Composable
fun FolderContentsDialog(
    folder: FolderItem,
    allApps: List<AppItem>,
    onLaunchApp: (AppItem) -> Unit,
    onRemoveFromFolder: (String) -> Unit,
    onRenameFolder: (String) -> Unit,
    onDeleteFolder: () -> Unit,
    onDismiss: () -> Unit
) {
    var showRenameDialog by remember { mutableStateOf(false) }
    var newFolderName by remember(folder.name) { mutableStateOf(folder.name) }

    val folderApps = remember(folder.appPackageNames, allApps) {
        val appMap = allApps.associateBy { it.packageName }
        folder.appPackageNames.mapNotNull { appMap[it] }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 10.dp,
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, SacredGold.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .testTag("folder_contents_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with Rename and Delete
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = folder.name,
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BrightGold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(
                            onClick = { showRenameDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Hanova anarana",
                                tint = SacredGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Row {
                        IconButton(
                            onClick = onDeleteFolder,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hamafa lahatahiry",
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hakatona",
                                tint = SacredGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = SacredGold.copy(alpha = 0.3f),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                if (folderApps.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Text(
                            text = "Tsy misy rindranasa ato amin'ity lahatahiry ity.\n(Tsindrio maharitra ny rindranasa iray hanampiana azy eto)",
                            fontFamily = GaramondFontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp)
                    ) {
                        items(folderApps, key = { it.packageName }) { app ->
                            val bitmap = remember(app.packageName) {
                                app.icon?.let { drawableToBitmap(it) }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onLaunchApp(app) }
                                    .padding(4.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .border(1.dp, SacredGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                                ) {
                                    if (bitmap != null) {
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = app.label,
                                            modifier = Modifier.size(42.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = app.label,
                                    fontFamily = GaramondFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "Esorina",
                                    fontFamily = GaramondFontFamily,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .clickable { onRemoveFromFolder(app.packageName) }
                                        .padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = {
                Text(text = "Hanova anaran'ny lahatahiry", fontFamily = CinzelFontFamily, color = BrightGold)
            },
            text = {
                OutlinedTextField(
                    value = newFolderName,
                    onValueChange = { newFolderName = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrightGold,
                        unfocusedBorderColor = SacredGold
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newFolderName.isNotBlank()) {
                            onRenameFolder(newFolderName)
                        }
                        showRenameDialog = false
                    }
                ) {
                    Text("Ekena", fontFamily = GaramondFontFamily, fontWeight = FontWeight.Bold, color = SacredGold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("Aoka ihany", fontFamily = GaramondFontFamily, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
}

@Composable
fun CreateFolderDialog(
    initialTargetApp: AppItem?,
    onCreate: (name: String) -> Unit,
    onDismiss: () -> Unit
) {
    var folderName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Hamorona lahatahiry vaovao", fontFamily = CinzelFontFamily, color = BrightGold)
        },
        text = {
            Column {
                if (initialTargetApp != null) {
                    Text(
                        text = "Hampidirina ao: ${initialTargetApp.label}",
                        fontFamily = GaramondFontFamily,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                OutlinedTextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    label = { Text("Anaran'ny lahatahiry") },
                    placeholder = { Text("Ohatra: Fiangonana, Asa...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrightGold,
                        unfocusedBorderColor = SacredGold
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("folder_name_input")
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(folderName) },
                colors = ButtonDefaults.textButtonColors(contentColor = SacredGold),
                modifier = Modifier.testTag("create_folder_confirm")
            ) {
                Text("Hamorona", fontFamily = CinzelFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Aoka ihany", fontFamily = GaramondFontFamily)
            }
        }
    )
}
