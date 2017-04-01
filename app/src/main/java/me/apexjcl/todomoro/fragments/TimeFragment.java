package me.apexjcl.todomoro.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by apex on 30/03/17.
 */
public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "timePicker";

    Integer hour;
    Integer minutes;

    public TimeFragment() {
        super();
        Calendar c = Calendar.getInstance();
        hour = hour == null ? c.get(Calendar.HOUR_OF_DAY) : hour;
        minutes = minutes == null ? c.get(Calendar.MINUTE) : minutes;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), this, hour, minutes, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minutes = minute;
    }
}
