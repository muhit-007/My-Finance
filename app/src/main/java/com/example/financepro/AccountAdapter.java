package com.example.financepro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder>{

    private ArrayList<AccountRecord> records;
    private CategoryDatabase database;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView1;

        private TextView textView3;
        private TextView textView4;
        private Button button;
        public ViewHolder(View v){
            super(v);
            textView1 = (TextView) v.findViewById(R.id.CatName);
            textView3 = (TextView) v.findViewById(R.id.amountShow);
            textView4 = (TextView) v.findViewById(R.id.initDate);
            button = (Button) v.findViewById(R.id.take_act);
        }
    }
    public AccountAdapter(Context context,ArrayList<AccountRecord> records) {
        this.records = records;
        database = new CategoryDatabase(context);
    }
    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_card, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.textView1.setText(records.get(position).getLabel());
        holder.textView3.setText("Amount: "+records.get(position).getAmount());
        holder.textView4.setText("Date: "+records.get(position).getDate());
        if(records.get(position).getType()==0){
            holder.button.setText("Pay");
        }
        else {
            holder.button.setText("Cut");
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.deleteAcRecord(records.get(position).getLabel());
                records.remove(position);
                notifyDataSetChanged();
            }
        });
    }
}


