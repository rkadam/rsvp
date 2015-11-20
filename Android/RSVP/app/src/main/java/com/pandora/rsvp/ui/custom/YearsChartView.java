package com.pandora.rsvp.ui.custom;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pandora.rsvp.R;
import com.pandora.rsvp.service.contract.InviteResponse;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class YearsChartView extends RelativeLayout {

    private PieChart chart;
    private TextView switchText;

    private boolean isDept;
    private List<InviteResponse> invitationData;
    private Switch pieSwitch;

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
        chart = (PieChart) findViewById(R.id.chart);
        pieSwitch = (Switch) findViewById(R.id.pie_switch);
        switchText = (TextView) findViewById(R.id.switch_text);
        pieSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleCheck(isChecked);
            }
        });
        toggleCheck(false);
    }


    private void toggleCheck(boolean isChecked) {
        switchText.setText(isChecked ? R.string.years_data : R.string.dept_data);
        isDept = !isChecked;
        buildData();
    }


    public void setInvitationData(List<InviteResponse> invitationData) {
        this.invitationData = invitationData;
        pieSwitch.setChecked(false);
        toggleCheck(false);
    }

    public void buildData() {
        if (invitationData == null || invitationData.isEmpty()) {
            return;
        }

        HashMap<String, Integer> valuesMapping = new HashMap<>();
        for (InviteResponse response : invitationData) {
            String key = isDept ? response.department : String.valueOf(Math.round(response.years)) + " Year(s)";
            if (valuesMapping.containsKey(key)) {
                valuesMapping.put(key, valuesMapping.get(key) + 1);
            } else {
                valuesMapping.put(key, 1);
            }
        }

        chart.setCenterText(getContext().getResources().getString(isDept ? R.string.dept_dist : R.string.years_dist));
        chart.setDescription("");
        chart.setDrawSliceText(false);

        List<Entry> entries = new ArrayList<>();
        List<Integer> values = new ArrayList<>(valuesMapping.values());
        for (int i = 0; i < values.size(); i++) {
            entries.add(new Entry(values.get(i), i));
        }
        PieDataSet set = new PieDataSet(entries, "");
        set.setSliceSpace(10);
        set.setColors(getRandomColors(entries.size()));
        set.setValueTextSize(16);
        set.setSelectionShift(20);
        PieData data = new PieData(new ArrayList<>(valuesMapping.keySet()));
        data.setDataSet(set);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });
        data.setValueTextSize(12);
        chart.setData(data);
        chart.invalidate();
        chart.notifyDataSetChanged();
    }


    private int[] getRandomColors(int count) {
        int[] colors = new int[count];
        Random random = new Random();
        int color = ContextCompat.getColor(getContext(), R.color.colorAccent);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float hue = hsv[0];
        float luminance = 0.9f;

        for (int i = 0; i < count; i++) {
            float saturation = (random.nextInt(2000) + 1000) / 10000f;
            colors[i] = Color.HSVToColor(new float[]{hue, saturation, luminance});
        }
        return colors;
    }


}
