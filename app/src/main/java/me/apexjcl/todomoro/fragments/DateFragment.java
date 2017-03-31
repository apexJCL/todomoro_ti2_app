package me.apexjcl.todomoro.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Creates a datepicker dialog
 * Created by apex on 30/03/17.
 */
public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "datePicker";

    Integer year;
    Integer month;
    Integer day;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        year = year == null ? c.get(Calendar.YEAR) : year;
        month = month == null ? c.get(Calendar.MONTH) : month;
        day = day == null ? c.get(Calendar.DAY_OF_MONTH) : day;
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
    }

}
