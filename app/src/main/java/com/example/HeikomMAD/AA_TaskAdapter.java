package com.example.HeikomMAD;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

public class AA_TaskAdapter extends RecyclerView.Adapter<AA_TaskAdapter.MyViewHolder> {
    Context context;
    ArrayList<TaskModel> taskModels;

    String userId;

    private PointAdditionListener pointAdditionListener;

    private HashSet<Integer> clickedPositions = new HashSet<>();

    private TaskCompletionListener taskCompletionListener;

    DataManager dataManager = DataManager.getInstance();
    private int completedTasksCount = 0;
    private Context applicationContext; // Add this variable

    public AA_TaskAdapter(Context context, ArrayList<TaskModel> taskModels, String userId) {
        this.context = context;
        this.taskModels = taskModels;
        this.userId = userId;
        this.applicationContext = context.getApplicationContext(); // Assign application context
    }

    @NonNull
    @Override
    public AA_TaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout and show the row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_task_card, parent, false);
        return new AA_TaskAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_TaskAdapter.MyViewHolder holder, int position) {
        TaskModel currentTask = taskModels.get(position);

        holder.textView.setText(currentTask.getTaskText());
        holder.imageView.setImageResource(currentTask.getImageFirst());
        holder.imageView2.setImageResource(currentTask.getImageSecond());
        holder.bindIntValue(currentTask.getPointsVal());


        boolean isClicked = currentTask.isClicked(context);
        holder.itemView.setAlpha(isClicked ? 0.5f : 1.0f);
        holder.itemView.setClickable(!isClicked);

        holder.itemView.setOnClickListener(v -> {
            if (!isClicked) {
                currentTask.setClicked(context, true);
                //updateCompletedTasksCount(); // Update completed task count in DataManager
                notifyDataSetChanged();

                int pointsToAdd = currentTask.getPointsVal();
                if (pointAdditionListener != null) {
                    pointAdditionListener.onAddPointClicked(position, pointsToAdd);
                    Log.d("AA_TaskAdapter", "Points added: " + pointsToAdd);
                }

                updateCompletedTasksCount();
                if (taskCompletionListener != null) {
                    //taskCompletionListener.onTaskCompleted(completedTasksCount);
                    Log.d("AA_TaskAdapter", "Task completed. Total completed tasks: " + completedTasksCount);
                }

            }
        });


    }




    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing view from recycler view and put data into layout

        ImageView imageView, imageView2;
        TextView textView, textView2, textView3;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.taskIcon1);
            imageView2 = itemView.findViewById(R.id.taskIcon2);
            textView = itemView.findViewById(R.id.TVTaskView);
            textView2 = itemView.findViewById(R.id.taskPoints);
            textView3 = itemView.findViewById(R.id.testCompletedTask);
        }

        public void bindIntValue(int value){
            textView2.setText(String.valueOf(value));
        }
    }
    public interface PointAdditionListener {
        void onAddPointClicked(int position, int pointsToAdd);
    }

    public void setPointAdditionListener(PointAdditionListener listener) {
        this.pointAdditionListener = listener;
    }

    public interface TaskCompletionListener {
        //void onTaskCompleted(int completedTasksCount);
    }

    public void setTaskCompletionListener(TaskCompletionListener listener) {
        this.taskCompletionListener = listener;
    }

    // Method to update completedTasksCount
    private void updateCompletedTasksCount() {
        completedTasksCount++;
        System.out.println("Completed Task Count:" + completedTasksCount);
        dataManager.saveCompletedTasksCount(applicationContext, completedTasksCount);
        notifyDataSetChanged(); // Notify RecyclerView about the data change
    }

    public void setCompletedTasksCount(int count) {
        this.completedTasksCount = count;
    }
}
