package me.apexjcl.todomoro.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.apexjcl.todomoro.R;
import me.apexjcl.todomoro.dialogs.DescriptionDialog;
import me.apexjcl.todomoro.entities.Task;

import java.util.List;

/**
 * Created by apex on 29/03/17.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<Task> tasks;
    private FragmentManager fragmentManager;

    public TaskListAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnLongClickListener {

        private Task task;
        @BindView(R.id.taskTitle)
        TextView title;
        @BindView(R.id.readDescriptionButton)
        ImageButton readDescription;
        @BindView(R.id.doneButton)
        ImageButton doneButton;
        private FragmentManager fg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
            readDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DescriptionDialog dialog = new DescriptionDialog();
                    dialog.setContent(task.description);
                    dialog.show(fg, "");
                }
            });
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public void setFragmentManager(FragmentManager fg) {
            this.fg = fg;
        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(v.getContext(), task.description != null ? task.description : "", Toast.LENGTH_LONG).show();
            return false;
        }

        public void updateFields() {
            title.setText(task.title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.setFragmentManager(fragmentManager);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task t = tasks.get(position);
        if (t == null)
            return;
        holder.setTask(t);
        holder.updateFields();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}
