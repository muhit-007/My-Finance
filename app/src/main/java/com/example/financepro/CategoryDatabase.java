package com.example.financepro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;

public class CategoryDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "task";
    private static final int DB_VERSION = 1;
    String[] expList = {"Food","Bill","Fees","Fuel","Education","Health","Shopping","Others"};
    String[] incList = {"Salary","Awards","Grants","Sale","Lottery","Refund"};

    CategoryDatabase(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db,0,DB_VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDatabase(db,oldVersion,newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion<1){
            db.execSQL("CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+"NAME TEXT, TYPE INTEGER, BUDGET INT);");
            for(int i=0;i<expList.length;i++){
                Category cat = new Category();
                cat.setName(expList[i]);
                cat.setType(0);
                defaultInsert(db, cat);
            }
            for(int i=0;i<incList.length;i++){
                Category cat = new Category();
                cat.setName(incList[i]);
                cat.setType(1);
                defaultInsert(db, cat);
            }

            db.execSQL("CREATE TABLE HISTORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, " +
                    "DATE TEXT, TIME TEXT, AMOUNT INTEGER);");
        }
    }
    public void defaultInsert(SQLiteDatabase db, Category category){
        ContentValues values = new ContentValues();
        values.put("NAME", category.getName());
        values.put("TYPE", category.getType());
        values.put("BUDGET", 0);
        db.insert("CATEGORY", null, values);
    }
    public int insertQ(Category category){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select * from CATEGORY WHERE NAME = '"+category.getName()+"'";
            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst()){
                return 1;
            }else {
                ContentValues values = new ContentValues();
                values.put("NAME", category.getName());
                values.put("TYPE", category.getType());
                values.put("BUDGET", 0);
                db.insert("CATEGORY", null, values);
                db.close();

            }

        }catch (SQLiteException e){

        }
        return 0;
    }
    ArrayList<Category> showList(int type){
        String sql = "select * from CATEGORY WHERE TYPE = "+type;
        ArrayList<Category> captions = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToFirst()){
                do{
                    Category category = new Category();
                    category.setName(cursor.getString(1));
                    category.setType(cursor.getInt(2));
                    category.setBudget(cursor.getInt(3));
                    captions.add(category);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (SQLiteException e){

        }
        return captions;
    }

    public void deleteItem(String s){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CATEGORY", "NAME = ?", new String[]{s});
        db.close();
    }
    public void update(String s, String name){
        ContentValues values = new ContentValues();
        values.put("NAME",name);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("CATEGORY", values, "NAME = ?", new String[]{s});
        db.close();
    }
    public void updateBudget(String s, int n){
        ContentValues values = new ContentValues();
        values.put("BUDGET", n);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("CATEGORY", values, "NAME = ?", new String[]{s});
        db.close();
    }

    public void insertTran(HistoryRecord record){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentTransaction = new ContentValues();
        contentTransaction.put("NAME", record.getCat());
        contentTransaction.put("DATE", record.getDate());
        contentTransaction.put("TIME", record.getTime());
        contentTransaction.put("AMOUNT", record.getAmount());
        db.insert("HISTORY", null, contentTransaction);
    }
    public ArrayList<HistoryRecord> showHistory(String date){
        String sql = "select * from HISTORY WHERE DATE= '"+date+"' ORDER BY _id DESC";
        ArrayList<HistoryRecord> records = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToFirst()){
                do{
                    HistoryRecord rec = new HistoryRecord();
                    rec.setCat(cursor.getString(1));
                    rec.setAmount(cursor.getInt(4));
                    records.add(rec);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (SQLiteException e){

        }

        return records;
    }

    public ArrayList<Category> showBudgetCat(){
        String sql = "select * from CATEGORY";
        ArrayList<Category> categories = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getInt(3)<=0)
                        continue;
                    Category category = new Category();
                    category.setName(cursor.getString(1));
                    category.setBudget(cursor.getInt(3));
                    categories.add(category);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (SQLiteException e){

        }

        return categories;
    }
    public int totalCatAmount(String name, String date){
        String sql = "select * from HISTORY WHERE DATE= '"+date+"' AND NAME = '"+name+"'";
        int amount = 0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToFirst()){
                do{
                    amount += (cursor.getInt(4));
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (SQLiteException e){

        }
        return amount;
    }
}
