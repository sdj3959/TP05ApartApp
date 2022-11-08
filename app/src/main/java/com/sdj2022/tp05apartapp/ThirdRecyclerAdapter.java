package com.sdj2022.tp05apartapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ThirdRecyclerAdapter extends RecyclerView.Adapter<ThirdRecyclerAdapter.VH> {

    Context context;
    ArrayList<ThirdRecyclerItem> items;

    public ThirdRecyclerAdapter(Context context, ArrayList<ThirdRecyclerItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_third_item, parent, false);
        VH holder = new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String days = sdf.format(date);
        int today = Integer.parseInt(days.replaceAll("-",""));

        String itemsDays = items.get(position).endDate.replaceAll("-","");
        int compareDay = Integer.parseInt(itemsDays);

        String itemDays2 = items.get(position).startDate.replaceAll("-","");
        int compareDay2 = Integer.parseInt(itemDays2);

        if(today<=compareDay&&today>=compareDay2){
            holder.startDate.setTextColor(context.getResources().getColor(R.color.blue));
            holder.endDate.setTextColor(context.getResources().getColor(R.color.blue));
        }else if(today<compareDay2){
            holder.startDate.setTextColor(context.getResources().getColor(R.color.purple));
            holder.endDate.setTextColor(context.getResources().getColor(R.color.purple));
        }else{
            holder.startDate.setTextColor(context.getResources().getColor(R.color.red));
            holder.endDate.setTextColor(context.getResources().getColor(R.color.red));
        }

        holder.name.setText("주택명 : "+items.get(position).name);
        holder.regName.setText("·공급지역 : "+items.get(position).regName);
        holder.location.setText("·공급위치 : "+items.get(position).location);
        holder.startDate.setText("·청약 접수 시작일 : "+items.get(position).startDate);
        holder.endDate.setText("·청약 접수 종료일 : "+items.get(position).endDate);
        holder.address.setText("·홈페이지 주소 : "+items.get(position).address);
        holder.company.setText("·건설업체명 : "+items.get(position).company);
        holder.tel.setText("·문의처 : "+items.get(position).tel);
        holder.bank.setText("·사업주체명(시행사) : "+items.get(position).bank);
        holder.goInsideDate.setText("·입주예정년월 : "+items.get(position).goInsideDate);

        holder.btnMap.setOnClickListener(v->{
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra("map", items.get(position).location);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView name, regName, location, startDate, endDate, address, company, tel, bank, goInsideDate;
        AppCompatButton btnMap;

        public VH(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            regName = itemView.findViewById(R.id.tv_reg_name);
            location = itemView.findViewById(R.id.tv_location);
            startDate = itemView.findViewById(R.id.tv_start_date);
            endDate = itemView.findViewById(R.id.tv_end_date);
            address = itemView.findViewById(R.id.tv_address);
            company = itemView.findViewById(R.id.tv_company);
            tel = itemView.findViewById(R.id.tv_tel);
            bank = itemView.findViewById(R.id.tv_bank);
            goInsideDate = itemView.findViewById(R.id.tv_go_inside_date);
            btnMap = itemView.findViewById(R.id.btn_map);
        }
    }
}
