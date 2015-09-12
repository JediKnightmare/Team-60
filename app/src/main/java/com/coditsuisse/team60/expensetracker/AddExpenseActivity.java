package com.coditsuisse.team60.expensetracker;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    private String category = "Others";
    private ExpensesDataSource expensesDataSource;
    private Date date;
    private String note;
    float amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expensesDataSource = new ExpensesDataSource(this);
        expensesDataSource.open();

        Button button = (Button) findViewById(R.id.add_expense_button);
        button.setOnClickListener(this);
        EditText dateField = (EditText) findViewById(R.id.expense_date);
        dateField.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_expense, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_expense_button:
                final EditText amountField = (EditText) findViewById(R.id.expense_amount);
                amount = Float.parseFloat(amountField.getText().toString());
                final EditText noteField = (EditText) findViewById(R.id.expense_note);
                note = noteField.getText().toString();
                final EditText dateField = (EditText) findViewById(R.id.expense_date);
                String dateString = dateField.getText().toString();

                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                format.setTimeZone(TimeZone.getDefault());
                try {
                    date = new java.sql.Date(format.parse(dateString).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                expensesDataSource.createExpense(category, date, note, amount);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.expense_date:
                showDatePickerDialog(view);
                break;
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.expense_foodndrinks:
                if (checked)
                    category = "Food and Drinks";
                break;
            case R.id.expense_health:
                if (checked)
                    category = "Health";
                break;
            case R.id.expense_leisure:
                if (checked)
                    category = "Leisure";
                break;
            case R.id.expense_transportation:
                if (checked)
                    category = "Transportation";
                break;
            case R.id.expense_others:
                if (checked)
                    category = "Others";
                break;
        }
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
