package com.luxinrun.fastbilling.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
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
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);
        mChart.setBackgroundResource(R.color.colorWhite);
        mChart.setExtraOffsets(50, 10, 50, 10);       //设置pieChart图表上下左右的偏移，类似于外边距
        mChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]
        mChart.setRotationAngle(0);                   //设置pieChart图表起始角度
        mChart.setRotationEnabled(true);              //设置pieChart图表是否可以手动旋转
        mChart.setHighlightPerTapEnabled(true);       //设置piecahrt图表点击Item高亮是否可用
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);// 设置pieChart图表展示动画效果
        // 设置 pieChart 图表Item文本属性
        mChart.setDrawEntryLabels(true);              //设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
        mChart.setEntryLabelColor(getResources().getColor(R.color.tv_selected_color));       //设置pieChart图表文本字体颜色
        mChart.setEntryLabelTextSize(8f);            //设置pieChart图表文本字体大小

        // 设置 pieChart 内部圆环属性
        mChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        mChart.setHoleRadius(40f);
        mChart.setTransparentCircleRadius(0f);//设置PieChart内部圆的半径(这里设置28.0f)
        mChart.setHoleColor(Color.WHITE);             //设置PieChart内部圆的颜色
        mChart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）
        mChart.setCenterText("支出");                 //设置PieChart内部圆文字的内容
        mChart.setCenterTextSize(12f);                //设置PieChart内部圆文字的大小
        mChart.setCenterTextColor(getResources().getColor(R.color.tv_selected_color));         //设置PieChart内部圆文字的颜色

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
            get_money_data[i] = split[1];
        }

        String[] aa = Constant.changeStringArray(this, R.array.classify_title_exp);
        String[] bb = Constant.changeStringArray(this, R.array.classify_title_exp_color);
        for (int i = 0; i < get_classify_num_data.length; i++) {
            get_classify_title_data[i] = aa[Integer.valueOf(get_classify_num_data[i])];
            get_classify_color_data[i] = bb[Integer.valueOf(get_classify_num_data[i])];
            Log.d("lxr", get_classify_num_data[i] +"=" + get_classify_title_data[i]+"="+get_money_data[i]+"="+get_classify_color_data[i]);
        }


        ArrayList<PieEntry> percent = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0;i<get_classify_color_data.length;i++){
            colors.add(Color.parseColor(get_classify_color_data[i]));
            PieEntry entry = new PieEntry(Float.parseFloat(get_money_data[i]),get_classify_title_data[i]);
            percent.add(entry);
        }

        PieDataSet pieDataSet = new PieDataSet(percent,"");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setColors(colors);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1Length(0.6f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(getResources().getColor(R.color.tv_selected_color));
        pieData.setValueTextSize(10f);
        mChart.setData(pieData);
        mChart.invalidate();

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);


    }
}
