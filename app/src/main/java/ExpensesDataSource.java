import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Kshitij on 9/12/2015.
 */

public class ExpensesDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] allColumns = { SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_DATE, SQLiteHelper.COLUMN_CATEGORY, SQLiteHelper.COLUMN_NOTE };

    public ExpensesDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ExpenseData createExpense(String catergory, Date date, String note)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COLUMN_DATE, simpleDateFormat.format(date));
        contentValues.put(SQLiteHelper.COLUMN_CATEGORY, catergory);
        contentValues.put(SQLiteHelper.COLUMN_NOTE, note);

        long insertId = database.insert(SQLiteHelper.TABLE_expenses, null,
                contentValues);

        Cursor cursor = database.query(SQLiteHelper.TABLE_expenses,
                allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ExpenseData expenseData = cursorToExpenseData(cursor);

        return expenseData;
    }

    public void deleteExpenseData(ExpenseData expenseData) {
        long id = expenseData.getId();
        System.out.println("Expense deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_expenses, SQLiteHelper.COLUMN_ID
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
        expenseData.setCategory(cursor.getString(2));
        expenseData.setNote(cursor.getString(3));
        return expenseData;
    }
}
