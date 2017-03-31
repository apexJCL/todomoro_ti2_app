package me.apexjcl.todomoro.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.apexjcl.todomoro.R;
import me.apexjcl.todomoro.adapters.TaskListAdapter;
import me.apexjcl.todomoro.entities.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment implements FloatingActionButton.OnClickListener {

    @BindView(R.id.recyclerTaskView)
    RecyclerView recyclerView;

    private FloatingActionButton fab;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo, container, false);
        ButterKnife.bind(this, v);
        // Inflate the layout for this fragment
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TaskListAdapter(Task.fetchAll()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Load tasks from realm
    }

    @Override
    public void onClick(View v) {
        fab.setImageResource(R.drawable.ic_done);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, new NewTaskFragment());
        ft.commit();
    }
}
