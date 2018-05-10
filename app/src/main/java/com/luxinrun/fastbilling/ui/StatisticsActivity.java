package com.luxinrun.fastbilling.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.assistent.Constant;
import com.luxinrun.fastbilling.assistent.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    private int CURRENT_YEAR = calendar.get(Calendar.YEAR);
    private int CURRENT_MONTH = calendar.get(Calendar.MONTH);

    private PieChart mChart;
    private DBHelper dbHelper;
    private ArrayList<Map<String, Object>> data;
    private String[] get_classify_num_data;
    private String[] get_classify_title_data;
    private String[] get_classify_color_data;
    private String[] get_money_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        mChart = (PieChart) findViewById(R.id.mPieChart);
        mChart.setUsePercentValues(true);
        mChart.setBackgroundResource(R.color.colorWhite);
        mChart.setExtraOffsets(5, 10, 60, 10);        //设置pieChart图表上下左右的偏移，类似于外边距
        mChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]
        mChart.setRotationAngle(0);                   //设置pieChart图表起始角度
        mChart.setRotationEnabled(true);              //设置pieChart图表是否可以手动旋转
        mChart.setHighlightPerTapEnabled(true);       //设置piecahrt图表点击Item高亮是否可用
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);// 设置pieChart图表展示动画效果
        // 设置 pieChart 图表Item文本属性
        mChart.setDrawEntryLabels(true);              //设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
        mChart.setEntryLabelColor(Color.WHITE);       //设置pieChart图表文本字体颜色
        mChart.setEntryLabelTextSize(10f);            //设置pieChart图表文本字体大小

        // 设置 pieChart 内部圆环属性
        mChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        mChart.setHoleRadius(28f);                    //设置PieChart内部圆的半径(这里设置28.0f)
        mChart.setTransparentCircleRadius(31f);       //设置PieChart内部透明圆的半径(这里设置31.0f)
        mChart.setTransparentCircleColor(Color.BLACK);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        mChart.setTransparentCircleAlpha(50);         //设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明
        mChart.setHoleColor(Color.WHITE);             //设置PieChart内部圆的颜色
        mChart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）
        mChart.setCenterText("Test");                 //设置PieChart内部圆文字的内容
        mChart.setCenterTextSize(10f);                //设置PieChart内部圆文字的大小
        mChart.setCenterTextColor(Color.RED);         //设置PieChart内部圆文字的颜色

        dbHelper = new DBHelper(this);
        data = dbHelper.cursorMonth(CURRENT_YEAR + Constant.num_Format(CURRENT_MONTH + 1), "0");
        List<String> list = Constant.getPieChartData(data);
        get_classify_num_data = new String[list.size()];
        get_classify_title_data = new String[list.size()];
        get_classify_color_data = new String[list.size()];
        get_money_data = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String[] split = list.get(i).split("=");
            get_classify_num_data[i] = split[0];
            get_money_data[i] = split[2];
        }
        ArrayList<PieEntry> title = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();


    }
}
