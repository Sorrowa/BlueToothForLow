package com.example.zhangzihao.buletoothtest.MyAdapter;

import android.bluetooth.BluetoothGattCharacteristic;
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

public class MyAdapterForChar extends RecyclerView.Adapter<MyAdapterForChar.ViewHolder>{

    private List<BluetoothGattCharacteristic> characteristics;

    private ConnectActivity context;

    public MyAdapterForChar(List<BluetoothGattCharacteristic> characteristics,
                            ConnectActivity context){
        this.characteristics=characteristics;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.service_item,
                parent,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(characteristics.get(position).toString());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.connectChar(characteristics.get(position).getUuid(),
                        characteristics.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return characteristics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.item);
            view=itemView;
        }
    }
}
