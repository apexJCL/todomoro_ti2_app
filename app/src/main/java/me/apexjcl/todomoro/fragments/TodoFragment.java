package me.apexjcl.todomoro.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
public class TodoFragment extends Fragment implements FloatingActionButton.OnClickListener, FragmentManager.OnBackStackChangedListener {

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
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo, container, false);
        ButterKnife.bind(this, v);
        // Inflate the layout for this fragment
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TaskListAdapter adapter = new TaskListAdapter(Task.fetchAll());
        adapter.setFragmentManager(getFragmentManager());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else fab.show();
            }
        });
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
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackStackChanged() {
        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_fragment);
        fab.setOnClickListener((View.OnClickListener) f);
        if (f instanceof NewTaskFragment)
            fab.setImageResource(R.drawable.ic_done);
        else if (f instanceof DayViewFragment || f instanceof TodoFragment)
            fab.setImageResource(R.drawable.ic_add);
    }
}
