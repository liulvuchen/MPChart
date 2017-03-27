
package com.xxmassdeveloper.mpchartexample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.xxmassdeveloper.mpchartexample.custom.RadarMarkerView;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.util.ArrayList;

public class RadarChartActivitry extends DemoBase {

    private RadarChart mChart;
    private float[] shu = new float[]{
            90f, 70f, 88.5f, 90.5f, 75.8f, 88.2f, 40f
    };
    private float[] shu1 = new float[]{
            23f, 64f, 34f, 88.4f, 76.1f, 75.5f, 95f
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_radarchart_noseekbar);

        //YOUR PREFERENCES的字体设置
        TextView tv = (TextView) findViewById(R.id.textView);
        //自定义的字体（具体实现跳入mTfLight）
        tv.setTypeface(mTfLight);
        //设置字体的颜色和背景
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.rgb(60, 65, 82));

        mChart = (RadarChart) findViewById(R.id.chart1);
        //图表控件的背景色
        mChart.setBackgroundColor(Color.rgb(60, 65, 82));
        //设置字体不可点击
        mChart.getDescription().setEnabled(false);

        //Y轴的线的宽度和颜色
        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.LTGRAY);
        //x轴的线的宽度和颜色
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.LTGRAY);
        //线的透明度
        mChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //设置一个（70%）的背景图
        MarkerView mv = new RadarMarkerView(this, R.layout.radar_markerview);
        //应用到图表中
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        setData();

        //首次进入的动画
        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);
        //设置图表中的文字
        XAxis xAxis = mChart.getXAxis();
        //图表中文字的类型
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(9f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        //设置图表中文字说明，无限循环
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"战斗力", "经济", "推进", "反补", "参团", "正补", "发展"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(9, true);
        yAxis.setTextSize(18f);
        yAxis.setAxisMinimum(0f);
        float a1 = maximum(shu, 7);
        float a2 = maximum(shu1, 7);
        if (a1 > a2) {
            yAxis.setAxisMaximum(a1);
        } else {
            yAxis.setAxisMaximum(a2);
        }
        yAxis.setDrawLabels(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTypeface(mTfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.radar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //用来显示图标在线上的值（72.0等）
            case R.id.actionToggleValues: {
                for (IDataSet<?> set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            //用来显示点选位置的显示（47%）
            case R.id.actionToggleHighlight: {
                if (mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
                break;
            }
            //是否开启转动
            case R.id.actionToggleRotate: {
                if (mChart.isRotationEnabled())
                    mChart.setRotationEnabled(false);
                else
                    mChart.setRotationEnabled(true);
                mChart.invalidate();
                break;
            }
            //是否开启图层
            case R.id.actionToggleFilled: {

                ArrayList<IRadarDataSet> sets = (ArrayList<IRadarDataSet>) mChart.getData()
                        .getDataSets();

                for (IRadarDataSet set : sets) {
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                mChart.invalidate();
                break;
            }
            //显示的圆点与actionToggleHighlight配套使用
            case R.id.actionToggleHighlightCircle: {

                ArrayList<IRadarDataSet> sets = (ArrayList<IRadarDataSet>) mChart.getData()
                        .getDataSets();

                for (IRadarDataSet set : sets) {
                    set.setDrawHighlightCircleEnabled(!set.isDrawHighlightCircleEnabled());
                }
                mChart.invalidate();
                break;
            }

            case R.id.actionSave: {
                if (mChart.saveToPath("title" + System.currentTimeMillis(), "")) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
                break;
            }

            //设置图层线的个数
            case R.id.actionToggleXLabels: {
                mChart.getXAxis().setEnabled(!mChart.getXAxis().isEnabled());
                mChart.notifyDataSetChanged();
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleYLabels: {

                mChart.getYAxis().setEnabled(!mChart.getYAxis().isEnabled());
                mChart.invalidate();
                break;
            }
            //动画效果
            case R.id.animateX: {
                mChart.animateX(1400);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(1400);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(1400, 1400);
                break;
            }
            //首次进入是否有动画效果
            case R.id.actionToggleSpin: {
                mChart.spin(2000, mChart.getRotationAngle(), mChart.getRotationAngle() + 360, Easing.EasingOption
                        .EaseInCubic);
                break;
            }
        }
        return true;
    }

    public void setData() {

//        float mult = 90;//随机数所要乘的值
//        float min = 0;//每一个初始点的最小值
        int cnt = 7;//Y轴的个数

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
//            float val1 = (float) (Math.random() * mult) + min;
            float val1 = shu[i];
            entries1.add(new RadarEntry(val1));

//            float val2 = (float) (Math.random() * mult) + min;
            float val2 = shu1[i];
            entries2.add(new RadarEntry(val2));
        }

        //设置字体描述和对应的图形
        RadarDataSet set1 = new RadarDataSet(entries1, "Last Week");
        //设置小方块的颜色
        set1.setColor(Color.rgb(255, 155, 72));
        //设置图形中边缘线的颜色
        set1.setFillColor(Color.rgb(255, 155, 72));
        //设置图层背景是否显示
        set1.setDrawFilled(true);
        //设置图形的透明度
        set1.setFillAlpha(180);
        //设置图形中线的宽度
        set1.setLineWidth(2f);
        //设置圆点是否显示
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "This Week");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        //设置图形中数字的字体
        RadarData data = new RadarData(sets);
        data.setValueTypeface(mTfLight);
        data.setValueTextSize(16f);
        //首次是否显示
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        //重绘方法
        mChart.invalidate();
    }

    public float maximum(float[] arr, int size) {
        int i;
        float max = arr[0];
        for (i = 1; i < size; i++) {
            if (max < arr[i]) max = arr[i];
        }
        return max;
    }
}
