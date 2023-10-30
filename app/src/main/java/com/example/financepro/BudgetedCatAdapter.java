package com.example.financepro;

import android.graphics.Color;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.view.MenuItem;
import android.widget.PopupMenu;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;
import android.widget.ProgressBar;
public class BudgetedCatAdapter extends RecyclerView.Adapter<BudgetedCatAdapter.ViewHolder>{
    private CategoryDatabase db;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    private Context context;
    private ArrayList<Category> captions;
    private Category category;
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView0;
        private TextView textView1;
        private TextView textView2;
        private TextView buttonViewOption;

        private ProgressBar progressBar;
        public ViewHolder(View v){
            super(v);
            textView0 = (TextView) v.findViewById(R.id.cat_name);
            textView1 = (TextView) v.findViewById(R.id.progressText);
            textView2 = (TextView) v.findViewById(R.id.remainingText);
            buttonViewOption = (TextView) v.findViewById(R.id.total_cat);
            progressBar = (ProgressBar) v.findViewById(R.id.percentBar);
        }
    }
    public BudgetedCatAdapter(Context context, ArrayList<Category> captions) {
        this.captions = captions;
        this.context = context;
        db = new CategoryDatabase(context);
    }
    @Override
    public int getItemCount() {
        return captions.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.progressBar.setMax(captions.get(position).getBudget());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int totalVal = db.totalCatAmount(captions.get(position).getName(), sdf.format(cal.getTime()));
        int lastDate = cal.getActualMaximum(Calendar.DATE);
        for (int j=1;j<lastDate;j++){
            cal.add(Calendar.DATE, 1);
            totalVal += db.totalCatAmount(captions.get(position).getName(), sdf.format(cal.getTime()));
        }
        totalVal *= -1;

        holder.progressBar.setProgress(totalVal);

        holder.textView0.setText(captions.get(position).getName());
        holder.textView1.setText("Exp: "+String.valueOf(totalVal)+"/"+String.valueOf(captions.get(position).getBudget()));
        holder.textView2.setText("Bal: "+String.valueOf(captions.get(position).getBudget()-totalVal));
        if(captions.get(position).getBudget()-totalVal < 0 ){
            holder.textView2.setTextColor(Color.parseColor("#C62828"));
        }else {
            holder.textView2.setTextColor(Color.parseColor("#00695C"));
        }

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int position = holder.getAdapterPosition();
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.buttonViewOption);
                popupMenu.inflate(R.menu.budget_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.removebudget){
                            db.updateBudget(captions.get(position).getName(), 0);
                            captions.remove(position);
                            notifyItemRemoved(position);
                        }
                        else if(menuItem.getItemId()==R.id.resetbudget){
                            setBudget(captions.get(position).getName(), position);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
    public void setBudget(final String s, int position){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.set_budget, null);
        EditText catField = (EditText) subView.findViewById(R.id.budgetInput);
        TextView month = (TextView) subView.findViewById(R.id.currMonth);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy");
        String curTime = sdf.format(cal.getTime());
        month.setText("Month: "+curTime);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set Budget");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int budget = 0;
                if(catField.getText().toString().isEmpty()){
                    Toast.makeText(context, "Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    budget = Integer.parseInt(catField.getText().toString());
                }

                if(budget <= 0){
                    Toast.makeText(context, "Amount must be more than 0", Toast.LENGTH_SHORT).show();
                }else {
                    db.updateBudget(s, budget);
                    captions.get(position).setBudget(budget);
                    notifyItemChanged(position);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

}

