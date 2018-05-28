package com.luxinrun.fastbilling.ui;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.luxinrun.fastbilling.adapter.StatisticsSameItemRecyclerViewAdapter;
import com.luxinrun.fastbilling.assistent.Constant;
import com.luxinrun.fastbilling.assistent.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    Calendar calendar = Calendar.getInstance();
    private int CURRENT_YEAR = calendar.get(Calendar.YEAR);
    private int CURRENT_MONTH = calendar.get(Calendar.MONTH);
    private int CURRENT_DAY = calendar.get(Calendar.DAY_OF_MONTH);
    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;
    private boolean isDateRight;
    private String startDate;
    private String endDate;
    private String expORincome = "0";
    private PopupWindow sameItemPopWindow;
    private PopupWindow chooseDatePopWindow;
    private ImageButton btn_statistics_back;
    private TextView statistics_date_tv;
    private TextView statistics_exp_tv;
    private TextView statistics_income_tv;
    private PieChart mChart;
    private RecyclerView statistics_recyclerView;
    private StatisticsRecyclerViewAdapter statisticsRecyclerViewAdapter;
    private StatisticsSameItemRecyclerViewAdapter statisticsSameItemRecyclerViewAdapter;
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

        drawPieChart(expORincome);


    }

    private String appendDate(int year, int month, int day) {
        String date = year + "-" + Constant.num_Format(month + 1) + "-" + Constant.num_Format(day);
        return date;
    }

    private void initId() {
        mChart = (PieChart) findViewById(R.id.mPieChart);
        btn_statistics_back = (ImageButton) findViewById(R.id.btn_statistics_back);
        btn_statistics_back.setOnClickListener(this);
        statistics_date_tv = (TextView) findViewById(R.id.statistics_date_tv);
        statistics_date_tv.setOnClickListener(this);
        statistics_exp_tv = (TextView) findViewById(R.id.statistics_exp_tv);
        statistics_exp_tv.setOnClickListener(this);
        statistics_income_tv = (TextView) findViewById(R.id.statistics_income_tv);
        statistics_income_tv.setOnClickListener(this);

        statistics_recyclerView = (RecyclerView) findViewById(R.id.statistics_recyclerView);
        initDateChoose();
    }

    private void initDateChoose() {
        isDateRight = false;
        startYear = CURRENT_YEAR;
        startMonth = CURRENT_MONTH;
        startDay = 1;
        endYear = CURRENT_YEAR;
        endMonth = CURRENT_MONTH;
        endDay = CURRENT_DAY;
        startDate = appendDate(startYear, startMonth, startDay);
        endDate = appendDate(endYear, endMonth, endDay);
        statistics_date_tv.setText(startDate + getString(R.string.to_append) + endDate);
    }

    private void drawPieChart(String expORincome) {
        mChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);
        mChart.setBackgroundResource(R.color.colorWhite);
        mChart.setExtraOffsets(0, 15, 0, 15);       //设置pieChart图表上下左右的偏移，类似于外边距
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
        if (expORincome.equals("0")){
            mChart.setCenterText(getString(R.string.title_exp));
            statistics_exp_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            statistics_exp_tv.setBackgroundResource(R.drawable.tv_exp_bg_selected);
            statistics_income_tv.setTextColor(getResources().getColor(R.color.colorWhite));
            statistics_income_tv.setBackgroundResource(R.drawable.tv_income_bg_nor);
            expORincome = "0";
        }else {
            mChart.setCenterText(getString(R.string.title_income));
            statistics_income_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            statistics_income_tv.setBackgroundResource(R.drawable.tv_income_bg_selected);
            statistics_exp_tv.setTextColor(getResources().getColor(R.color.colorWhite));
            statistics_exp_tv.setBackgroundResource(R.drawable.tv_exp_bg_nor);
            expORincome = "1";
        }
        mChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        mChart.setCenterTextSize(14f);                //设置PieChart内部圆文字的大小
        mChart.setCenterTextColor(getResources().getColor(R.color.tv_selected_color));         //设置PieChart内部圆文字的颜色

        dbHelper = new DBHelper(this);
        data = dbHelper.cursorDateSlot(startDate + " 00:00:00", endDate + " 23:59:59", expORincome);
        same_classify_data = Constant.getPieChartData(this, data , expORincome);
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
        statisticsRecyclerViewAdapter = new StatisticsRecyclerViewAdapter(this, same_classify_data, expORincome);
        statistics_recyclerView.setAdapter(statisticsRecyclerViewAdapter);

        statisticsRecyclerViewAdapter.setOnItemClickListener(new StatisticsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showSameClassifyItem(position);
            }
        });
    }

    private void showChooseDate() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_choose_date, null);
        chooseDatePopWindow = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        chooseDatePopWindow.setFocusable(true);
        chooseDatePopWindow.showAtLocation(this.findViewById(R.id.statistics_recyclerView), Gravity.CENTER_VERTICAL, 0, 0);
        chooseDatePopWindow.setOutsideTouchable(true);
        Button sure_date_btn = (Button) view.findViewById(R.id.sure_date_btn);
        DatePicker start_date_picker = (DatePicker) view.findViewById(R.id.start_date_picker);
        DatePicker end_date_picker = (DatePicker) view.findViewById(R.id.end_date_picker);
        start_date_picker.setMaxDate(new Date().getTime());
        end_date_picker.setMaxDate(new Date().getTime());
        start_date_picker.init(startYear, startMonth, startDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
            }
        });

        end_date_picker.init(endYear, endMonth, endDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endYear = year;
                endMonth = monthOfYear;
                endDay = dayOfMonth;
            }
        });

        sure_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((startYear > endYear) || ((startYear == endYear) && (startMonth > endMonth)) || ((startYear == endYear) && (startMonth == endMonth) && (startDay >= endDay))) {
                    Toast.makeText(StatisticsActivity.this, getString(R.string.error_date_choose), Toast.LENGTH_SHORT).show();
                    isDateRight = false;
                } else {
                    isDateRight = true;
                    chooseDatePopWindow.dismiss();
                }

            }
        });
        chooseDatePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isDateRight) {
                    initDateChoose();
                }
                startDate = appendDate(startYear, startMonth, startDay);
                endDate = appendDate(endYear, endMonth, endDay);
                statistics_date_tv.setText(startDate + getString(R.string.to_append) + endDate);
                drawPieChart(expORincome);
            }
        });


    }

    private void showSameClassifyItem(int position) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_same_classify_item, null);
        sameItemPopWindow = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        sameItemPopWindow.setFocusable(true);
        sameItemPopWindow.showAtLocation(this.findViewById(R.id.statistics_recyclerView), Gravity.CENTER_VERTICAL, 0, 0);
        sameItemPopWindow.setOutsideTouchable(true);
        TextView same_item_total_money = (TextView) view.findViewById(R.id.same_item_total_money);
        TextView same_item_title = (TextView) view.findViewById(R.id.same_item_title);
        ImageView same_item_icon = (ImageView) view.findViewById(R.id.same_item_icon);
        ImageView btn_same_item_close = (ImageView) view.findViewById(R.id.btn_same_item_close);
        btn_same_item_close.setOnClickListener(this);
        RecyclerView same_item_recyclerView = (RecyclerView) view.findViewById(R.id.same_item_recyclerView);
        LinearLayout delete_btn_layout = (LinearLayout) view.findViewById(R.id.delete_btn_layout);
        RelativeLayout delete_null_layout = (RelativeLayout) view.findViewById(R.id.delete_null_layout);
        String classify_title = same_classify_data.get(position).get("classify_title").toString();
        int classify_num = Integer.valueOf(same_classify_data.get(position).get("classify_num").toString());
        ArrayList<Map<String, Object>> sameArrayList = Constant.getSameClassifyData(this, data, classify_num);
        same_item_total_money.setText("("+get_money_data[position]+")");
        same_item_title.setText(classify_title);
        same_item_icon.setImageResource(Constant.changeDrawableArray(this, R.array.classify_exp_icon_selected)[classify_num]);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        manager.setOrientation(LinearLayout.VERTICAL);
        same_item_recyclerView.setLayoutManager(manager);
        statisticsSameItemRecyclerViewAdapter = new StatisticsSameItemRecyclerViewAdapter(this, sameArrayList);
        same_item_recyclerView.setAdapter(statisticsSameItemRecyclerViewAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_statistics_back:
                this.finish();
                break;
            case R.id.statistics_date_tv:
                showChooseDate();
                break;
            case R.id.btn_same_item_close:
                sameItemPopWindow.dismiss();
                break;
            case R.id.statistics_exp_tv:
                expORincome = "0";
                drawPieChart(expORincome);
                break;
            case R.id.statistics_income_tv:
                expORincome = "1";
                drawPieChart(expORincome);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
