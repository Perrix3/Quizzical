package com.example.quizzical;

public class gameHistoryClass {

    //Class used to save games to place into game history, data types are subject to change.

    private String date;
    private String opponent;
    private String result;
    private String time;

    public gameHistoryClass(String date, String opponent, String result, String time){
        this.date=date;
        this.opponent=opponent;
        this.result=result;
        this.time=time;
    }

    public String getDate(){
        return this.date;
    }
    public String getOpponent(){
        return this.opponent;
    }
    public String getResult(){
        return this.result;
    }
    public String getTime(){
        return this.time;
    }

    public void setDate(String date){
        this.date=date;
    }
    public void setOpponent(String opponent){
        this.opponent=opponent;
    }
    public void setResult(String result){
        this.result=result;
    }
    public void setTime(String time){
        this.time=time;
    }







}
