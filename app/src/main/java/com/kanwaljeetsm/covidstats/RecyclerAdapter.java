package com.kanwaljeetsm.covidstats;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    List<String> mStateList, mStateNums1, mStateNums2, mStateNums3, mStateNums4, mStateNums5, mStateNums6, mStateNums7;

    public RecyclerAdapter(List<String> mStateList, List<String> mStateNums1, List<String> mStateNums2, List<String> mStateNums3, List<String> mStateNums4,
                           List<String> mStateNums5, List<String> mStateNums6, List<String> mStateNums7) {
        this.mStateList = mStateList;
        this.mStateNums1 = mStateNums1;
        this.mStateNums2 = mStateNums2;
        this.mStateNums3 = mStateNums3;
        this.mStateNums4 = mStateNums4;
        this.mStateNums5 = mStateNums5;
        this.mStateNums6 = mStateNums6;
        this.mStateNums7 = mStateNums7;
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

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StateDetails.class);
                intent.putExtra("nActive", mStateNums5.get(holder.getAdapterPosition()));
                intent.putExtra("nRecovered", mStateNums6.get(holder.getAdapterPosition()));
                intent.putExtra("nDeceased", mStateNums7.get(holder.getAdapterPosition()));
                intent.putExtra("Active", mStateNums4.get(holder.getAdapterPosition()));
                intent.putExtra("Recovered", mStateNums2.get(holder.getAdapterPosition()));
                intent.putExtra("Deceased", mStateNums3.get(holder.getAdapterPosition()));
                intent.putExtra("Total", mStateNums1.get(holder.getAdapterPosition()));
                intent.putExtra("Name", mStateList.get(holder.getAdapterPosition()));

                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mStateNums1.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView textView1, textView2, textView3, textView4, textView8;
    public LinearLayout linear;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView8 = itemView.findViewById(R.id.textView8);

            linear = itemView.findViewById(R.id.linear);
        }
    }
}

