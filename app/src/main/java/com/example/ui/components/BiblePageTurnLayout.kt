package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnimationSpeed
import com.example.data.model.PageTransitionStyle
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.GaramondFontFamily
import com.example.ui.theme.SacredGold
import com.example.ui.theme.ScriptureCrimson
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun BiblePageTurnLayout(
    currentPage: Int,
    totalPages: Int,
    showVerseHeader: Boolean,
    showPageNumbers: Boolean,
    pageTransitionStyle: PageTransitionStyle,
    animationSpeed: AnimationSpeed,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onCloseToCover: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (page: Int) -> Unit
) {
    val density = LocalDensity.current.density
    val coroutineScope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    var isDragging by remember { mutableFloatStateOf(0f) }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val animDuration = animationSpeed.durationMs

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(currentPage, totalPages) {
                detectHorizontalDragGestures(
                    onDragStart = { isDragging = 1f },
                    onDragEnd = {
                        isDragging = 0f
                        val currentOffset = dragOffset.value
                        coroutineScope.launch {
                            if (currentOffset < -140f) {
                                // Dragged left -> Next page
                                if (currentPage < totalPages - 1) {
                                    onNextPage()
                                }
                            } else if (currentOffset > 140f) {
                                // Dragged right -> Previous page or Cover
                                if (currentPage > 0) {
                                    onPreviousPage()
                                } else {
                                    onCloseToCover()
                                }
                            }
                            dragOffset.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = animDuration / 2, easing = FastOutSlowInEasing)
                            )
                        }
                    },
                    onDragCancel = {
                        isDragging = 0f
                        coroutineScope.launch {
                            dragOffset.animateTo(0f)
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            val newTarget = (dragOffset.value + dragAmount).coerceIn(-400f, 400f)
                            dragOffset.snapTo(newTarget)
                        }
                    }
                )
            }
            .testTag("bible_page_layout")
    ) {
        // Red silk bookmark ribbon peeking from the top header
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 56.dp, top = statusBarPadding)
                .width(18.dp)
                .height(48.dp)
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(ScriptureCrimson, Color(0xFF7A1515))
                    )
                )
                .border(0.5.dp, BrightGold.copy(alpha = 0.5f), RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
        )

        // Bible page paper container with realistic 3D curl / slide
        val rotationAngle = when (pageTransitionStyle) {
            PageTransitionStyle.PAGE_CURL -> {
                (dragOffset.value / 400f) * 22f
            }
            else -> 0f
        }

        val translationX = when (pageTransitionStyle) {
            PageTransitionStyle.SLIDE -> dragOffset.value
            PageTransitionStyle.PAGE_CURL -> dragOffset.value * 0.4f
            PageTransitionStyle.FADE -> 0f
        }

        val pageAlpha = when (pageTransitionStyle) {
            PageTransitionStyle.FADE -> (1f - (abs(dragOffset.value) / 400f) * 0.6f).coerceIn(0.4f, 1f)
            else -> 1f
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.cameraDistance = 16f * density
                    this.rotationY = rotationAngle
                    this.translationX = translationX
                    this.alpha = pageAlpha
                    this.transformOrigin = if (dragOffset.value < 0) {
                        TransformOrigin(0f, 0.5f) // Left spine pivot
                    } else {
                        TransformOrigin(1f, 0.5f) // Right spine pivot
                    }
                }
                .drawBehind {
                    // Left gutter / spine shadow (deep book binding shadow)
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.40f),
                                Color.Black.copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = 40.dp.toPx()
                        )
                    )

                    // Right gilded page edge (shining gold foil leaf)
                    drawLine(
                        brush = Brush.verticalGradient(
                            listOf(AntiqueGold, BrightGold, SacredGold, AntiqueGold)
                        ),
                        start = Offset(size.width - 2.dp.toPx(), 0f),
                        end = Offset(size.width - 2.dp.toPx(), size.height),
                        strokeWidth = 3.dp.toPx()
                    )

                    // Dynamic curl shadow when dragged
                    if (abs(dragOffset.value) > 10f) {
                        val shadowAlpha = (abs(dragOffset.value) / 400f * 0.35f).coerceIn(0f, 0.35f)
                        drawRect(color = Color.Black.copy(alpha = shadowAlpha))
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = statusBarPadding, bottom = navBarPadding)
            ) {
                // Sacred Scripture Page Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back to Cover / Quick Actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onCloseToCover,
                            modifier = Modifier.size(40.dp).testTag("close_to_cover_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = "Hikatona ny Baiboly",
                                tint = SacredGold
                            )
                        }

                        IconButton(
                            onClick = onOpenSearch,
                            modifier = Modifier.size(40.dp).testTag("page_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Hikaroka",
                                tint = SacredGold
                            )
                        }
                    }

                    // Scripture Verse reference / Header
                    if (showVerseHeader) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = "† DEOTERONOMIA 28:6 †",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = SacredGold
                            )
                            Text(
                                text = "Hotahina ianao, raha miditra...",
                                fontFamily = GaramondFontFamily,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                maxLines = 1
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // Settings Button
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.size(40.dp).testTag("page_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Fandrindrana",
                            tint = SacredGold
                        )
                    }
                }

                // Decorative golden divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    SacredGold.copy(alpha = 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Main Page Content (The Grid of Applications)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    content(currentPage)
                }

                // Page Footer (Bible Page numbering and navigation)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Page Button
                    IconButton(
                        onClick = {
                            if (currentPage > 0) onPreviousPage() else onCloseToCover()
                        },
                        modifier = Modifier.size(40.dp).testTag("previous_page_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (currentPage == 0) "Fonony" else "Pejy teo aloha",
                            tint = SacredGold
                        )
                    }

                    // Page Number Indicator: e.g. "Pejy 1 amin'ny 3"
                    if (showPageNumbers) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "— Pejy ${currentPage + 1} / $totalPages —",
                                fontFamily = CinzelFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp,
                                color = SacredGold
                            )

                            // Mini page indicator dots
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (i in 0 until totalPages.coerceAtMost(8)) {
                                    Box(
                                        modifier = Modifier
                                            .size(if (i == currentPage) 6.dp else 4.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (i == currentPage) BrightGold else SacredGold.copy(alpha = 0.35f)
                                            )
                                    )
                                }
                            }
                        }
                    }

                    // Next Page Button
                    IconButton(
                        onClick = onNextPage,
                        enabled = currentPage < totalPages - 1,
                        modifier = Modifier.size(40.dp).testTag("next_page_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Pejy manaraka",
                            tint = if (currentPage < totalPages - 1) SacredGold else SacredGold.copy(alpha = 0.25f)
                        )
                    }
                }
            }
        }
    }
}
