package me.apexjcl.todomoro.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import me.apexjcl.todomoro.entities.Task;

import java.util.Calendar;

/**
 * Creates a datepicker dialog
 * Created by apex on 30/03/17.
 */
public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int year;
    private int month;
    private int day;

    private Task task;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        year = task.year == null ? c.get(Calendar.YEAR) : task.year;
        month = task.month == null ? c.get(Calendar.MONTH) : task.month;
        day = task.dayOfMonth == null ? c.get(Calendar.DAY_OF_MONTH) : task.dayOfMonth;
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        task.setDueDate(year, month, dayOfMonth);
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
