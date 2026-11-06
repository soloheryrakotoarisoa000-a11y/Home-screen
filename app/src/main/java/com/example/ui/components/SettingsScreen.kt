package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnimationSpeed
import com.example.data.model.IconShape
import com.example.data.model.LauncherPreferences
import com.example.data.model.PageTransitionStyle
import com.example.data.model.SortOrder
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.GaramondFontFamily
import com.example.ui.theme.MidnightDark
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.ParchmentLight
import com.example.ui.theme.RoyalBurgundyDark
import com.example.ui.theme.SacredGold

@Composable
fun SettingsScreen(
    preferences: LauncherPreferences,
    onUpdateIconSize: (Int) -> Unit,
    onUpdateColumns: (Int) -> Unit,
    onUpdateRows: (Int) -> Unit,
    onUpdateShowLabels: (Boolean) -> Unit,
    onUpdateLabelSize: (Int) -> Unit,
    onUpdateShowPageNumbers: (Boolean) -> Unit,
    onUpdateShowVerseHeader: (Boolean) -> Unit,
    onUpdateTheme: (String) -> Unit,
    onUpdateAnimationSpeed: (AnimationSpeed) -> Unit,
    onUpdatePageTransition: (PageTransitionStyle) -> Unit,
    onUpdateIconShape: (IconShape) -> Unit,
    onUpdateSortOrder: (SortOrder) -> Unit,
    onOpenDefaultLauncherSettings: () -> Unit,
    onCreateFolderClick: () -> Unit,
    onOpenAbout: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Endrika", "Kisary", "Pejy", "Filaharana", "Fihetsika")

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize().testTag("settings_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarPadding, bottom = navBarPadding)
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose, modifier = Modifier.testTag("settings_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Hiverina",
                            tint = SacredGold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "FANDRINDRAM-PEJY",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        letterSpacing = 1.sp,
                        color = BrightGold
                    )
                }

                Row {
                    IconButton(onClick = onOpenAbout, modifier = Modifier.testTag("settings_about_button")) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Momba",
                            tint = SacredGold
                        )
                    }
                }
            }

            // Live Preview Card (Always visible at the top so user sees immediate results)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .border(1.dp, SacredGold.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Column {
                        Text(
                            text = "Fijerena Mialoha (Live Preview)",
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = SacredGold
                        )
                        Text(
                            text = "${preferences.columns} tsanganana × ${preferences.rows} laharana • ${preferences.iconSizeDp}dp",
                            fontFamily = GaramondFontFamily,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    // Simulated Icon
                    val previewShape = when (preferences.iconShape) {
                        IconShape.SQUIRCLE -> RoundedCornerShape(26)
                        IconShape.ROUNDED -> RoundedCornerShape(16)
                        IconShape.CIRCLE -> CircleShape
                        IconShape.DEFAULT -> RoundedCornerShape(12)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size((preferences.iconSizeDp * 0.75f).coerceIn(36f, 54f).dp)
                                .clip(previewShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, SacredGold, previewShape)
                        ) {
                            Text(text = "†", color = BrightGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        if (preferences.showAppLabels) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Baiboly",
                                fontFamily = GaramondFontFamily,
                                fontSize = preferences.labelTextSizeSp.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = SacredGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BrightGold
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontFamily = CinzelFontFamily,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    )
                }
            }

            // Tab Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                when (selectedTab) {
                    0 -> AppearanceTab(
                        theme = preferences.themeModeName,
                        showVerseHeader = preferences.showVerseHeader,
                        showPageNumbers = preferences.showPageNumbers,
                        onThemeSelect = onUpdateTheme,
                        onToggleVerse = onUpdateShowVerseHeader,
                        onTogglePageNumbers = onUpdateShowPageNumbers,
                        onOpenDefaultLauncher = onOpenDefaultLauncherSettings
                    )
                    1 -> IconsTab(
                        iconSize = preferences.iconSizeDp,
                        iconShape = preferences.iconShape,
                        showLabels = preferences.showAppLabels,
                        labelSize = preferences.labelTextSizeSp,
                        onIconSizeChange = onUpdateIconSize,
                        onIconShapeChange = onUpdateIconShape,
                        onShowLabelsChange = onUpdateShowLabels,
                        onLabelSizeChange = onUpdateLabelSize
                    )
                    2 -> LayoutTab(
                        columns = preferences.columns,
                        rows = preferences.rows,
                        onColumnsChange = onUpdateColumns,
                        onRowsChange = onUpdateRows,
                        onCreateFolder = onCreateFolderClick
                    )
                    3 -> SortingTab(
                        currentSort = preferences.sortOrder,
                        onSortChange = onUpdateSortOrder
                    )
                    4 -> AnimationsTab(
                        transition = preferences.pageTransitionStyle,
                        speed = preferences.animationSpeed,
                        onTransitionChange = onUpdatePageTransition,
                        onSpeedChange = onUpdateAnimationSpeed
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AppearanceTab(
    theme: String,
    showVerseHeader: Boolean,
    showPageNumbers: Boolean,
    onThemeSelect: (String) -> Unit,
    onToggleVerse: (Boolean) -> Unit,
    onTogglePageNumbers: (Boolean) -> Unit,
    onOpenDefaultLauncher: () -> Unit
) {
    Text(
        text = "Loko sy Endriky ny Baiboly (Themes)",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ThemeOptionCard(
            title = "Midnight",
            subtitle = "Manga alina & Volamena",
            bgColor = MidnightNavy,
            borderColor = SacredGold,
            isSelected = theme == "MIDNIGHT_BIBLE",
            onClick = { onThemeSelect("MIDNIGHT_BIBLE") },
            modifier = Modifier.weight(1f)
        )
        ThemeOptionCard(
            title = "Parchment",
            subtitle = "Taratasy fotsy & Ranomainty",
            bgColor = ParchmentLight,
            borderColor = AntiqueGold,
            isSelected = theme == "PARCHMENT_IVORY",
            onClick = { onThemeSelect("PARCHMENT_IVORY") },
            modifier = Modifier.weight(1f)
        )
        ThemeOptionCard(
            title = "Burgundy",
            subtitle = "Mena antitra masina",
            bgColor = RoyalBurgundyDark,
            borderColor = BrightGold,
            isSelected = theme == "ROYAL_BURGUNDY",
            onClick = { onThemeSelect("ROYAL_BURGUNDY") },
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(20.dp))
    HorizontalDivider(color = SacredGold.copy(alpha = 0.3f))
    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = "Andinin-tsoratra Masina sy Pejy",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Asehoy ny Soratra Masina eo an-tampony",
                fontFamily = GaramondFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Deoteronomia 28:6 amin'ny pejin'ny rindranasa",
                fontFamily = GaramondFontFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Switch(
            checked = showVerseHeader,
            onCheckedChange = onToggleVerse,
            colors = SwitchDefaults.colors(checkedThumbColor = BrightGold, checkedTrackColor = SacredGold)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Asehoy ny laharan'ny pejy",
                fontFamily = GaramondFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Pejy 1, 2, 3... toy ny Baiboly tena izy",
                fontFamily = GaramondFontFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Switch(
            checked = showPageNumbers,
            onCheckedChange = onTogglePageNumbers,
            colors = SwitchDefaults.colors(checkedThumbColor = BrightGold, checkedTrackColor = SacredGold)
        )
    }

    Spacer(modifier = Modifier.height(24.dp))
    HorizontalDivider(color = SacredGold.copy(alpha = 0.3f))
    Spacer(modifier = Modifier.height(14.dp))

    // Default Launcher Button
    ElevatedButton(
        onClick = onOpenDefaultLauncher,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = SacredGold,
            contentColor = MidnightDark
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("set_default_launcher_button")
    ) {
        Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Ataovy Launcher Fototra (Default Home)",
            fontFamily = CinzelFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ThemeOptionCard(
    title: String,
    subtitle: String,
    bgColor: Color,
    borderColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = if (isSelected) BrightGold else borderColor.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (bgColor == ParchmentLight) Color(0xFF2A1C0E) else BrightGold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontFamily = GaramondFontFamily,
                fontSize = 10.sp,
                color = if (bgColor == ParchmentLight) Color(0xFF5A4838) else Color(0xFFD6C8AC)
            )
        }
    }
}

@Composable
private fun IconsTab(
    iconSize: Int,
    iconShape: IconShape,
    showLabels: Boolean,
    labelSize: Int,
    onIconSizeChange: (Int) -> Unit,
    onIconShapeChange: (IconShape) -> Unit,
    onShowLabelsChange: (Boolean) -> Unit,
    onLabelSizeChange: (Int) -> Unit
) {
    Text(
        text = "Haben'ny Kisary (Icon Size): ${iconSize}dp",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Slider(
        value = iconSize.toFloat(),
        onValueChange = { onIconSizeChange(it.toInt()) },
        valueRange = 44f..72f,
        steps = 6,
        colors = SliderDefaults.colors(thumbColor = BrightGold, activeTrackColor = SacredGold),
        modifier = Modifier.fillMaxWidth().testTag("icon_size_slider")
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Endriky ny Kisary (Icon Shape)",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        IconShape.values().forEach { shape ->
            FilterChip(
                selected = iconShape == shape,
                onClick = { onIconShapeChange(shape) },
                label = {
                    Text(
                        text = shape.displayName,
                        fontFamily = GaramondFontFamily,
                        fontSize = 12.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SacredGold.copy(alpha = 0.3f),
                    selectedLabelColor = BrightGold
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = iconShape == shape,
                    borderColor = SacredGold.copy(alpha = 0.5f),
                    selectedBorderColor = BrightGold
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    HorizontalDivider(color = SacredGold.copy(alpha = 0.3f))
    Spacer(modifier = Modifier.height(14.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Asehoy ny anaran'ny rindranasa",
            fontFamily = GaramondFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = showLabels,
            onCheckedChange = onShowLabelsChange,
            colors = SwitchDefaults.colors(checkedThumbColor = BrightGold, checkedTrackColor = SacredGold)
        )
    }

    if (showLabels) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Haben'ny soratra: ${labelSize}sp",
            fontFamily = CinzelFontFamily,
            fontSize = 13.sp,
            color = SacredGold
        )
        Slider(
            value = labelSize.toFloat(),
            onValueChange = { onLabelSizeChange(it.toInt()) },
            valueRange = 10f..16f,
            steps = 5,
            colors = SliderDefaults.colors(thumbColor = BrightGold, activeTrackColor = SacredGold),
            modifier = Modifier.fillMaxWidth().testTag("label_size_slider")
        )
    }
}

@Composable
private fun LayoutTab(
    columns: Int,
    rows: Int,
    onColumnsChange: (Int) -> Unit,
    onRowsChange: (Int) -> Unit,
    onCreateFolder: () -> Unit
) {
    Text(
        text = "Isan'ny Tsanganana (Columns): $columns",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        listOf(3, 4, 5).forEach { col ->
            FilterChip(
                selected = columns == col,
                onClick = { onColumnsChange(col) },
                label = { Text("$col Tsanganana", fontFamily = GaramondFontFamily, fontSize = 13.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SacredGold.copy(alpha = 0.3f),
                    selectedLabelColor = BrightGold
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = "Isan'ny Laharana (Rows): $rows",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        listOf(4, 5, 6, 7).forEach { row ->
            FilterChip(
                selected = rows == row,
                onClick = { onRowsChange(row) },
                label = { Text("$row Laharana", fontFamily = GaramondFontFamily, fontSize = 13.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SacredGold.copy(alpha = 0.3f),
                    selectedLabelColor = BrightGold
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = "Isan'ny rindranasa isaky ny pejy: ${columns * rows}",
        fontFamily = GaramondFontFamily,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )

    Spacer(modifier = Modifier.height(20.dp))
    HorizontalDivider(color = SacredGold.copy(alpha = 0.3f))
    Spacer(modifier = Modifier.height(14.dp))

    OutlinedButton(
        onClick = onCreateFolder,
        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("create_folder_button")
    ) {
        Icon(imageVector = Icons.Default.CreateNewFolder, contentDescription = null, tint = SacredGold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Hamorona Lahatahiry Vaovao (Create Folder)",
            fontFamily = CinzelFontFamily,
            color = BrightGold,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun SortingTab(
    currentSort: SortOrder,
    onSortChange: (SortOrder) -> Unit
) {
    Text(
        text = "Filaharan'ny Rindranasa (Sorting)",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SortOrder.values().forEach { order ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (currentSort == order) SacredGold.copy(alpha = 0.15f) else Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSortChange(order) }
                    .padding(vertical = 4.dp, horizontal = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = currentSort == order,
                        onClick = { onSortChange(order) },
                        colors = RadioButtonDefaults.colors(selectedColor = BrightGold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = order.malagasyName,
                            fontFamily = GaramondFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = order.displayName,
                            fontFamily = GaramondFontFamily,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimationsTab(
    transition: PageTransitionStyle,
    speed: AnimationSpeed,
    onTransitionChange: (PageTransitionStyle) -> Unit,
    onSpeedChange: (AnimationSpeed) -> Unit
) {
    Text(
        text = "Fomba Fihodinan'ny Pejy (Transition Style)",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        PageTransitionStyle.values().forEach { style ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (transition == style) SacredGold.copy(alpha = 0.15f) else Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onTransitionChange(style) }
                    .padding(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = transition == style,
                        onClick = { onTransitionChange(style) },
                        colors = RadioButtonDefaults.colors(selectedColor = BrightGold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = style.displayName,
                        fontFamily = GaramondFontFamily,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    HorizontalDivider(color = SacredGold.copy(alpha = 0.3f))
    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = "Hafainganana (Speed)",
        fontFamily = CinzelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrightGold
    )
    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        AnimationSpeed.values().forEach { s ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (speed == s) SacredGold.copy(alpha = 0.15f) else Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSpeedChange(s) }
                    .padding(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = speed == s,
                        onClick = { onSpeedChange(s) },
                        colors = RadioButtonDefaults.colors(selectedColor = BrightGold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = s.displayName,
                        fontFamily = GaramondFontFamily,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
