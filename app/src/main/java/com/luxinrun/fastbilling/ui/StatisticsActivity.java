package com.luxinrun.fastbilling.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.luxinrun.fastbilling.R;
import com.luxinrun.fastbilling.adapter.DetailRecyclerViewAdapter;
import com.luxinrun.fastbilling.adapter.StatisticsRecyclerViewAdapter;
import com.luxinrun.fastbilling.assistent.Constant;
import com.luxinrun.fastbilling.assistent.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    Calendar calendar = Calendar.getInstance();
    private int CURRENT_YEAR = calendar.get(Calendar.YEAR);
    private int CURRENT_MONTH = calendar.get(Calendar.MONTH);
    private PopupWindow sameItemPopWindow;
    private ImageButton btn_statistics_back;
    private PieChart mChart;
    private RecyclerView statistics_recyclerView;
    private StatisticsRecyclerViewAdapter statisticsRecyclerViewAdapter;
    private DBHelper dbHelper;
    private ArrayList<Map<String, Object>> data;//详细数据
    private ArrayList<Map<String, Object>> same_classify_data;//按类别分的数据
    private String[] get_classify_num_data;
    private String[] get_classify_title_data;
    private String[] get_classify_color_data;
    private String[] get_money_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initId();

        drawPieChart("0");


    }

    private void initId() {
        mChart = (PieChart) findViewById(R.id.mPieChart);
        btn_statistics_back = (ImageButton) findViewById(R.id.btn_statistics_back);
        btn_statistics_back.setOnClickListener(this);
        statistics_recyclerView = (RecyclerView) findViewById(R.id.statistics_recyclerView);
    }

    private void drawPieChart(String expORincome) {
        mChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);
        mChart.setBackgroundResource(R.color.colorWhite);
        mChart.setExtraOffsets(0, 20, 0, 20);       //设置pieChart图表上下左右的偏移，类似于外边距
        mChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]
        mChart.setRotationAngle(0);                   //设置pieChart图表起始角度
        mChart.setRotationEnabled(true);              //设置pieChart图表是否可以手动旋转
        mChart.setHighlightPerTapEnabled(true);       //设置piecahrt图表点击Item高亮是否可用
        mChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);// 设置pieChart图表展示动画效果
        // 设置 pieChart 图表Item文本属性
        mChart.setDrawEntryLabels(true);              //设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）
        mChart.setEntryLabelColor(getResources().getColor(R.color.tv_selected_color));       //设置pieChart图表文本字体颜色
        mChart.setEntryLabelTextSize(12f);            //设置pieChart图表文本字体大小

        // 设置 pieChart 内部圆环属性
        mChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(0f);//设置PieChart内部圆的半径(这里设置28.0f)
        mChart.setHoleColor(Color.WHITE);             //设置PieChart内部圆的颜色
        mChart.setDrawCenterText(true);               //是否绘制PieChart内部中心文本（true：下面属性才有意义）
        mChart.setClickable(true);
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lxr","sssss");
            }
        });
        mChart.setCenterText(getString(R.string.title_exp));                 //设置PieChart内部圆文字的内容
        mChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        mChart.setCenterTextSize(14f);                //设置PieChart内部圆文字的大小
        mChart.setCenterTextColor(getResources().getColor(R.color.tv_selected_color));         //设置PieChart内部圆文字的颜色

        dbHelper = new DBHelper(this);
        data = dbHelper.cursorMonth(CURRENT_YEAR + Constant.num_Format(CURRENT_MONTH + 1), "0");
        same_classify_data = Constant.getPieChartData(this, data);
        get_classify_num_data = new String[same_classify_data.size()];
        get_classify_title_data = new String[same_classify_data.size()];
        get_classify_color_data = new String[same_classify_data.size()];
        get_money_data = new String[same_classify_data.size()];
        for (int i = 0; i < same_classify_data.size(); i++) {
            get_classify_num_data[i] = same_classify_data.get(i).get("classify_num").toString();
            get_classify_title_data[i] = same_classify_data.get(i).get("classify_title").toString();
            get_money_data[i] = same_classify_data.get(i).get("money").toString();
            get_classify_color_data[i] = same_classify_data.get(i).get("color").toString();
        }

        ArrayList<PieEntry> percent = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < same_classify_data.size(); i++) {
            colors.add(Color.parseColor(get_classify_color_data[i]));
            PieEntry entry = new PieEntry(Float.parseFloat(get_money_data[i]), get_classify_title_data[i]);
            percent.add(entry);
        }

        PieDataSet pieDataSet = new PieDataSet(percent, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setColors(colors);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1Length(0.5f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(getResources().getColor(R.color.tv_selected_color));
        pieData.setValueTextSize(10f);
        mChart.setData(pieData);
        mChart.invalidate();

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        manager.setOrientation(LinearLayout.VERTICAL);
        statistics_recyclerView.setLayoutManager(manager);
        statisticsRecyclerViewAdapter = new StatisticsRecyclerViewAdapter(this, same_classify_data, "0");
        statistics_recyclerView.setAdapter(statisticsRecyclerViewAdapter);

        statisticsRecyclerViewAdapter.setOnItemClickListener(new StatisticsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showSameClassifyItem(position);
            }
        });
    }

    private void showSameClassifyItem(int position){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_same_classify_item, null);
        sameItemPopWindow = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        sameItemPopWindow.setFocusable(true);
        sameItemPopWindow.showAtLocation(this.findViewById(R.id.statistics_recyclerView), Gravity.CENTER_VERTICAL, 0, 0);
        sameItemPopWindow.setOutsideTouchable(true);
        TextView same_item_title = (TextView) view.findViewById(R.id.same_item_title);
        ImageView same_item_icon = (ImageView) view .findViewById(R.id.same_item_icon);
        RecyclerView same_item_recyclerView = (RecyclerView) view.findViewById(R.id.same_item_recyclerView);
        LinearLayout delete_btn_layout = (LinearLayout) view.findViewById(R.id.delete_btn_layout);
        RelativeLayout delete_null_layout = (RelativeLayout) view.findViewById(R.id.delete_null_layout);
        String classify_title = same_classify_data.get(position).get("classify_title").toString();
        int classify_num = Integer.valueOf(same_classify_data.get(position).get("classify_num").toString());
        same_item_title.setText(classify_title);
        same_item_icon.setImageResource(Constant.changeDrawableArray(this,R.array.classify_exp_icon_selected)[classify_num]);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        manager.setOrientation(LinearLayout.VERTICAL);
        same_item_recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_statistics_back:
                this.finish();
                break;
        }
    }
}
