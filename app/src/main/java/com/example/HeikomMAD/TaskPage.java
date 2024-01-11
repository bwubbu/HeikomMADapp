package com.example.HeikomMAD;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class TaskPage extends Fragment implements AA_TaskAdapter.PointAdditionListener, DataManager.ActivitySavedListener, AA_TaskAdapter.OnPointsAddedListener {
    ArrayList<TaskModel> taskModel = new ArrayList<>();

    AA_TaskAdapter adapter;

    //private ArrayList<ActivitiesModels> activitiesModels = new ArrayList<>();

    private FirebasePointManager firebasePointManager;
    DataManager dataManager = DataManager.getInstance();

    //for progress bar
    private int CurrentProgress = 0;
    private ProgressBar progressBar;
    private Button startProgress;

    private ImageView headerProfilepic;
    private TextView headerUser;

    private String username;
    public TaskPage() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TaskPage newInstance() {
        TaskPage fragment = new TaskPage();
        return fragment;
    }
    @Override
    public void onPointsAdded(int addedPoints) {
        fetchAndUpdateUserPoints(); // This will fetch the latest points and update the progress bar
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set this fragment as the observer for activity saved events
        DataManager.getInstance().setActivitySavedListener(this);
        firebasePointManager = new FirebasePointManager();
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
        headerUser = view.findViewById(R.id.headerUser);
        headerProfilepic = view.findViewById(R.id.headerProfilepic);


        setTaskModel();



         System.out.println("Task Model Size:" + taskModel.size());
         FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();;
         String userId = currentUser.getUid();
         if(currentUser!=null){
             showUserProfile(currentUser);
         }


        adapter = new AA_TaskAdapter(requireContext(), taskModel, userId);
        adapter.setPointAdditionListener(this); // Set the listener to this fragment
        adapter.setOnPointsAddedListener(this); // Set the listener
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progressBarTP);
        progressBar.setMax(30000);

        setActivitiesModel();
        adapter.notifyDataSetChanged();
        // Fetch and set initial user points
        fetchAndUpdateUserPoints();

        ImageView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate((R.id.rewardReport));
        });

        return view;

    }


    public void onAddPointClicked(int position, int pointsToAdd) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebasePointManager.addPointsToUser(userId, pointsToAdd, new FirebasePointManager.PointUpdateListener() {
            @Override
            public void onPointUpdateSuccess() {
                // Points successfully added, now fetch the updated points
                fetchAndUpdateUserPoints();
            }

            @Override
            public void onPointUpdateFailure(String message) {
                // Handle failure to add points
            }
        });

        // Update the clicked status for the respective task
        TaskModel task = taskModel.get(position);
        task.setClicked(true); // Assuming the task was clicked
        adapter.notifyItemChanged(position); // Update the RecyclerView item
    }

    private void fetchAndUpdateUserPoints() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Fetch the current user ID
        firebasePointManager.fetchPointsForUser(userId, new FirebasePointManager.PointFetchListener() {
            @Override
            public void onPointFetchSuccess(int points) {
                progressBar.setProgress(points); // Update the progress bar
            }

            @Override
            public void onPointFetchFailure(String message) {
                Log.e("TaskPage", "Error fetching points: " + message);
                // Handle error
            }
        });
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
        // Refresh the progress bar when fragment resumes
        fetchAndUpdateUserPoints();
        if (adapter != null) {
            fetchAndUpdateUserPoints(); // Fetch and update user points when fragment is resumed
            int completedTaskCount = DataManager.getInstance().getCompletedTasksCount(getContext());
            adapter.setCompletedTasksCount(completedTaskCount);
            adapter.notifyDataSetChanged();
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    username = firebaseUser.getDisplayName();
                    headerUser.setText("Welcome, " + username + "!");
                    Uri uri = firebaseUser.getPhotoUrl();
                    if (uri != null && headerProfilepic != null && getContext() != null) {
                        Picasso.with(getContext()).load(uri).into(headerProfilepic);
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}