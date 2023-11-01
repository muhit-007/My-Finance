package com.example.financepro;

import android.graphics.Color;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>{

    private ArrayList<HistoryRecord> records;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView1;

        private TextView textView3;
        public ViewHolder(View v){
            super(v);
            textView1 = (TextView) v.findViewById(R.id.CatName);
            textView3 = (TextView) v.findViewById(R.id.amountShow);
        }
    }
    public TransactionAdapter(ArrayList<HistoryRecord> records) {
       this.records = records;
    }
    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.textView1.setText(records.get(position).getCat());
        holder.textView3.setText(String.valueOf(records.get(position).getAmount()));
        if (records.get(position).getAmount()<0){
            holder.textView3.setTextColor(Color.parseColor("#C62828"));
        }
        else {
            holder.textView3.setTextColor(Color.parseColor("#00695C"));
        }
    }
}

