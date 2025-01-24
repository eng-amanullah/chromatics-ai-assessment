package com.amanullah.chromaticsaiassessment.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amanullah.chromaticsaiassessment.base.utils.getInitials

@Composable
fun NameAvatar(
    modifier: Modifier = Modifier,
    name: String,
    backgroundColor: Color = MaterialTheme.colorScheme.onBackground,
    textColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val initials = name.getInitials()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size = 48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        Text(
            modifier = Modifier
                .padding(all = 8.dp),
            text = initials,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun Preview() {
    NameAvatar(name = "Amanullah")
}