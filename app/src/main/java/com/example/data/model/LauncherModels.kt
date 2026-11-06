package com.example.data.model

import android.graphics.drawable.Drawable

data class AppItem(
    val packageName: String,
    val activityName: String,
    val label: String,
    val installTime: Long = 0L,
    val updateTime: Long = 0L,
    val appSizeBytes: Long = 0L,
    val launchCount: Int = 0,
    val lastLaunched: Long = 0L,
    val icon: Drawable? = null
)

data class FolderItem(
    val id: String,
    val name: String,
    val appPackageNames: List<String> = emptyList(),
    val pageIndex: Int = 0
)

enum class SortOrder(val displayName: String, val malagasyName: String) {
    ALPHA_AZ("Alphabetical (A–Z)", "Abidia (A–Z)"),
    ALPHA_ZA("Reverse Alphabetical (Z–A)", "Abidia mivadika (Z–A)"),
    SIZE_ASC("Smallest size first", "Habe kely indrindra"),
    SIZE_DESC("Largest size first", "Habe lehibe indrindra"),
    INSTALL_NEWEST("Newest installed first", "Vaovao indrindra"),
    INSTALL_OLDEST("Oldest installed first", "Tranainy indrindra"),
    RECENT_USED("Recently used", "Vao nampiasaina"),
    CUSTOM("Custom manual arrangement", "Filaharana manokana")
}

enum class IconShape(val displayName: String) {
    SQUIRCLE("Squircle"),
    ROUNDED("Boribory efamira"),
    CIRCLE("Boribory"),
    DEFAULT("Tsotra")
}

enum class PageTransitionStyle(val displayName: String) {
    PAGE_CURL("Fihodinan'ny pejin'ny Baiboly 3D (3D Bible Curl)"),
    SLIDE("Fikorisan'ny pejy (Smooth Slide)"),
    FADE("Fiovan'ny hazavana (Soft Fade)")
}

enum class AnimationSpeed(val displayName: String, val durationMs: Int) {
    SLOW("Milamina sy Masina (Sacred / 700ms)", 700),
    NORMAL("Mahazatra (Normal / 450ms)", 450),
    FAST("Haingana (Fast / 250ms)", 250)
}

data class LauncherPreferences(
    val iconSizeDp: Int = 56,
    val columns: Int = 4,
    val rows: Int = 5,
    val showAppLabels: Boolean = true,
    val labelTextSizeSp: Int = 12,
    val showPageNumbers: Boolean = true,
    val showVerseHeader: Boolean = true,
    val themeModeName: String = "MIDNIGHT_BIBLE",
    val animationSpeed: AnimationSpeed = AnimationSpeed.NORMAL,
    val pageTransitionStyle: PageTransitionStyle = PageTransitionStyle.PAGE_CURL,
    val iconShape: IconShape = IconShape.SQUIRCLE,
    val sortOrder: SortOrder = SortOrder.ALPHA_AZ,
    val isFirstLaunchCompleted: Boolean = false,
    val customOrder: List<String> = emptyList()
)
