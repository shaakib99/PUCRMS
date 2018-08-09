package com.example.wahid.pucrms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class cardview2 extends RecyclerView.Adapter<cardview2.ViewHolder> {

    ArrayList<String> d;
    ArrayList<String> sect_ion;
    ArrayList<String> overlap;

    public cardview2(ArrayList<String> d, ArrayList<String> sect_ion, ArrayList<String> overlap) {
        this.d = d;
        this.sect_ion = sect_ion;
        this.overlap = overlap;
    }

    @NonNull
    @Override
    public cardview2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview2,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cardview2.ViewHolder holder, int position) {
        holder.din.setText(d.get(position));
        holder.overlapData.setText(overlap.get(position));
        holder.sectionData.setText(sect_ion.get(position));


    }

    @Override
    public int getItemCount() {
        return sect_ion.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView din,sectionData,overlapData;
        public ViewHolder(View itemView) {
            super(itemView);

            din = itemView.findViewById(R.id.overlapped_day);
            sectionData  = itemView.findViewById(R.id.current_subject_and_time);
            overlapData = itemView.findViewById(R.id.overlapped_subject_and_time);
        }
    }
}
