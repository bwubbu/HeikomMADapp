package com.example.HeikomMAD;

/*
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.PointViewHolder> {

    private List<PointItems> pointItemsList;

    public PointsAdapter(List<PointItems> pointItemsList) {
        this.pointItemsList = pointItemsList;
    }

    @NonNull
    @Override
    public PointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_rewards, parent, false);
        System.out.println("ocvh success");
        return new PointViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PointViewHolder holder, int position) {
        PointItems pointItems = pointItemsList.get(position);
        holder.points.setText(pointItems.getPoint());
        System.out.println("obvh success");
    }

    @Override
    public int getItemCount() {
        return pointItemsList.size();
    }

    class PointViewHolder extends RecyclerView.ViewHolder {
        TextView points;

        public PointViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.textViewPoint);
            System.out.println("pvh success");
        }
    }
}
*/