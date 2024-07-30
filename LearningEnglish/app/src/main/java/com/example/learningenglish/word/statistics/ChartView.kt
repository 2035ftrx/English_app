package com.example.learningenglish.word.statistics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    onUpdate: (BarChart) -> Unit
) {
    val axisLineColor = MaterialTheme.colorScheme.secondary.value.toInt()
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            val chart = BarChart(context)  // Initialise the chart
            // 设置 Y 轴的标签
            chart.axisLeft.axisLineColor = axisLineColor
            chart.axisRight.isEnabled = false

            chart.setScaleEnabled(false);
            chart.setClickable(false);
            chart.setHighlightPerDragEnabled(false);
            chart.setHighlightPerTapEnabled(false);

            // 获取 X 轴对象
            val xAxis = chart.xAxis
            // 设置 X 轴的标签
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
                    val tempTime = value.toLong() + 1719038400000
                    Timber.d(" $value $tempTime  ${dateFormat.format(tempTime)}")
                    return dateFormat.format(tempTime)
                }
            }
            // 设置 X 轴的位置
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.axisLineColor = axisLineColor
//            xAxis.labelRotationAngle = -45f
//            xAxis.setDrawLabels(false)
            //设置X轴标签居中显示，不然标签与柱状图不居中对应
            xAxis.setCenterAxisLabels(true);
            xAxis.setDrawGridLines(false);
            //隐藏x轴描述
            val description = Description()
            description.setEnabled(false)
            chart.setDescription(description)

            // 可选：自定义图表外观
            val legend: Legend = chart.getLegend()
//            legend.form = Legend.LegendForm.CIRCLE

            val customBarChartRender =
                CustomBarChartRender(chart, chart.animator, chart.viewPortHandler)
            customBarChartRender.setRadius(30)
            chart.renderer = customBarChartRender

            // Refresh and return the chart
            chart.invalidate()
            chart
        }, update = { chart ->
            onUpdate(chart)
        }
    )
}

// https://github.com/PhilJay/MPAndroidChart
