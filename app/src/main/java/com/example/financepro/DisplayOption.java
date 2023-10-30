package com.example.financepro;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.lang.Math;

import android.content.Context;

public class DisplayOption {
    private Context context;
    private View view;
    private ArrayList<HistoryRecord> records = new ArrayList<>();
    private Calendar cal;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat headf= new SimpleDateFormat("dd MMM, yyyy");
    private SimpleDateFormat tvString = new SimpleDateFormat("MMM dd");
    private SimpleDateFormat tvMonth = new SimpleDateFormat("MMMM, yyyy");
    private CategoryDatabase database;
    public static TransactionAdapter adapter;
    private ArrayList<Category> list;
    private  LinearLayout body;



    DisplayOption(Context context, View view){
        this.context = context;
        database = new CategoryDatabase(context);
        this.view = view;
    }
    public void displayRecord(Calendar cal){
        //----------------------Initialize different Variable-------------------------->>
        body = (LinearLayout) view.findViewById(R.id.recordBody);
        TextView tv = (TextView) view.findViewById(R.id.modeText);
        this.cal = cal;
        body.removeAllViewsInLayout();
        body.invalidate();
        HistoryRecord.INC = 0;
        HistoryRecord.EXP = 0;

        //-------------------Calculate Data by User choice----------------------------->>
        if(MainActivity.viewValue==R.id.daily){
            this.viewBuilder(cal);
            tv.setText(tvString.format(cal.getTime()));
        }
        else if(MainActivity.viewValue==R.id.weekly){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            this.viewBuilder(cal);
            String s = tvString.format(cal.getTime()) + "-";
            for(int j=0; j<6; j++){
                cal.add(Calendar.DATE, 1);
                this.viewBuilder(cal);
            }
            s += tvString.format(cal.getTime());
            tv.setText(s);
        }
        else if(MainActivity.viewValue==R.id.monthly){
            cal.set(Calendar.DAY_OF_MONTH, 1);
            this.viewBuilder(cal);
            int lastDate = cal.getActualMaximum(Calendar.DATE);
            for (int j=1;j<lastDate;j++){
                cal.add(Calendar.DATE, 1);
                this.viewBuilder(cal);
            }
            tv.setText(tvMonth.format(cal.getTime()));
        }

        //----------------------Set Value------------------------->>
        TextView inc = (TextView) view.findViewById(R.id.incValue);
        inc.setText(String.valueOf(HistoryRecord.INC));
        TextView exp = (TextView) view.findViewById(R.id.expValue);
        exp.setText(String.valueOf(HistoryRecord.EXP *-1));
        TextView total = (TextView) view.findViewById(R.id.totalValue);
        total.setText(String.valueOf(HistoryRecord.INC + HistoryRecord.EXP)); //EXP is always negative form
        if(Integer.parseInt(total.getText().toString())<0){
            total.setTextColor(Color.parseColor("#C62828"));
        }
        else {
            total.setTextColor(Color.parseColor("#00695C"));
        }

    }
    private void viewBuilder(Calendar cal){
        String header = headf.format(cal.getTime());
        String dateBy = sdf.format(cal.getTime());
        records = database.showHistory(dateBy);
        if(records.isEmpty()){return;}
        adapter = new TransactionAdapter(records);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        TextView dateHead = new TextView(context);
        dateHead.setText(header);
        dateHead.setLayoutParams(params);
        dateHead.setTextSize(18.0f);
        dateHead.setPadding(40,30,0,30);
        dateHead.setTypeface(null, Typeface.BOLD_ITALIC);
        body.addView(dateHead);

        RecyclerView newView = new RecyclerView(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        newView.setLayoutManager(layoutManager);
        newView.setLayoutParams(params);
        newView.setAdapter(adapter);
        body.addView(newView);

    }
    public void initState(Calendar cal){
        if(MainActivity.viewValue==R.id.weekly){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
        else if(MainActivity.viewValue==R.id.monthly){
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public ArrayList<Category> catOverview(int type, Calendar cal){
        list = new ArrayList<>();
        int s;
        ArrayList<Category> categories = new ArrayList<>();
        categories = database.showList(type);
        for(int i=0;i<categories.size();i++){
            s = getTotal(categories.get(i).getName(), cal);
            if(Math.abs(s) > 0){
                categories.get(i).total = s;
                list.add(categories.get(i));
            }
        }
        return  list;
    }
    public int getTotal(String s, Calendar cal){
        HistoryRecord.INC = 0;
        HistoryRecord.EXP = 0;
        TextView tv = (TextView) view.findViewById(R.id.modeText);
        String date;
        int total = 0;
        if(MainActivity.viewValue==R.id.daily){
            date = sdf.format(cal.getTime());
            total += database.totalCatAmount(s,date);
            tv.setText(tvString.format(cal.getTime()));
            database.showHistory(date);
        }
        else if(MainActivity.viewValue==R.id.weekly){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            date = sdf.format(cal.getTime());
            total += database.totalCatAmount(s,date);
            database.showHistory(date);
            String ss = tvString.format(cal.getTime()) + "-";
            for(int j=0; j<6; j++){
                cal.add(Calendar.DATE, 1);
                date = sdf.format(cal.getTime());
                total += database.totalCatAmount(s,date);
                database.showHistory(date);
            }
            ss += tvString.format(cal.getTime());
            tv.setText(ss);
        }
        else if(MainActivity.viewValue==R.id.monthly){
            cal.set(Calendar.DAY_OF_MONTH, 1);
            date = sdf.format(cal.getTime());
            total += database.totalCatAmount(s,date);
            database.showHistory(date);
            int lastDate = cal.getActualMaximum(Calendar.DATE);
            for (int j=1;j<lastDate;j++){
                cal.add(Calendar.DATE, 1);
                date = sdf.format(cal.getTime());
                total += database.totalCatAmount(s,date);
                database.showHistory(date);
            }
            tv.setText(tvMonth.format(cal.getTime()));
        }
        //----------------------Set Value------------------------->>
        TextView inc = (TextView) view.findViewById(R.id.incValue);
        inc.setText(String.valueOf(HistoryRecord.INC));
        TextView exp = (TextView) view.findViewById(R.id.expValue);
        exp.setText(String.valueOf(HistoryRecord.EXP *-1));
        TextView totaltext = (TextView) view.findViewById(R.id.totalValue);
        totaltext.setText(String.valueOf(HistoryRecord.INC + HistoryRecord.EXP)); //EXP is always negative form
        if(Integer.parseInt(totaltext.getText().toString())<0){
            totaltext.setTextColor(Color.parseColor("#C62828"));
        }
        else {
            totaltext.setTextColor(Color.parseColor("#00695C"));
        }

        return total;
    }


}
