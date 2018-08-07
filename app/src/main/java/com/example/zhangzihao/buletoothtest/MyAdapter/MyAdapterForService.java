package com.example.zhangzihao.buletoothtest.MyAdapter;

import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhangzihao.buletoothtest.ConnectActivity;
import com.example.zhangzihao.buletoothtest.R;

import java.util.List;

public class MyAdapterForService extends RecyclerView.Adapter<MyAdapterForService.ViewHolder> {

    private ConnectActivity context;

    private List<BluetoothGattService> services;


    public MyAdapterForService(ConnectActivity context, List<BluetoothGattService> services){
        this.context=context;
        this.services=services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.service_item,parent,
                false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(services.get(position).getUuid().toString());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.insertChar(services.get(position).getCharacteristics());
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            textView=itemView.findViewById(R.id.item);
        }
    }
}
