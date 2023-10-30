package com.example.financepro;

public class HistoryRecord {
    public static int INC = 0;
    public static int EXP = 0;
    private String cat;
    private String date;
    private String time;
    private int amount;
    public void setCat(String c){
        this.cat = c;
    }
    public void setDate(String c){
        this.date = c;
    }
    public void setTime(String c){
        this.time = c;
    }
    public void setAmount(int i){
        this.amount = i;
        add(i);
    }


    public String getCat(){
        return cat;
    }
    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }
    public int getAmount(){
        return amount;
    }
    private void add(int i){
        if(i>0){
            INC += i;
        }else
            EXP += i;
    }
}
