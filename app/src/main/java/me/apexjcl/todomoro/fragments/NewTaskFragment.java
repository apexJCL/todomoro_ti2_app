package me.apexjcl.todomoro.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import me.apexjcl.todomoro.R;
import me.apexjcl.todomoro.entities.Task;

import static android.support.design.widget.FloatingActionButton.OnClickListener;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by apex on 30/03/17.
 */
public class NewTaskFragment extends Fragment
        implements OnClickListener, Realm.Transaction.OnSuccess, Realm.Transaction.OnError,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static String TAG = "new_task";
    private Task task;

    @BindView(R.id.taskTitle)
    EditText taskTitle;
    @BindView(R.id.taskDescription)
    EditText taskDescription;
    @BindView(R.id.formLayout)
    RelativeLayout formLayout;
    @BindView(R.id.progressSpinner)
    ProgressBar progressBar;

    FloatingActionButton fab;

    DateFragment dateFragment;
    TimeFragment timeFragment;

    public NewTaskFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_task, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task = new Task();
        dateFragment = new DateFragment();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @OnClick(R.id.calendarToggle)
    void dueDate() {
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.timeToggle)
    void dueTime() {
        if (timeFragment == null)
            timeFragment = new TimeFragment();
        timeFragment.show(getFragmentManager(), TimeFragment.TAG);
    }

    private boolean canSave() {
        if (taskTitle.getText().toString().length() == 0 || taskTitle.getText().toString().matches("\\s*")) {
            taskTitle.setError(getString(R.string.emptyTitle));
            return false;
        }
        // Update task values
        task.title = taskTitle.getText().toString();
        task.description = taskDescription.getText().toString().isEmpty() ? "" : taskDescription.getText().toString();
        task.year = dateFragment.year;
        task.month = dateFragment.month;
        task.dayOfMonth = dateFragment.day;
        task.hour = timeFragment.hour;
        task.minutes = timeFragment.minutes;
        return true;
    }

    @Override
    public void onClick(View v) {
        fab = (FloatingActionButton) v;
        if (!canSave())
            return;
        v.setVisibility(GONE);
        toggleProgressbar();
        // Save to realm
        Task.save(task, this);
    }

    private void toggleProgressbar() {
        progressBar.setVisibility(progressBar.getVisibility() == GONE ? VISIBLE : GONE);
        formLayout.setVisibility(formLayout.getVisibility() == GONE ? VISIBLE : GONE);
    }

    @Override
    public void onSuccess() {
        // TODO check nullPointer here?
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, new TodoFragment());
        ft.commit();
        fab.setImageResource(R.drawable.ic_add);
        fab.setVisibility(VISIBLE);
    }

    @Override
    public void onError(Throwable error) {
        error.printStackTrace();
        Toast.makeText(getContext(), R.string.errorSaving, Toast.LENGTH_LONG).show();
        toggleProgressbar();
        fab.setVisibility(VISIBLE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
