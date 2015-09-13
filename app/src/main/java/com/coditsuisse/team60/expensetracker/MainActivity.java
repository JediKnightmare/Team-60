package com.coditsuisse.team60.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IncomeDataSource incomeDataSource;
    private ExpensesDataSource expensesDataSource;
    private float totalIncome;
    private float totalExpenses;
    private float totalLiabilities;
    private float savingsTarget;
    private static TextView totalIncomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incomeDataSource = new IncomeDataSource(this);
        incomeDataSource.open();
        totalIncome = incomeDataSource.getTotalIncomesCurrent();
        incomeDataSource.close();

        Log.d("LOG", String.valueOf(totalIncome));

        totalIncomeText = (TextView) findViewById(R.id.totalIncomeText);
        totalIncomeText.setText("null");
        //totalIncomeText.setText(String.valueOf(totalIncome));

        expensesDataSource = new ExpensesDataSource(this);
        expensesDataSource.open();
        totalExpenses = expensesDataSource.getExpensesCurrent();
        expensesDataSource.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setContentView(R.layout.activity_main);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_add_expense) {
            startActivity(new Intent(this, MainActivity2.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}