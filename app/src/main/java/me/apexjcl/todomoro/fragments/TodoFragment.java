package me.apexjcl.todomoro.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.apexjcl.todomoro.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {


    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    public TodoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo, container, false);
        ButterKnife.bind(this, v);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toggleProgressBar();
        // Load tasks from realm
    }

    private void toggleProgressBar() {
        progressBar.setVisibility(progressBar.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}
