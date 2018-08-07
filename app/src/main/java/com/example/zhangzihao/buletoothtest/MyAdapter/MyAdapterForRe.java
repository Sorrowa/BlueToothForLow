package com.example.zhangzihao.buletoothtest.MyAdapter;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhangzihao.buletoothtest.ConnectActivity;
import com.example.zhangzihao.buletoothtest.R;

import java.util.ArrayList;

/**
 * 显示蓝牙信息的recycleView的适配器
 */
public class MyAdapterForRe extends RecyclerView.Adapter<MyAdapterForRe.MyViewHolder> {

    //存储每项的信息
    private ArrayList<BluetoothDevice> devices;

    private Context context;

    private FragmentManager manager;


    public MyAdapterForRe(ArrayList<BluetoothDevice> devices,Context context,
                          FragmentManager manager){
        this.devices=devices;
        this.context=context;
        this.manager=manager;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_view_one,
                parent,false);

        final MyViewHolder holder=new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:显示确认信息
                MyDialogFragment dialog=new MyDialogFragment();
                dialog.show(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //todo:跳转到下一个界面
                                    goToConnectActivity(holder.position);
                                }
                            },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //todo:闭当前窗口
                            }
                        },
                        manager);
            }

        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BluetoothDevice device=devices.get(position);
        holder.mac.setText(device.getAddress());
        holder.setPosition(position);
        if (device.getName()==null){
            holder.name.setText("null");
            return;
        }
        holder.name.setText(device.getName());



    }


    @Override
    public int getItemCount() {
        return devices.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView mac;
        public int position=-1;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.position=position;
            name=itemView.findViewById(R.id.text_top);
            mac=itemView.findViewById(R.id.text_bottom);
        }

        public void setPosition(int i){
            position=i;
        }
    }


    public void add_item(BluetoothDevice device){
        if(devices==null){
            devices=new ArrayList<>();
        }
        if (devices.contains(device)){
            return;
        }
        this.devices.add(device);
        //刷新布局
        notifyDataSetChanged();
    }

    /**
     * 清空内容
     */
    public void clear(){
        devices.clear();
    }


    /**
     * 提示对话框
     */
    public static class MyDialogFragment extends DialogFragment{

        //设置为两个按钮的监听器
        private DialogInterface.OnClickListener positiveCallback;
        private DialogInterface.OnClickListener negativeCallback;

        /**
         * 显示内容
         * @param positiveCallback 确定的监听器
         * @param negativeCallback 否认的监听器
         * @param fragmentManager 管理对象
         */
        public void show(DialogInterface.OnClickListener positiveCallback,
                         DialogInterface.OnClickListener negativeCallback,
                         FragmentManager fragmentManager){
            this.positiveCallback=positiveCallback;
            this.negativeCallback=negativeCallback;
            //显示提示框
            show(fragmentManager,"确认");
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setTitle("确认吗？宝贝");
            builder.setMessage("");
            builder.setPositiveButton("确定", positiveCallback);
            builder.setNegativeButton("取消", negativeCallback);
            return builder.create();
        }
    }

    /**
     * 进入连接界面，并且进行数据整合
     */
    private void goToConnectActivity(int positon){
        BluetoothDevice device=devices.get(positon);
        Intent intent=new Intent(context, ConnectActivity.class);
        Bundle bundle=new Bundle();
        bundle.putParcelable("device",device);
        intent.putExtra("device",bundle);
        context.startActivity(intent);
    }
}
