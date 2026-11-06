package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.GaramondFontFamily
import com.example.ui.theme.SacredGold

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 12.dp,
            shadowElevation = 18.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, SacredGold.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                .testTag("about_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Sacred Icon
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.5.dp, BrightGold, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.holy_bible_icon_1793995293787),
                        contentDescription = "Holy Home Icon",
                        modifier = Modifier.size(66.dp).clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "HOLY HOME",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 2.sp,
                    color = BrightGold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Version 1.0",
                    fontFamily = CinzelFontFamily,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    color = AntiqueGold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "A Christian Android launcher inspired by the Holy Bible.",
                    fontFamily = GaramondFontFamily,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = SacredGold.copy(alpha = 0.35f))
                Spacer(modifier = Modifier.height(14.dp))

                // Creator & Developer Information
                Text(
                    text = "Mpamorona sy Mpamorina (Creator & Developer):",
                    fontFamily = CinzelFontFamily,
                    fontSize = 11.sp,
                    color = SacredGold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.creator_name),
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    color = BrightGold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Spiritual dedication and verse
                Text(
                    text = "\"HOTAHINA IANAO, RAHA MIDITRA; ARY HOTAHINA IANAO, RAHA MIVOAKA.\"",
                    fontFamily = GaramondFontFamily,
                    fontStyle = FontStyle.Italic,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "DEOTERONOMIA 28:6",
                    fontFamily = CinzelFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = BrightGold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "© 2026 Solohery RAKOTOARISOA. Zo rehetra voatokana.",
                    fontFamily = GaramondFontFamily,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                ElevatedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = SacredGold,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(0.7f).height(44.dp).testTag("about_dismiss_button")
                ) {
                    Text(
                        text = "Amena",
                        fontFamily = CinzelFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
