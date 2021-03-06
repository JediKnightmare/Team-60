package com.coditsuisse.team60.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kshitij on 9/12/2015.
 */

public class IncomeDataSource {
    private SQLiteDatabase database;
    private IncomeSQLiteHelper dbHelper;

    private String[] allColumns = { IncomeSQLiteHelper.COLUMN_ID,
            IncomeSQLiteHelper.COLUMN_AMT ,IncomeSQLiteHelper.COLUMN_DATE, IncomeSQLiteHelper.COLUMN_NOTE };

    public IncomeDataSource(Context context)    {
        dbHelper = new IncomeSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public IncomeData createIncome(Date date, String note, float amount)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put(IncomeSQLiteHelper.COLUMN_DATE, simpleDateFormat.format(date));
        contentValues.put(IncomeSQLiteHelper.COLUMN_AMT, amount);
        contentValues.put(IncomeSQLiteHelper.COLUMN_NOTE, note);

        long insertId = database.insert(IncomeSQLiteHelper.TABLE_income, null,
                contentValues);

        Cursor cursor = database.query(IncomeSQLiteHelper.TABLE_income,
                allColumns, IncomeSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        IncomeData incomeData = cursorToIncomeData(cursor);
        return incomeData;
    }

    public void deleteIncomeData(IncomeData incomeData) {
        long id = incomeData.getId();
        System.out.println("Income deleted with id: " + id);
        database.delete(IncomeSQLiteHelper.TABLE_income, IncomeSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    private IncomeData cursorToIncomeData(Cursor cursor) {
        IncomeData incomeData = new IncomeData();
        incomeData.setId(cursor.getLong(0));
        try {
            incomeData.setDate(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(2)).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        incomeData.setAmount(cursor.getFloat(1));
        incomeData.setNote(cursor.getString(3));
        return incomeData;
    }

    public List<IncomeData> getAllIncomes()   {
        List<IncomeData> incomeDataList = new ArrayList<IncomeData>();
        Cursor cursor = database.query(IncomeSQLiteHelper.TABLE_income,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())   {
            IncomeData incomeData = cursorToIncomeData(cursor);
            incomeDataList.add(incomeData);
            cursor.moveToNext();
        }

        cursor.close();
        return incomeDataList;
    }

    public float getTotalIncomesCurrent()  {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        java.util.Date date = new java.util.Date();
        float totalIncome = 0;

        String sql = "SELECT * from " + IncomeSQLiteHelper.TABLE_income +
                " where " + IncomeSQLiteHelper.COLUMN_DATE + " LIKE '____-"+ dateFormat.format(date) + "%';";
        Cursor cursor = database.rawQuery(sql, null);

        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                IncomeData incomeData = cursorToIncomeData(cursor);
                totalIncome += incomeData.getAmount();
                cursor.moveToNext();
            }
        }

        cursor.close();

        return totalIncome;
    }
}
