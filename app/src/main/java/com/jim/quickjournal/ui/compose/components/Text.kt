package com.jim.quickjournal.ui.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Subtitle(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SubtitleSmall(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        color = color
    )
}


@Composable
fun Headline(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium,
        color = color
    )
}


@Composable
fun BodyText(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        modifier = modifier
    )
}

