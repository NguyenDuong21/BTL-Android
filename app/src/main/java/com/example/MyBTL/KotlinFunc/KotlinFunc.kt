package com.example.MyBTL.KotlinFunc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.MyBTL.Chart.LineChart
import com.example.MyBTL.Chart.QuadLineChart
import com.metehanbolat.linechartcompose.ui.theme.LineChartComposeTheme

class KotlinFunc {
    fun setContentComposeView(composeView: ComposeView
    ){
        composeView.setContent {
            LineChartComposeTheme {
                val data = listOf(
                    Pair(6, 111.45),
                    Pair(7, 111.0),
                    Pair(8, 113.45),
                    Pair(9, 112.25),
                    Pair(10, 116.45),
                    Pair(11, 113.35),
                    Pair(12, 118.65),
                    Pair(13, 110.15),
                    Pair(14, 113.05),
                    Pair(15, 114.25),
                    Pair(16, 116.35),
                    Pair(17, 117.45),
                    Pair(18, 112.65),
                    Pair(19, 115.45),
                    Pair(20, 111.85)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    LineChart(
                        data = data,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}