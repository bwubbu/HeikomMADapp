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
    ArrayList<ActivitiesModels> activitiesModels;

    public AA_ActivitiesAdapter(Context context, ArrayList<ActivitiesModels> activitiesModels){
        this.context = context;
        this.activitiesModels = activitiesModels;
    }


    @NonNull
    @Override
    public AA_ActivitiesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout and show the row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.points_rewards, parent, false);

        return new AA_ActivitiesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@  NonNull AA_ActivitiesAdapter.MyViewHolder holder, int position) {
        System.out.println(position + " : " + activitiesModels.get(position).toString());
        //assign value to view we created
        holder.activityRep.setText(activitiesModels.get(position).getActivity());
        holder.pointsRep.setText(activitiesModels.get(position).getPoints());

    }

    @Override
    public int getItemCount() {
        return activitiesModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing view from recycle view and put the data into the layout


        TextView activityRep,pointsRep;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            activityRep =  itemView.findViewById(R.id.textActions);
            pointsRep = itemView.findViewById(R.id.textShowPoints);

        }
    }
}
