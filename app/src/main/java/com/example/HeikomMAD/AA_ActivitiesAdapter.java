package com.example.HeikomMAD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AA_ActivitiesAdapter extends RecyclerView.Adapter<AA_ActivitiesAdapter.MyViewHolder> {
    Context context;
    ArrayList<CompletedTask> completedTasks;

    public AA_ActivitiesAdapter(Context context, ArrayList<CompletedTask> completedTasks){
        this.context = context;
        this.completedTasks = completedTasks;
    }

    @NonNull
    @Override
    public AA_ActivitiesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.points_rewards, parent, false);
        return new AA_ActivitiesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_ActivitiesAdapter.MyViewHolder holder, int position) {
        CompletedTask task = completedTasks.get(position);
        holder.activityRep.setText(task.getTaskName());
        holder.pointsRep.setText(String.valueOf(task.getPoints()));
    }

    @Override
    public int getItemCount() {
        return completedTasks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView activityRep, pointsRep;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            activityRep = itemView.findViewById(R.id.textActions);
            pointsRep = itemView.findViewById(R.id.textShowPoints);
        }
    }

    public void updateActivities(ArrayList<CompletedTask> newTasks) {
        this.completedTasks.clear();
        this.completedTasks.addAll(newTasks);
        notifyDataSetChanged(); // Notify the adapter to refresh the data
    }

}

