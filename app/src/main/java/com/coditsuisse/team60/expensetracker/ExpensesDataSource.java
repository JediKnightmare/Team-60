package com.coditsuisse.team60.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kshitij on 9/12/2015.
 */

public class ExpensesDataSource {
    private SQLiteDatabase database;
    private ExpenseSQLiteHelper dbHelper;

    private String[] allColumns = { ExpenseSQLiteHelper.COLUMN_ID,
            ExpenseSQLiteHelper.COLUMN_DATE, ExpenseSQLiteHelper.COLUMN_AMT, ExpenseSQLiteHelper.COLUMN_CATEGORY, ExpenseSQLiteHelper.COLUMN_NOTE };

    public ExpensesDataSource(Context context) {
        dbHelper = new ExpenseSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ExpenseData createExpense(String category, Date date, String note, float amount)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpenseSQLiteHelper.COLUMN_DATE, simpleDateFormat.format(date));
        contentValues.put(ExpenseSQLiteHelper.COLUMN_AMT, amount);
        contentValues.put(ExpenseSQLiteHelper.COLUMN_CATEGORY, category);
        contentValues.put(ExpenseSQLiteHelper.COLUMN_NOTE, note);

        long insertId = database.insert(ExpenseSQLiteHelper.TABLE_expenses, null,
                contentValues);

        Cursor cursor = database.query(ExpenseSQLiteHelper.TABLE_expenses,
                allColumns, ExpenseSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ExpenseData expenseData = cursorToExpenseData(cursor);
        cursor.close();
        return expenseData;
    }

    public void deleteExpenseData(ExpenseData expenseData) {
        long id = expenseData.getId();
        System.out.println("Expense deleted with id: " + id);
        database.delete(ExpenseSQLiteHelper.TABLE_expenses, ExpenseSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    private ExpenseData cursorToExpenseData(Cursor cursor) {
        ExpenseData expenseData = new ExpenseData();
        expenseData.setId(cursor.getLong(0));
        try {
            expenseData.setDate(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(1)).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expenseData.setAmount(cursor.getFloat(2));
        expenseData.setCategory(cursor.getString(3));
        expenseData.setNote(cursor.getString(4));
        return expenseData;
    }

    public List<ExpenseData> getAllExpenses()   {
        List<ExpenseData> expenseDataList = new ArrayList<ExpenseData>();
        Cursor cursor = database.query(ExpenseSQLiteHelper.TABLE_expenses,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())   {
            ExpenseData expenseData = cursorToExpenseData(cursor);
            expenseDataList.add(expenseData);
            cursor.moveToNext();
        }

        cursor.close();
        return expenseDataList;
    }
}