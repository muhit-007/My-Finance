package com.example.financepro;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.TextView;
import android.graphics.Color;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.animation.Easing;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalyticsFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        MyDialog.DisplayOptionListener{
    private ArrayList<Category> records = new ArrayList<>();
    private OverviewAdapter overviewAdapter;
    private RecyclerView recyclerView;
    private LinearLayout layout;
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList<PieEntry> pieEntries;
    private int overviewMode = 0;
    private DisplayOption displayOverview;
    private Calendar cal = TransactionFragment.cal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.overviewSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.overview_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(AnalyticsFragment.this);
        pieChart = (PieChart) view.findViewById(R.id.pieChart);

        recyclerView = (RecyclerView) view.findViewById(R.id.catView);
        displayOverview = new DisplayOption(getActivity(), view);
        overviewRender();

        ImageButton button = (ImageButton) view.findViewById(R.id.showDisplayOption);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog d = new MyDialog(AnalyticsFragment.this);
                d.show(getChildFragmentManager(), "MyDialog");
            }
        });

        ImageButton buttonNext = (ImageButton) view.findViewById(R.id.next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.DATE, 1);
                overviewRender();
            }
        });

        ImageButton buttonPrev = (ImageButton) view.findViewById(R.id.prev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayOverview.initState(cal);
                cal.add(Calendar.DATE, -1);
                overviewRender();
            }
        });

        return view;
    }
    @Override
    public void onDisplayOptionChanged(){
        cal = Calendar.getInstance();
        overviewRender();
    }
    private void overviewRender(){
        records =displayOverview.catOverview(overviewMode, cal);
        if(records.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            pieChart.setVisibility(View.INVISIBLE);
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.VISIBLE);
        overviewAdapter = new OverviewAdapter(records);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(overviewAdapter);

        if(overviewMode==0) makePieChart("Expense");
        else makePieChart("Income");
    }
    private void makePieChart(String s){

        int[] colors = new int[20];
        int counter = 0;

        for (int color : ColorTemplate.JOYFUL_COLORS
        ) {
            colors[counter] = color;
            counter++;
        }

        for (int color : ColorTemplate.MATERIAL_COLORS
        ) {
            colors[counter] = color;
            counter++;
        }
        for (int color : ColorTemplate.PASTEL_COLORS
        ) {
            colors[counter] = color;
            counter++;
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS
        ) {
            colors[counter] = color;
            counter++;
        }


        getEntries();
        pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(12f);
        pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText(s);
        pieChart.setCenterTextSize(20f);
        pieChart.animateY(1000, Easing.EaseInOutQuad);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setEnabled(true);
        pieChart.invalidate();
    }

    private void getEntries() {
        pieEntries = new ArrayList<>();
        for (int i=0;i<records.size();i++){
            pieEntries.add(new PieEntry(Math.abs(records.get(i).total)*1f, records.get(i).getName()));
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextSize(20);
        overviewMode = position;
        overviewRender();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

}