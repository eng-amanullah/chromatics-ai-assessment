package com.amanullah.chromaticsaiassessment.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.amanullah.chromaticsaiassessment.base.navgraph.AppNavGraph
import com.amanullah.chromaticsaiassessment.base.theme.ChromaticsAIAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ChromaticsAIAssessmentTheme {
                AppNavGraph(incomingMobileNumber = intent.getStringExtra("number"))
            }
        }
    }
}