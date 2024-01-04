package com.example.HeikomMAD;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;



public class TaskPage extends Fragment implements AA_TaskAdapter.PointAdditionListener, DataManager.ActivitySavedListener {
    ArrayList<TaskModel> taskModel = new ArrayList<>();

    AA_TaskAdapter adapter;

    //private ArrayList<ActivitiesModels> activitiesModels = new ArrayList<>();


    DataManager dataManager = DataManager.getInstance();

    //for progress bar
    private int CurrentProgress = 0;
    private ProgressBar progressBar;
    private Button startProgress;
    public TaskPage() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TaskPage newInstance() {
        TaskPage fragment = new TaskPage();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set this fragment as the observer for activity saved events
        DataManager.getInstance().setActivitySavedListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DataManager.getInstance().setActivitySavedListener(this);
    }

    @Override
    public void onActivitySaved() {
        // Define the behavior when an activity is saved
        // This method must be implemented as part of the ActivitySavedListener interface
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_page, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.taskRecycleViewTP);

        setTaskModel();


        System.out.println("Task Model Size:" + taskModel.size());
        String userId = "userId";
        //AA_TaskAdapter adapter = new AA_TaskAdapter(getActivity(), taskModel, userId);

        adapter = new AA_TaskAdapter(requireContext(), taskModel, userId);
        adapter.setPointAdditionListener(this); // Set the listener to this fragment
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progressBarTP);
        //final int[] userPoints = {PointManager.getPoints(requireContext(), "userId")};
        int userPoints = PointManager.getPoints(requireContext(), "userId"); // Fetch the user's points as an integer
        progressBar.setMax(30000);

        dataManager.saveTotalTask(taskModel.size());
        setActivitiesModel();
        adapter.notifyDataSetChanged();

        return view;
    }


    public void onAddPointClicked(int position, int pointsToAdd) {
        // Implement logic to add points for the user
        String userId = "userId"; // Simulating a user ID for testing purposes
        PointManager.addPoints(requireContext(), userId, pointsToAdd);

        // Update the clicked status for the respective task
        TaskModel task = taskModel.get(position);
        task.setClicked(true); // Assuming the task was clicked
        adapter.notifyItemChanged(position); // Update the RecyclerView item

        // Update the progress bar with the new points
        updateProgressBar();
    }


    private void updateProgressBar() {
        String userId = "userId"; // Simulating a user ID for testing purposes
        int userPoints = PointManager.getPoints(requireContext(), userId);
        progressBar.setProgress(userPoints);
    }


    private void setTaskModel(){
        taskText text = new taskText();

        for(int i=0;i< text.taskText.length;i++){
            taskModel.add(new TaskModel(text.taskText[i],text.icon[i],text.points[i]));
        }
    }

    //fetch activities from task and then put it into the the activitiesModel
    private void setActivitiesModel() {
        for (TaskModel task : taskModel) {
            boolean isClicked = task.isClicked(requireContext());

            if (isClicked && !DataManager.getInstance().isActivityAlreadySaved(task.getTaskText())) { // Check if already saved
                dataManager.saveActivity(task.getTaskText(), String.valueOf(task.getPointsVal()));
            }

        }
    }




    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            int completedTaskCount = DataManager.getInstance().getCompletedTasksCount(this.getContext());
            adapter.setCompletedTasksCount(completedTaskCount);
            adapter.notifyDataSetChanged();
        }
    }
}