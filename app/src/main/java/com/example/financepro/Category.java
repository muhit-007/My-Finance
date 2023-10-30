package com.example.financepro;

public class Category {
    public int total=0;
    private String name;
    private int type;

    private int budget = 0;

    public void setName(String s){
        this.name = s;
    }
    public void setType(int i){
        this.type = i;
    }
    public void setTotal(int i){
        this.total = i;
    }

    public void setBudget(int i){
        if(i>0) this.budget = i;
    }

    String getName(){
        return this.name;
    }
    int getType(){
        return this.type;
    }
    int getTotal(){
        return this.total;
    }

    int getBudget(){
        return this.budget;
    }

}
