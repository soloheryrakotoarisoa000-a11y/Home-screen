package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.GaramondFontFamily
import com.example.ui.theme.MidnightDark
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.SacredGold
import com.example.ui.theme.ScriptureCrimson

@Composable
fun BibleCoverScreen(
    onOpenBible: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    // Swiping left to turn page / open Bible
                    if (dragAmount < -25f) {
                        onOpenBible()
                    }
                }
            }
            .testTag("bible_cover_screen")
    ) {
        // Background leather texture
        Image(
            painter = painterResource(id = R.drawable.bible_cover_bg_1793995307964),
            contentDescription = "Bible Cover Leather",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark rich tint overlay to guarantee sacred contrast and legibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MidnightDark.copy(alpha = 0.78f),
                            MidnightNavy.copy(alpha = 0.65f),
                            MidnightDark.copy(alpha = 0.88f)
                        )
                    )
                )
        )

        // Sacred Gold Book Spine Ribbon & Gilded Trim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    // Left spine stitch & embossed groove
                    drawLine(
                        color = AntiqueGold.copy(alpha = 0.6f),
                        start = Offset(24f, 0f),
                        end = Offset(24f, size.height),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = BrightGold.copy(alpha = 0.4f),
                        start = Offset(30f, 0f),
                        end = Offset(30f, size.height),
                        strokeWidth = 1f
                    )

                    // Right gilded page edge highlight
                    drawLine(
                        color = SacredGold.copy(alpha = 0.8f),
                        start = Offset(size.width - 6f, 0f),
                        end = Offset(size.width - 6f, size.height),
                        strokeWidth = 6f
                    )
                }
        )

        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarPadding + 16.dp, bottom = navBarPadding + 16.dp)
                .padding(horizontal = 24.dp)
        ) {
            // Top Toolbar: Quick Search, Settings, About
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onOpenSearch,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MidnightNavy.copy(alpha = 0.7f))
                        .border(1.dp, SacredGold.copy(alpha = 0.4f), CircleShape)
                        .testTag("cover_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Hikaroka rindranasa",
                        tint = BrightGold
                    )
                }

                // Decorative miniature cross header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(18.dp)
                            .height(1.dp)
                            .background(AntiqueGold.copy(alpha = 0.6f))
                    )
                    Text(
                        text = " † ",
                        color = BrightGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .width(18.dp)
                            .height(1.dp)
                            .background(AntiqueGold.copy(alpha = 0.6f))
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onOpenAbout,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MidnightNavy.copy(alpha = 0.7f))
                            .border(1.dp, SacredGold.copy(alpha = 0.4f), CircleShape)
                            .testTag("cover_about_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Momba an'i Holy Home",
                            tint = BrightGold
                        )
                    }

                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MidnightNavy.copy(alpha = 0.7f))
                            .border(1.dp, SacredGold.copy(alpha = 0.4f), CircleShape)
                            .testTag("cover_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Fandrindrana",
                            tint = BrightGold
                        )
                    }
                }
            }

            // Central Sacred Bible Presentation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                // Sacred Christian Cross Graphic with Radiant Aura
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(110.dp)
                        .scale(pulseScale)
                ) {
                    // Soft Holy Radiance Glow
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .alpha(glowAlpha)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(BrightGold.copy(alpha = 0.45f), Color.Transparent)
                                )
                            )
                    )

                    // Minimalist Elegant Christian Cross
                    ChristianCrossCanvas(
                        modifier = Modifier.size(76.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title: HOLY HOME
                Text(
                    text = "HOLY HOME",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = 4.sp,
                    color = BrightGold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.shadow(8.dp, spotColor = SacredGold)
                )

                Text(
                    text = "NY BAIBOLY MASINA AMIN'NY FINDAINAO",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = AntiqueGold.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // The Prominent Malagasy Scripture from Deuteronomy 28:6
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MidnightNavy.copy(alpha = 0.85f),
                    tonalElevation = 8.dp,
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.5.dp,
                            brush = Brush.horizontalGradient(
                                listOf(
                                    AntiqueGold.copy(alpha = 0.4f),
                                    BrightGold.copy(alpha = 0.9f),
                                    AntiqueGold.copy(alpha = 0.4f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 18.dp)
                    ) {
                        // Scripture Crimson Bookmark Ribbon accent
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(ScriptureCrimson)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = stringResource(id = R.string.verse_malagasy),
                            fontFamily = GaramondFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            fontSize = 19.sp,
                            lineHeight = 28.sp,
                            color = Color(0xFFF9F6F0),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = stringResource(id = R.string.verse_reference),
                            fontFamily = CinzelFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 2.sp,
                            color = BrightGold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Bottom Open Action: "Sokafy ny Tenin'Andriamanitra"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                ElevatedButton(
                    onClick = onOpenBible,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = SacredGold,
                        contentColor = MidnightDark
                    ),
                    shape = RoundedCornerShape(28.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(54.dp)
                        .border(1.dp, BrightGold, RoundedCornerShape(28.dp))
                        .testTag("open_bible_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.open_bible_instruction),
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Afindrao miankavia na tsindrio raha hanokatra ◄",
                    fontFamily = GaramondFontFamily,
                    fontSize = 12.sp,
                    color = AntiqueGold.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
fun ChristianCrossCanvas(
    modifier: Modifier = Modifier,
    crossColor: Color = BrightGold
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val strokeWidth = w * 0.13f
        val capRadius = strokeWidth / 2f

        // Vertical beam
        drawLine(
            color = crossColor,
            start = Offset(w / 2f, h * 0.08f),
            end = Offset(w / 2f, h * 0.92f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Horizontal beam
        drawLine(
            color = crossColor,
            start = Offset(w * 0.18f, h * 0.35f),
            end = Offset(w * 0.82f, h * 0.35f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Embossed core inner gold line
        drawLine(
            color = Color.White.copy(alpha = 0.6f),
            start = Offset(w / 2f, h * 0.12f),
            end = Offset(w / 2f, h * 0.88f),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.White.copy(alpha = 0.6f),
            start = Offset(w * 0.22f, h * 0.35f),
            end = Offset(w * 0.78f, h * 0.35f),
            strokeWidth = 2f
        )
    }
}
