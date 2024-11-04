package com.example.test.features.mp_horizontal_bar_chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test.R
import com.example.test.databinding.FragmentMPHorizontalBarChartBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlin.math.max
import kotlin.math.min

class MPHorizontalBarChartFragment : Fragment() {
    private var _binding: FragmentMPHorizontalBarChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMPHorizontalBarChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHorizontalbarchart()
        displayHorizontalBarChart()
    }

    private fun initHorizontalbarchart() {
        with(binding) {
            horizontalBarChart.setDoubleTapToZoomEnabled(false)

            //mHorChart.setOnChartValueSelectedListener(this);//Hien comment
            // mHorChart.setHighlightEnabled(false);
            horizontalBarChart.setDrawBarShadow(false)

            horizontalBarChart.setDrawValueAboveBar(true)

            horizontalBarChart.getDescription().setEnabled(false)

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            horizontalBarChart.setMaxVisibleValueCount(60)

            // scaling can now only be done on x- and y-axis separately
            horizontalBarChart.setPinchZoom(false)

            // draw shadows for each bar that show the maximum value
            // mHorChart.setDrawBarShadow(true);
            horizontalBarChart.setDrawGridBackground(false)

            val xl: XAxis = horizontalBarChart.getXAxis()
            xl.position = XAxis.XAxisPosition.BOTTOM
            //xl.setTypeface(mTfLight);//Hien comment
            xl.setDrawAxisLine(true)
            xl.setDrawGridLines(false)
            xl.granularity = 10f

            xl.axisLineColor = requireActivity().getColor(R.color.black)
            xl.gridColor = requireActivity().getColor(R.color.black)
            xl.textColor = requireActivity().getColor(R.color.black)

            val yl: YAxis = horizontalBarChart.getAxisLeft()
            //yl.setTypeface(mTfLight);//Hien comment
            yl.setDrawAxisLine(true)
            yl.setDrawGridLines(false)
            yl.axisMinimum = 0f // this replaces setStartAtZero(true)
            //        yl.setInverted(true);
            yl.setDrawLabels(false)

            yl.axisLineColor = requireActivity().getColor(R.color.black)
            yl.gridColor = requireActivity().getColor(R.color.black)
            yl.textColor = requireActivity().getColor(R.color.black)

            //        YAxis yr = mHorChart.getAxisRight();
//        //yr.setTypeface(mTfLight);//Hien comment
//        yr.setDrawAxisLine(true);
//        yr.setDrawLabels(false);
//        yr.setDrawGridLines(false);
//        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
////        yr.setInverted(true);
//        yr.setAxisLineColor(MTSHelper.getColor(R.color.black));
//        yr.setGridColor(MTSHelper.getColor(R.color.white));
//        yr.setTextColor(MTSHelper.getColor(R.color.white));
            displayHorizontalBarChart()
            horizontalBarChart.setFitBars(true)
            horizontalBarChart.animateY(1500)

            val l: Legend = horizontalBarChart.getLegend()
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            /*l.setFormSize(8f);
            l.setXEntrySpace(4f);*/
            l.formSize = 0f
            l.xEntrySpace = 0f

            //setData();
        }
    }

    private fun displayHorizontalBarChart() {
        with(binding) {
            val set1: BarDataSet
            val yVals1 = ArrayList<BarEntry>()
//            yVals1.apply {
//                add(BarEntry(10000f, 10000f))
//                add(BarEntry(50000f, 100000f))
//                add(BarEntry(80000f, 50000f))
//                add(BarEntry(100000f, 90000f))
//                add(BarEntry(30000f, 1000f))
//            }

//            yVals1.apply {
//                add(BarEntry(10f, 10000f))
//                add(BarEntry(50f, 100000f))
//                add(BarEntry(100f, 50000f))
//                add(BarEntry(1f, 90000f))
//                add(BarEntry(200f, 1000f))
//            }

//            yVals1.apply {
//                add(BarEntry(110f * 1000, 10000f))
//                add(BarEntry(120f * 1000, 100000f))
//                add(BarEntry(100f * 1000, 50000f))
//            }

            yVals1.apply {
                add(BarEntry(0.01f * 1000, 10000f))
                add(BarEntry(0.02f * 1000, 100000f))
                add(BarEntry(0.03f * 1000, 50000f))
                add(BarEntry(0.09f * 1000, 90000f))
                add(BarEntry(0.1f * 1000, 1000f))
            }

//            yVals1.apply {
//                add(BarEntry(1.3f * 1000, 10000f))
//                add(BarEntry(1.5f * 1000, 100000f))
//                add(BarEntry(1.4f * 1000, 50000f))
//                add(BarEntry(1.23f * 1000, 90000f))
//                add(BarEntry(1.6f * 1000, 1000f))
//            }

//            yVals1.apply {
//                add(BarEntry(11.3f * 1000, 10000f))
//                add(BarEntry(12.5f * 1000, 100000f))
//                add(BarEntry(11.4f * 1000, 50000f))
//                add(BarEntry(12.23f * 1000, 90000f))
//                add(BarEntry(13.6f * 1000, 1000f))
//            }

//            yVals1.apply {
//                add(BarEntry(140.3f * 1000, 10000f))
//                add(BarEntry(142.5f * 1000, 100000f))
//                add(BarEntry(143.4f * 1000, 50000f))
//                add(BarEntry(144.23f * 1000, 90000f))
//                add(BarEntry(145.6f * 1000, 1000f))
//            }

//            yVals1.apply {
//                add(BarEntry(1000.3f * 1000, 10000f))
//                add(BarEntry(1200.5f * 1000, 100000f))
//                add(BarEntry(1300.4f * 1000, 50000f))
//                add(BarEntry(1400.23f * 1000, 90000f))
//                add(BarEntry(1500.6f * 1000, 1000f))
//            }

//            yVals1.apply {
//                add(BarEntry(0.51f * 1000, 10000f))
//                add(BarEntry(0.5f * 1000, 100000f))
//                add(BarEntry(0.33f * 1000, 50000f))
//                add(BarEntry(0.39f * 1000, 90000f))
//                add(BarEntry(0.52f * 1000, 1000f))
//            }

//            yVals1.apply {
//                add(BarEntry(5.31f * 1000, 10000f))
//                add(BarEntry(5.5f * 1000, 100000f))
//                add(BarEntry(5.3f * 1000, 50000f))
//                add(BarEntry(5.28f * 1000, 90000f))
//                add(BarEntry(5.22f * 1000, 1000f))
//            }



//            yVals1.apply {
//                add(BarEntry(0.1f * 1000, 10000f))
//                add(BarEntry(0.2f * 1000, 100000f))
//                add(BarEntry(0.3f * 1000, 50000f))
//                add(BarEntry(0.4f * 1000, 90000f))
//                add(BarEntry(0.8f * 1000, 1000f))
//            }
            val minXEntry = yVals1.minByOrNull { it.x }
            val maxXEntry = yVals1.maxByOrNull { it.x }

            val minX = minXEntry?.x
            val maxX = maxXEntry?.x
            val xl: XAxis = horizontalBarChart.getXAxis()

//            if ((minX ?: 0f) < 100f) {
//                xl.axisMinimum = minX ?: 0f
//                xl.axisMaximum = maxX ?: 0f
//            }

            xl.granularity = minX ?: 10f
            set1 = BarDataSet(yVals1, "DataSet 1")
            set1.valueTextColor = requireActivity().getColor(R.color.black)
            //set1.setColor(MTSHelper.getColor(R.color.red));
            set1.setColors(R.color.color_6DE4BA)

            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            //data.setValueTypeface(mTfLight);//Hien comment
            if ((minX ?: 0f) <= 1000f) { // 0.01 -> 1
                data.barWidth = 2.5f
            } else if ((minX ?: 0f) <= 10000f) {
                data.barWidth = 5f
            } else {
                data.barWidth = 40f
            }

            horizontalBarChart.setData(data)


            //mHorChart.setFitBars(true);
            horizontalBarChart.invalidate()

//        val xl: XAxis = binding.horizontalBarChart.getXAxis()
//        xl.position = XAxis.XAxisPosition.BOTTOM
//
//        //xl.setTypeface(mTfLight);//Hien comment
//        xl.setDrawAxisLine(true)
//        xl.setDrawGridLines(false)
//        xl.granularity = 10f
//
//
//        xl.axisLineColor = requireActivity().getColor(R.color.white)
//        xl.gridColor = requireActivity().getColor(R.color.white)
//        xl.textColor = requireActivity().getColor(R.color.white)
//
//        val yl: YAxis =  binding.horizontalBarChart.getAxisLeft()
//
//        //yl.setTypeface(mTfLight);//Hien comment
//        yl.setDrawAxisLine(true)
//        yl.setDrawGridLines(false)
//        yl.axisMinimum = 0f // this replaces setStartAtZero(true)
//
//        //        yl.setInverted(true);
//        yl.setDrawLabels(false)
//
//        yl.axisLineColor = requireActivity().getColor(R.color.black)
//        yl.gridColor = requireActivity().getColor(R.color.white)
//        yl.textColor = requireActivity().getColor(R.color.black)
//        val xl: XAxis = binding.horizontalBarChart.xAxis
//        xl.position = XAxis.XAxisPosition.BOTTOM
//        xl.setDrawAxisLine(true)
//        xl.setDrawGridLines(false)
////        xl.granularity = 0.01f  // Khoảng cách tối thiểu giữa các giá trị
////        xl.axisMinimum = 0.01f  // Giá trị nhỏ nhất của trục X
////        xl.axisMaximum = 0.1f   // Giá trị lớn nhất của trục X
//
//        val yl: YAxis = binding.horizontalBarChart.axisLeft
//        yl.setDrawAxisLine(true)
//        yl.setDrawGridLines(false)
//        yl.axisMinimum = 0f
//
//        val yVals1 = ArrayList<BarEntry>()
////        yVals1.apply {
////            add(BarEntry(10f, 10000f))
////            add(BarEntry(50f, 100000f))
////            add(BarEntry(100f, 50000f))
////            add(BarEntry(1f, 90000f))
////            add(BarEntry(200f, 1000f))
////        }
//            yVals1.apply {
//                add(BarEntry(0.01f, 10000f))
//                add(BarEntry(0.02f, 100000f))
//                add(BarEntry(0.03f, 50000f))
//                add(BarEntry(0.09f, 90000f))
//                add(BarEntry(0.1f, 1000f))
//            }
//        val set1 = BarDataSet(yVals1, "DataSet 1")
//        set1.valueTextColor = requireActivity().getColor(R.color.black)
//        set1.setColor(R.color.color_EB9ABE)
//        val dataSets = arrayListOf<IBarDataSet>()
//        dataSets.add(set1)
//        val mData = BarData(dataSets)
//        mData.barWidth = 0.1f
//        binding.horizontalBarChart.apply {
//            setFitBars(true)
//            animateY(1500)
//            setDrawBarShadow(false)
//            setDrawValueAboveBar(true)
//            description.isEnabled = false
//            setMaxVisibleValueCount(50)
//            setPinchZoom(false)
//            data = mData
//            invalidate()
//        }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}