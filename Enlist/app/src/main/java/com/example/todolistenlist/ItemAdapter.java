package com.example.todolistenlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private static final String TAG = "ItemAdapter";
    Context context;
    static ArrayList<DataItem> dataItems;
    int flag=0;
    String todayAsString,day_after_tomorrowAsString,tomorrowAsString;

    public ItemAdapter(Context c,ArrayList<DataItem> p){
        context = c;
        dataItems = p;
    }

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat format_date = new SimpleDateFormat("dd MMM");
    String date = format_date.format(calendar.getTime());

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder ViewHolder , int position) {

        if(flag == 0){
            Date today = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date day_after_tomorrow = calendar.getTime();

            DateFormat dateFormat = new SimpleDateFormat("dd MMM");

            todayAsString = dateFormat.format(today);
            tomorrowAsString = dateFormat.format(tomorrow);
            day_after_tomorrowAsString = dateFormat.format(day_after_tomorrow);
        }
        flag=1;

        ViewHolder.title.setText(dataItems.get(position).getTitle());
        ViewHolder.description.setText(dataItems.get(position).getDescription());
        ViewHolder.deadline.setText(dataItems.get(position).getDeadline());

        if(dataItems.get(position).getDeadline().equals(date)){
            ViewHolder.title.setTextColor(context.getResources().getColor(R.color.colorPrimary) );
            ViewHolder.deadline.setTextColor(context.getResources().getColor(R.color.colorPrimary) );
        }
        else if(dataItems.get(position).getDeadline().equals(tomorrowAsString)){
            ViewHolder.title.setTextColor(context.getResources().getColor(R.color.medium) );
            ViewHolder.deadline.setTextColor(context.getResources().getColor(R.color.medium) );
        }
        else if(dataItems.get(position).getDeadline().equals(day_after_tomorrowAsString)){
            ViewHolder.title.setTextColor(context.getResources().getColor(R.color.low) );
            ViewHolder.deadline.setTextColor(context.getResources().getColor(R.color.low) );
        }

        final String getTitle_Layout = dataItems.get(position).getTitle();
        final String getDescription_Layout = dataItems.get(position).getDescription();
        final String getDeadline_Layout = dataItems.get(position).getDeadline();
        final String getKey_Layout = dataItems.get(position).getKey();

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, EditTask.class);
                intent.putExtra("title_extra",getTitle_Layout);
                intent.putExtra("description_extra",getDescription_Layout);
                intent.putExtra("deadline_extra",getDeadline_Layout);
                intent.putExtra("key_extra",getKey_Layout);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,deadline,key;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_layout);
            description = itemView.findViewById(R.id.description_layout);
            deadline = itemView.findViewById(R.id.deadline_layout);
        }
    }
}
