package me.apexjcl.todomoro.fragments;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import me.apexjcl.todomoro.R;
import me.apexjcl.todomoro.entities.Task;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DayViewFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, View.OnClickListener {


    public static String TAG = "DayViewFragment";

    @BindView(R.id.weekView)
    WeekView weekView;

    public DayViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        // Init WeekView
        weekView.setMonthChangeListener(this);
        weekView.setOnEventClickListener(this);
        weekView.setEventLongPressListener(this);
        weekView.goToHour(DateTime.now().getHourOfDay());
        return v;
    }

    public WeekView getWeekView() {
        return weekView;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return new ArrayList<WeekViewEvent>();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "Toasting from Dayview", Toast.LENGTH_LONG).show();
    }

    public void updateWeekview(List<Task> tasks) {
        for (Task task : tasks){
            // TODO create events and add to weekView
        }
    }
}
