package com.example.to_do_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<DataItem> dataItems;

    public ItemAdapter(Context c,ArrayList<DataItem> p){
        context = c;
        dataItems = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder ViewHolder , int position) {

        ViewHolder.title.setText(dataItems.get(position).getTitle_layout());
        ViewHolder.description.setText(dataItems.get(position).getDescription_layout());
        ViewHolder.deadline.setText(dataItems.get(position).getDeadline_layout());
        //ViewHolder.key.setText(dataItems.get(position).getKey_layout());

        final String getTitle_Layout = dataItems.get(position).getTitle_layout();
        final String getDescription_Layout = dataItems.get(position).getDescription_layout();
        final String getDeadline_Layout = dataItems.get(position).getDeadline_layout();
        final String getKey_Layout = dataItems.get(position).getKey_layout();

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Edit_task.class);
                intent.putExtra("title_extra",getTitle_Layout);
                intent.putExtra("description_extra",getDescription_Layout);
                intent.putExtra("deadline_extra",getDeadline_Layout);
                intent.putExtra("key_extra",getKey_Layout);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

     public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,description,deadline,key;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_layout);
            description = itemView.findViewById(R.id.description_layout);
            deadline = itemView.findViewById(R.id.deadline_layout);
        }
    }
}
