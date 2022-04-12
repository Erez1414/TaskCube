package com.example.android.productivecube;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrefAdapter extends RecyclerView.Adapter<PrefAdapter.PrefAdapterViewHolder> {

    final private ItemClickListener itemClickListener;
    private List<Task> tasksEntries;
    final static public Map<Color,Integer> colors = new HashMap<Color,Integer>(){{
        put(Color.BLUE, R.color.blue);
        put(Color.GREEN, R.color.green);
        put(Color.YELLOW, R.color.yellow);
        put(Color.PURPLE, R.color.pink);
        put(Color.WHITE, R.color.white);
        put(Color.CYAN, R.color.cyan);
        put(Color.RED, R.color.red);
        //todo: add all colors
    }};

    public PrefAdapter(Context context, ItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setTasksEntries(List<Task> tasksEntries){
        this.tasksEntries = tasksEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrefAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context parenContext = parent.getContext();
        int layoutForTaskItem = R.layout.pref_item;
        LayoutInflater inflater = LayoutInflater.from(parenContext);

        View view = inflater.inflate(layoutForTaskItem, parent, false);
        return new PrefAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrefAdapterViewHolder holder, int position) {
        Task task = tasksEntries.get(position);
        Log.d("Soemthing", task.getName());
        holder.mColorBlock.setBackgroundResource(colors.get(task.getColor()));
        holder.mTaskName.setText(task.getName());
        holder.mTaskTime.setText(task.getTime().toString());
    }

    @Override
    public int getItemCount() {
        if (tasksEntries == null){
            return 0;
        }
        return tasksEntries.size();
    }

    public interface ItemClickListener{
        void onItemClickListener(Task task);
    }

    public class PrefAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mColorBlock;
        public final TextView mTaskName;
        public final TextView mTaskTime;

        public PrefAdapterViewHolder(View view){
            super(view);
            mColorBlock = (TextView) view.findViewById(R.id.tv_color_block);
            mTaskName = (TextView) view.findViewById(R.id.tv_task_name);
            mTaskTime = (TextView) view.findViewById(R.id.tv_task_time);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Task task = tasksEntries.get(getAdapterPosition());
            itemClickListener.onItemClickListener(task);
        }
    }
}
