package com.example.financepro;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;
import android.view.View;
import java.util.ArrayList;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;
import android.text.TextUtils;
import androidx.cardview.widget.CardView;
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

