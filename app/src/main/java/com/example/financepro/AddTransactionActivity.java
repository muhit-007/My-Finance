package com.example.financepro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.viewpager.widget.ViewPager;
import android.content.SharedPreferences;
import android.app.DialogFragment;
import android.widget.DatePicker;
public class AddTransactionActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {


    private CategoryDatabase categoryDatabase;
    private ArrayList<String> categoryList = new ArrayList<>();
    private Button dateET;

    private EditText amountET;
    private RadioGroup radioGroup;
    HistoryRecord record = new HistoryRecord();

    public static ArrayList<String> convertTo(ArrayList<Category> list){
        ArrayList<String> nameList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            nameList.add(list.get(i).getName());
        }
        return nameList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        categoryDatabase = new CategoryDatabase(this);

        dateET = (Button) findViewById(R.id.dateET);
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.example.financepro.DatePicker date = new com.example.financepro.DatePicker();
                date.show(getSupportFragmentManager(), "DatePick");
            }
        });
        amountET = (EditText) findViewById(R.id.amountET);

        radioGroup = (RadioGroup) findViewById(R.id.groupradio);
        radioGroup.check(R.id.radia_id2);

        categoryList = convertTo(categoryDatabase.showList(0));
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);

        categoryList = convertTo(categoryDatabase.showList(1));
        ArrayAdapter<String> stringArrayAdapterInc = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        stringArrayAdapterInc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId==R.id.radia_id1)
                    spinner.setAdapter(stringArrayAdapterInc);
                else
                    spinner.setAdapter(stringArrayAdapter);
            }
        });

        Button inbtn = (Button) findViewById(R.id.incomebtn);
        inbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = amountET.getText().toString();
                if (s.equals("")) {
                    Toast.makeText(AddTransactionActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                int amount = Integer.parseInt(s);

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(AddTransactionActivity.this, "Choose Income or Expense", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(selectedId == R.id.radia_id2){
                    amount *= -1;
                }

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String curTime = sdf.format(cal.getTime());

                String inputDate = dateET.getText().toString();

                if(inputDate.equals("Today")){
                    SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                    record.setDate(sd.format(cal.getTime()));
                }
                record.setAmount(amount);
                record.setTime(curTime);
                record.setCat(String.valueOf(spinner.getSelectedItem()));

                confirmTransaction(String.valueOf(spinner.getSelectedItem()), inputDate, amount);
            }
        });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        String selectedDate = dateFormat.format(mCalendar.getTime());
        dateET.setText(selectedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        record.setDate(sdf.format(mCalendar.getTime()));
    }




    public void confirmTransaction(String cat, String date, int amount){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.confirmation, null);
        TextView catField = (TextView) subView.findViewById(R.id.confmsg);
        catField.setText("Are you sure?\n"+date+"\n"+cat+"  "+String.valueOf(amount));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Transaction Confirmation");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                amountET.getText().clear();
                amountET.clearFocus();
                categoryDatabase.insertTran(record);
            }
        });
        builder.setNegativeButton("Drop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

}