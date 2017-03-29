package me.apexjcl.todomoro.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.apexjcl.todomoro.R;
import me.apexjcl.todomoro.entities.Task;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by apex on 29/03/17.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<Task> tasks;

    public TaskListAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.taskTitle)
        private TextView title;
        @BindView(R.id.taskDescription)
        private TextView description;
        @BindView(R.id.taskDueDate)
        private TextView dueDate;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task t = tasks.get(position);
        if (t == null)
            return;
        holder.title.setText(t.title);
        holder.description.setText(t.description);
        DateTime dt = new DateTime(t.dueDate.getTime());
        holder.dueDate.setText(dt.toString());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

}
