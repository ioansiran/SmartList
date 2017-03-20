package com.akitektuo.smartlist.util;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akitektuo.smartlist.R;
import com.akitektuo.smartlist.activity.ListActivity;
import com.akitektuo.smartlist.database.DatabaseHelper;

import java.util.ArrayList;

import static com.akitektuo.smartlist.util.Constant.COLOR_BLACK;
import static com.akitektuo.smartlist.util.Constant.COLOR_BLUE;
import static com.akitektuo.smartlist.util.Constant.COLOR_GREEN;
import static com.akitektuo.smartlist.util.Constant.COLOR_ORANGE;
import static com.akitektuo.smartlist.util.Constant.COLOR_RED;
import static com.akitektuo.smartlist.util.Constant.COLOR_YELLOW;
import static com.akitektuo.smartlist.util.Constant.KEY_AUTO_FILL;
import static com.akitektuo.smartlist.util.Constant.KEY_COLOR;
import static com.akitektuo.smartlist.util.Constant.KEY_RECOMMENDATIONS;
import static com.akitektuo.smartlist.util.Constant.handler;
import static com.akitektuo.smartlist.util.Constant.preference;

/**
 * Created by AoD Akitektuo on 14-Mar-17.
 */

public class ListAdapter extends ArrayAdapter<ListItem> {

    private Context context;
    private ListItem[] items;
    private DatabaseHelper database;

    public ListAdapter(Context context, ListItem[] listItems) {
        super(context, R.layout.item_list, listItems);
        this.context = context;
        items = listItems;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        database = new DatabaseHelper(context);
        final ListItem item = items[position];
        holder.textNumber.setText(changeNumber(item.getNumber()));
        holder.editValue.setText(item.getValue());
        holder.textCurrency.setText(item.getCurrency());
        holder.editAutoProduct.setText(item.getProduct());
        switch (item.getButtonType()) {
            case 0:
                holder.buttonSave.setVisibility(View.VISIBLE);
                holder.buttonDelete.setVisibility(View.GONE);
                break;
            case 1:
                holder.buttonSave.setVisibility(View.GONE);
                holder.buttonDelete.setVisibility(View.VISIBLE);
                break;
            default:
                holder.buttonSave.setVisibility(View.VISIBLE);
                holder.buttonDelete.setVisibility(View.VISIBLE);
        }
        refreshList(holder.editAutoProduct);
        holder.editAutoProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                if (holder.editValue.getText().toString().isEmpty() && preference.getPreferenceBoolean(KEY_AUTO_FILL)) {
                    handler.post(new Runnable() {
                        public void run() {
                            holder.editValue.setText(String.valueOf(database.getPriceForProduct(database.getReadableDatabase(), adapterView.getItemAtPosition(i).toString())));
                        }
                    });
                }
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Item deleted...", Toast.LENGTH_SHORT).show();
                database.deleteList(database.getWritableDatabase(), position + 1);
                updateDatabase(database, position);
                ListActivity.refreshList(context);
            }
        });
        holder.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.editValue.getText().toString().isEmpty() || holder.editAutoProduct.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    if (position + 1 == database.getListNumberNew()) {
                        database.addList(database.getWritableDatabase(), database.getListNumberNew(),
                                holder.editValue.getText().toString(), holder.editAutoProduct.getText().toString());
                        ListActivity.refreshList(context);
                        Toast.makeText(context, "Item saved...", Toast.LENGTH_SHORT).show();
                    } else {
                        database.updateList(database.getWritableDatabase(), position + 1, item.getNumber(),
                                holder.editValue.getText().toString(), holder.editAutoProduct.getText().toString());
                        ListActivity.refreshList(context);
                        Toast.makeText(context, "Item updated...", Toast.LENGTH_SHORT).show();
                    }
                    database.updatePrices(database.getWritableDatabase(), holder.editAutoProduct.getText().toString(),
                            holder.editValue.getText().toString());
                    refreshList(holder.editAutoProduct);
                }
            }
        });
        holder.editValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (item.getButtonType() != 0) {
                    holder.buttonSave.setVisibility(View.VISIBLE);
                    holder.buttonDelete.setVisibility(View.VISIBLE);
                } else {
                    holder.buttonDelete.setVisibility(View.GONE);
                }
            }
        });
        holder.editAutoProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (item.getButtonType() != 0) {
                    holder.buttonSave.setVisibility(View.VISIBLE);
                    holder.buttonDelete.setVisibility(View.VISIBLE);
                } else {
                    holder.buttonDelete.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private String changeNumber(int num) {
        if (num < 10) {
            return " " + num;
        }
        return String.valueOf(num);
    }

    private void updateDatabase(DatabaseHelper database, int position) {
        for (int i = position + 2; i < database.getListNumberNew(); i++) {
            Cursor cursor = database.getListForNumber(database.getReadableDatabase(), i);
            if (cursor.moveToFirst()) {
                database.updateList(database.getWritableDatabase(), cursor.getInt(0), cursor.getInt(0) - 1,
                        cursor.getString(1), cursor.getString(2));
            }
            cursor.close();
        }
    }

    private class ViewHolder {
        TextView textNumber;
        EditText editValue;
        TextView textCurrency;
        AutoCompleteTextView editAutoProduct;
        Button buttonDelete;
        Button buttonSave;

        ViewHolder(View view) {
            textNumber = (TextView) view.findViewById(R.id.text_item_number);
            editValue = (EditText) view.findViewById(R.id.edit_item_value);
            textCurrency = (TextView) view.findViewById(R.id.text_item_currency);
            editAutoProduct = (AutoCompleteTextView) view.findViewById(R.id.edit_auto_item_product);
            buttonDelete = (Button) view.findViewById(R.id.button_delete);
            buttonSave = (Button) view.findViewById(R.id.button_save);
            switch (preference.getPreferenceString(KEY_COLOR)) {
                case COLOR_BLUE:
                    textNumber.setTextColor(context.getResources().getColor(R.color.colorPrimaryBlue));
                    buttonDelete.setBackground(context.getDrawable(R.drawable.delete_blue));
                    buttonSave.setBackground(context.getDrawable(R.drawable.save_blue));
                    break;
                case COLOR_YELLOW:
                    textNumber.setTextColor(context.getResources().getColor(R.color.colorPrimaryYellow));
                    buttonDelete.setBackground(context.getDrawable(R.drawable.delete_yellow));
                    buttonSave.setBackground(context.getDrawable(R.drawable.save_yellow));
                    break;
                case COLOR_RED:
                    textNumber.setTextColor(context.getResources().getColor(R.color.colorPrimaryRed));
                    buttonDelete.setBackground(context.getDrawable(R.drawable.delete_red));
                    buttonSave.setBackground(context.getDrawable(R.drawable.save_red));
                    break;
                case COLOR_GREEN:
                    textNumber.setTextColor(context.getResources().getColor(R.color.colorPrimaryGreen));
                    buttonDelete.setBackground(context.getDrawable(R.drawable.delete_green));
                    buttonSave.setBackground(context.getDrawable(R.drawable.save_green));
                    break;
                case COLOR_ORANGE:
                    textNumber.setTextColor(context.getResources().getColor(R.color.colorPrimaryOrange));
                    buttonDelete.setBackground(context.getDrawable(R.drawable.delete_orange));
                    buttonSave.setBackground(context.getDrawable(R.drawable.save_orange));
                    break;
                case COLOR_BLACK:
                    textNumber.setTextColor(context.getResources().getColor(R.color.colorPrimaryBlack));
                    buttonDelete.setBackground(context.getDrawable(R.drawable.delete_black));
                    buttonSave.setBackground(context.getDrawable(R.drawable.save_black));
                    break;
            }
        }
    }

    private void refreshList(final AutoCompleteTextView autoCompleteTextView) {
        if (preference.getPreferenceBoolean(KEY_RECOMMENDATIONS)) {
            handler.post(new Runnable() {
                public void run() {
                    ArrayList<String> list = new ArrayList<>();
                    Cursor cursor = database.getUsage(database.getReadableDatabase());
                    if (cursor.moveToFirst()) {
                        do {
                            list.add(cursor.getString(0));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
                    autoCompleteTextView.setAdapter(adapter);
                }
            });
        }
    }
}
