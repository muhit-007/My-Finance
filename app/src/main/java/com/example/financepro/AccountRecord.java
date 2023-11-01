package com.example.financepro;

public class AccountRecord {
    public static int TOTAL = 0;
    private String label;
    private String date;
    private int amount;
    private int type;
    private boolean status;

    public void setLabel(String s){
        this.label = s;
    }
    public void setAmount(int i){
        this.amount = i;
    }
    public void setType(int s){
        this.type = s;
    }
    public void setDate(String s){
        this.date = s;
    }
    public void setStatus(boolean i){
        this.status = i;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getLabel() {
        return label;
    }

    public int getType() {
        return type;
    }

    public boolean isStatus() {
        return status;
    }
}
