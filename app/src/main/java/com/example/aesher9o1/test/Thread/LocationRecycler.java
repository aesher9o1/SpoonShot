package com.example.aesher9o1.test.Thread;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aesher9o1.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LocationRecycler extends RecyclerView.Adapter<LocationRecycler.Viewholder> {

    private List<LocationModel> locationModel;
    public LocationRecycler(List<LocationModel> locationModel){
       this.locationModel = locationModel;
       Log.w("aashis", ""+locationModel.size());
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recyclerview,viewGroup,false);
        return new LocationRecycler.Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
      viewholder.heading.setText(locationModel.get(i).getLocation());
      viewholder.location.setText(locationModel.get(i).getAddress());
    }

    @Override
    public int getItemCount() {
        return locationModel.size();
    }

    static class  Viewholder extends RecyclerView.ViewHolder {
        TextView heading, location;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            heading= itemView.findViewById(R.id.heading);
            location = itemView.findViewById(R.id.content);
        }
    }
}
