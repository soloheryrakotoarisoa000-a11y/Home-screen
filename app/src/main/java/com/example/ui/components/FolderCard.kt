package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppItem
import com.example.data.model.FolderItem
import com.example.ui.theme.GaramondFontFamily
import com.example.ui.theme.SacredGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FolderCard(
    folder: FolderItem,
    allApps: List<AppItem>,
    iconSizeDp: Int,
    showLabel: Boolean,
    labelTextSizeSp: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val folderApps = remember(folder.appPackageNames, allApps) {
        val appMap = allApps.associateBy { it.packageName }
        folder.appPackageNames.mapNotNull { appMap[it] }.take(4)
    }

    val shape = RoundedCornerShape(22)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width((iconSizeDp + 24).dp)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 2.dp)
            .testTag("folder_${folder.id}")
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSizeDp.dp)
                .shadow(elevation = 4.dp, shape = shape)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f))
                .border(
                    width = 1.5.dp,
                    color = SacredGold.copy(alpha = 0.6f),
                    shape = shape
                )
                .padding(4.dp)
        ) {
            if (folderApps.isEmpty()) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = folder.name,
                    tint = SacredGold,
                    modifier = Modifier.size((iconSizeDp * 0.6f).dp)
                )
            } else {
                FlowRow(
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center
                ) {
                    val miniSize = (iconSizeDp * 0.38f).coerceAtLeast(16f).dp
                    folderApps.forEach { app ->
                        val bitmap = remember(app.packageName) {
                            app.icon?.let { drawableToBitmap(it) }
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(miniSize)
                                .padding(1.dp)
                        ) {
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.size(miniSize)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = folder.name,
                fontFamily = GaramondFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = labelTextSizeSp.sp,
                color = SacredGold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                lineHeight = (labelTextSizeSp + 2).sp,
                modifier = Modifier.width((iconSizeDp + 20).dp)
            )
        }
    }
}
