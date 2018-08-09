package com.example.wahid.pucrms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class cardview1 extends RecyclerView.Adapter<cardview1.ViewHolder>{

    ArrayList<String> Time;
    ArrayList<String> subjectAndRoom;
    public cardview1(ArrayList<String> time, ArrayList<String> subjectAndRoom) {
        this.Time =time;
        this.subjectAndRoom = subjectAndRoom;
    }

    @NonNull
    @Override
    public cardview1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.Time.setText(Time.get(position));
        holder.subjectAndRoom.setText(subjectAndRoom.get(position));
    }

    @Override
    public int getItemCount() {
        return Time.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView Time,subjectAndRoom;
        public ViewHolder(View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.time1);
            subjectAndRoom = itemView.findViewById(R.id.subject1);
        }
    }
}
