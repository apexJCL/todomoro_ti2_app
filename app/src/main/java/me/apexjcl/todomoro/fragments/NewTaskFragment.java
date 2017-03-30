package me.apexjcl.todomoro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.apexjcl.todomoro.R;
import me.apexjcl.todomoro.entities.Task;

/**
 * Created by apex on 30/03/17.
 */
public class NewTaskFragment extends Fragment {

    public static String TAG = "new_task";
    private Task task;

    @BindView(R.id.taskTitle)
    EditText taskTitle;
    @BindView(R.id.taskDescription)
    EditText taskDescription;

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
    }

    @OnClick(R.id.calendarToggle)
    void dueDate() {
        DateFragment dateFragment = new DateFragment();
        dateFragment.setTask(task);
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    public boolean saveTask() {
        if (taskTitle.getText().toString().length() == 0 || taskTitle.getText().toString().matches("\\s*")) {
            taskTitle.setError(getString(R.string.emptyTitle));
            return false;
        }
        task.title = taskTitle.getText().toString();
        task.description = taskDescription.getText().toString();
        task.save();
        return true;
    }
}
