package com.example.financepro;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.view.MenuItem;
import android.view.Menu;
import android.widget.PopupMenu;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;
import android.text.TextUtils;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private CategoryDatabase db;
    private Context context;
    private ArrayList<Category> captions;
    private ArrayList<Category> budgetCat = BudgetCatFragment.categories;
    private BudgetedCatAdapter budgetAdapter = BudgetCatFragment.adapter;
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private TextView buttonViewOption;
        public ViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.item);
            buttonViewOption = (TextView) v.findViewById(R.id.textViewOptions);
        }
    }
    public CategoryAdapter(Context context, ArrayList<Category> captions) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String name = captions.get(position).getName();
        holder.textView.setText(name);
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //osition = holder.getAdapterPosition();
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.buttonViewOption);
                popupMenu.inflate(R.menu.recycler_item_menu);
                Menu menu = popupMenu.getMenu();
                if (captions.get(position).getBudget() > 0 || captions.get(position).getType()==1) {
                    menu.getItem(0).setEnabled(false);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.delete){
                            db.deleteItem(name);

                            BudgetCatFragment.categories.remove(captions.remove(position));
                            notifyDataSetChanged();
                            BudgetCatFragment.adapter.notifyDataSetChanged();

                        }
                        else if(menuItem.getItemId()==R.id.edit){
                            editCat(name, position);
                        }
                        else if(menuItem.getItemId()==R.id.budget){
                            setBudget(name, position);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
    public void editCat(final String s, int position){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.edit_category, null);
        final EditText catField = (EditText) subView.findViewById(R.id.enterCat);
        if(s != null) catField.setText(s);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Category");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String category = catField.getText().toString();
                if(TextUtils.isEmpty(category)){
                    Toast.makeText(context, "Enter Category", Toast.LENGTH_SHORT).show();
                }else {
                    db.update(s,category);
                    captions.get(position).setName(category);
                    notifyDataSetChanged();
                    BudgetCatFragment.adapter.notifyDataSetChanged();
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
                    notifyDataSetChanged();

                    BudgetCatFragment.categories.add(captions.get(position));
                    BudgetCatFragment.adapter.notifyDataSetChanged();
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
    public int findIndex(ArrayList<Category> cat, String s){
        if(cat.isEmpty()) return -1;
        for(int i=0;i<cat.size();i++){
            if(cat.get(i).getName() == s)
                return i;
        }
        return -1;
    }

}

