package com.example.financepro;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.text.DecimalFormat;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder>{

    private ArrayList<Category> records;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private ProgressBar progressBar;
        public ViewHolder(View v){
            super(v);
            textView1 = (TextView) v.findViewById(R.id.cat_name);
            textView2 = (TextView) v.findViewById(R.id.total_cat);
            textView3 = (TextView) v.findViewById(R.id.percent);
            progressBar = (ProgressBar) v.findViewById(R.id.percentProgress);
        }
    }
    public OverviewAdapter(ArrayList<Category> records) {
        this.records = records;
    }
    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(OverviewAdapter.ViewHolder holder, int position){

        holder.textView1.setText(records.get(position).getName());
        holder.textView2.setText(String.valueOf(records.get(position).total));
        double i = records.get(position).total*1.00;
        DecimalFormat df = new DecimalFormat("#.##");

        if (records.get(position).getType()==0){
            double d = i/HistoryRecord.EXP*100;
            holder.textView3.setText(String.valueOf(df.format(d))+"%");
            holder.textView2.setTextColor(Color.parseColor("#C62828"));
            holder.progressBar.setMax(HistoryRecord.EXP*-1);

        }
        else {
            holder.textView3.setText(String.valueOf(df.format(i/HistoryRecord.INC*100))+"%");
            holder.textView2.setTextColor(Color.parseColor("#00695C"));
            holder.progressBar.setMax(HistoryRecord.INC);
        }
        holder.progressBar.setProgress(Math.abs(records.get(position).total));
    }
}
