package com.kanwaljeetsm.covidstats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    List<String> mStateList, mStateNums1, mStateNums2, mStateNums3, mStateNums4;

    public RecyclerAdapter(List<String> mStateList, List<String> mStateNums1, List<String> mStateNums2, List<String> mStateNums3, List<String> mStateNums4) {
        this.mStateList = mStateList;
        this.mStateNums1 = mStateNums1;
        this.mStateNums2 = mStateNums2;
        this.mStateNums3 = mStateNums3;
        this.mStateNums4 = mStateNums4;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_main, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textView1.setText(mStateList.get(position));
        holder.textView2.setText(mStateNums1.get(position));
        holder.textView3.setText(mStateNums2.get(position));
        holder.textView4.setText(mStateNums3.get(position));
        holder.textView8.setText(mStateNums4.get(position));
    }

    @Override
    public int getItemCount() {
        return mStateNums1.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView textView1, textView2, textView3, textView4, textView8;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView8 = itemView.findViewById(R.id.textView8);
        }
    }
}

