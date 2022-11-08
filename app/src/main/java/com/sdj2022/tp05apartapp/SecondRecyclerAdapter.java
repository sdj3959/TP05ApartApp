package com.sdj2022.tp05apartapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SecondRecyclerAdapter extends RecyclerView.Adapter<SecondRecyclerAdapter.VH> {

    Context context;
    ArrayList<SecondRecyclerItem> items;

    public SecondRecyclerAdapter(Context context, ArrayList<SecondRecyclerItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_second_item, parent, false);
        VH holder = new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        holder.btn.setText(items.get(position).region);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        Button btn;

        public VH(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn);

            btn.setOnClickListener(view -> {
                Intent intent = new Intent(context, ThirdActivity.class);

                intent.putExtra("region", btn.getText().toString());
                //intent.putExtra("urlRegion", btn.getText().toString().split(" ")[0]);
                context.startActivity(intent);
            });
        }
    }
}
