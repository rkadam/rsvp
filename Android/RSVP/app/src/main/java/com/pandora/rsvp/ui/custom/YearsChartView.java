package com.pandora.rsvp.ui.custom;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.pandora.rsvp.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class YearsChartView extends RelativeLayout {

    public YearsChartView(Context context) {
        super(context);
        init();
    }

    public YearsChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YearsChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.frag_charts, this);
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setCenterText(getContext().getResources().getString(R.string.years_dist));
        chart.setBackgroundColor(Color.TRANSPARENT);
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(10, 0));
        entries.add(new Entry(100, 1));
        entries.add(new Entry(40, 2));
        PieDataSet set = new PieDataSet(entries, "");
        set.setSliceSpace(10);
        set.setSelectionShift(20);
        PieData data = new PieData(new String[]{"10", "100", "40"});
        data.setDataSet(set);
        chart.setData(data);
    }


}
