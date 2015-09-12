package com.coditsuisse.team60.expensetracker;

import java.sql.Date;

/**
 * Created by Kshitij on 9/12/2015.
 */

public class IncomeData {
    private long id;
    private Date date;
    private String note;
    private float amount;

    public long getId() {
        return id;
    }
    public Date getDate()   {
        return date;
    }
    public String getNote() {
        return note;
    }
    public float getAmount()    {
        return amount;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setDate(Date date)  {
        this.date = date;
    }
    public void setNote(String note)    {
        this.note = note;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
}
