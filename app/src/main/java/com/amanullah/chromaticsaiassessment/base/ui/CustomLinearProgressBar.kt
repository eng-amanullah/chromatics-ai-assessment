package com.amanullah.chromaticsaiassessment.base.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomLinearProgressBar(state: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    ) {
        when (state) {
            true -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            else -> {
                Spacer(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}