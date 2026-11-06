package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppItem
import com.example.data.model.FolderItem
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.GaramondFontFamily
import com.example.ui.theme.SacredGold

@Composable
fun MoveToPageDialog(
    app: AppItem,
    totalPages: Int,
    currentPage: Int,
    folders: List<FolderItem>,
    onSelectPage: (Int) -> Unit,
    onSelectFolder: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Afindra ny rindranasa",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = BrightGold
                )
                Text(
                    text = app.label,
                    fontFamily = GaramondFontFamily,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth().testTag("move_to_page_list")
            ) {
                item {
                    Text(
                        text = "Hisafidy Pejin'ny Baiboly:",
                        fontFamily = CinzelFontFamily,
                        fontSize = 12.sp,
                        color = SacredGold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(totalPages) { pageIdx ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (pageIdx == currentPage) SacredGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectPage(pageIdx) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Book, contentDescription = null, tint = SacredGold)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Pejy ${pageIdx + 1}${if (pageIdx == currentPage) " (Eto)" else ""}",
                                fontFamily = GaramondFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                if (folders.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = SacredGold.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Na ampidirina anaty Lahatahiry:",
                            fontFamily = CinzelFontFamily,
                            fontSize = 12.sp,
                            color = SacredGold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(folders.size) { idx ->
                        val folder = folders[idx]
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSelectFolder(folder.id) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = BrightGold)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = folder.name,
                                    fontFamily = GaramondFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Aoka ihany", fontFamily = GaramondFontFamily, color = SacredGold)
            }
        }
    )
}
