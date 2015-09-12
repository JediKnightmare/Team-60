package com.coditsuisse.team60.expensetracker;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Samarth on 13-09-2015.
 */
public class ListTransactionAdapter extends ArrayAdapter<IncomeData> {
    public ListTransactionAdapter(Context context, int resource) { super(context, resource); }

    public static class ViewHolder {
        public final TextView dateView;
        public final TextView noteView;
        public final TextView amountView;

        public ViewHolder(View view) {
            dateView = (TextView) view.findViewById(R.id.list_item_date);
            noteView = (TextView) view.findViewById(R.id.list_item_note);
            amountView = (TextView) view.findViewById(R.id.list_item_amount);
        }
    }

    
}
