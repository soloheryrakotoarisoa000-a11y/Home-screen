package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.GaramondFontFamily
import com.example.ui.theme.MidnightDark
import com.example.ui.theme.SacredGold

@Composable
fun OnboardingDialog(
    onSetAsDefaultLauncher: () -> Unit,
    onComplete: () -> Unit
) {
    Dialog(onDismissRequest = onComplete) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 14.dp,
            shadowElevation = 20.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, SacredGold.copy(alpha = 0.7f), RoundedCornerShape(24.dp))
                .testTag("onboarding_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Header cross
                Text(
                    text = "†",
                    color = BrightGold,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "TONGASOA ETO AMIN'NY",
                    fontFamily = CinzelFontFamily,
                    fontSize = 12.sp,
                    letterSpacing = 2.sp,
                    color = AntiqueGold
                )

                Text(
                    text = "HOLY HOME",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    letterSpacing = 3.sp,
                    color = BrightGold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "\"HOTAHINA IANAO, RAHA MIDITRA; ARY HOTAHINA IANAO, RAHA MIVOAKA.\"",
                    fontFamily = GaramondFontFamily,
                    fontStyle = FontStyle.Italic,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "DEOTERONOMIA 28:6",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = SacredGold
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = SacredGold.copy(alpha = 0.35f))
                Spacer(modifier = Modifier.height(14.dp))

                // Feature Highlights
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FeatureGuideRow(
                        icon = Icons.Default.AutoStories,
                        title = "Ny findainao ho toy ny Baiboly",
                        description = "Sokafy ny fonony hahitana ny tenin'Andriamanitra sy ny rindranasa rehetra."
                    )
                    FeatureGuideRow(
                        icon = Icons.Default.Swipe,
                        title = "Avadiho ny pejy amin'ny rantsan-tanana",
                        description = "Afindrao miankavia na miankavanana toy ny mamadika ny pejin'ny Baiboly masina."
                    )
                    FeatureGuideRow(
                        icon = Icons.Default.Home,
                        title = "Ataovy Launcher Fototra (Default Home)",
                        description = "Isaky ny manindry ny bokotra 'Home' ianao dia miverina eto amin'ny Baiboly."
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions: Set as default launcher and continue
                ElevatedButton(
                    onClick = onSetAsDefaultLauncher,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = SacredGold,
                        contentColor = MidnightDark
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("onboarding_set_default_button")
                ) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ataovy Launcher Fototra Izao",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onComplete,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("onboarding_start_button")
                ) {
                    Text(
                        text = "Hanomboka Hampiasa",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = SacredGold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureGuideRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SacredGold.copy(alpha = 0.15f))
                .border(1.dp, SacredGold.copy(alpha = 0.4f), CircleShape)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = BrightGold, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BrightGold
            )
            Text(
                text = description,
                fontFamily = GaramondFontFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                lineHeight = 16.sp
            )
        }
    }
}
