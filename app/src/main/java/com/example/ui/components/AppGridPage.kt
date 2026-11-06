package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppItem
import com.example.data.model.FolderItem
import com.example.data.model.LauncherPreferences
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.SacredGold

sealed interface PageGridItem {
    data class App(val app: AppItem) : PageGridItem
    data class Folder(val folder: FolderItem) : PageGridItem
}

@Composable
fun AppGridPage(
    pageIndex: Int,
    allApps: List<AppItem>,
    displayApps: List<AppItem>,
    folders: List<FolderItem>,
    preferences: LauncherPreferences,
    isLoading: Boolean,
    onAppClick: (AppItem) -> Unit,
    onAppLongClick: (AppItem) -> Unit,
    onFolderClick: (FolderItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val capacity = (preferences.columns * preferences.rows).coerceAtLeast(1)

    // Gather folders belonging to this page
    val pageFolders = remember(folders, pageIndex) {
        folders.filter { it.pageIndex == pageIndex }
    }

    // Folders take up slots on this page, remaining slots are filled with apps
    val pageApps = remember(displayApps, pageIndex, capacity, pageFolders.size) {
        val appSlots = (capacity - pageFolders.size).coerceAtLeast(1)
        val startIndex = pageIndex * appSlots
        if (startIndex < displayApps.size) {
            val endIndex = minOf(startIndex + appSlots, displayApps.size)
            displayApps.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
    }

    val gridItems = remember(pageFolders, pageApps) {
        val list = mutableListOf<PageGridItem>()
        pageFolders.forEach { list.add(PageGridItem.Folder(it)) }
        pageApps.forEach { list.add(PageGridItem.App(it)) }
        list
    }

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = SacredGold,
                strokeWidth = 3.dp
            )
        }
        return
    }

    if (gridItems.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize().padding(24.dp)
        ) {
            Text(
                text = "Tsy misy rindranasa eto amin'ity pejy ity",
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        return
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(preferences.columns),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalArrangement = Arrangement.SpaceEvenly,
            userScrollEnabled = true,
            modifier = Modifier
                .fillMaxSize()
                .testTag("app_grid_page_$pageIndex")
        ) {
            items(
                items = gridItems,
                key = { item ->
                    when (item) {
                        is PageGridItem.App -> "app_${item.app.packageName}"
                        is PageGridItem.Folder -> "folder_${item.folder.id}"
                    }
                }
            ) { item ->
                when (item) {
                    is PageGridItem.App -> {
                        AppIconView(
                            app = item.app,
                            iconSizeDp = preferences.iconSizeDp,
                            iconShape = preferences.iconShape,
                            showLabel = preferences.showAppLabels,
                            labelTextSizeSp = preferences.labelTextSizeSp,
                            onClick = { onAppClick(item.app) },
                            onLongClick = { onAppLongClick(item.app) }
                        )
                    }
                    is PageGridItem.Folder -> {
                        FolderCard(
                            folder = item.folder,
                            allApps = allApps,
                            iconSizeDp = preferences.iconSizeDp,
                            showLabel = preferences.showAppLabels,
                            labelTextSizeSp = preferences.labelTextSizeSp,
                            onClick = { onFolderClick(item.folder) }
                        )
                    }
                }
            }
        }
    }
}
