package com.example.zhangzihao.buletoothtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangzihao.buletoothtest.MyAdapter.MyAdapterForChar;
import com.example.zhangzihao.buletoothtest.MyAdapter.MyAdapterForService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

public class ConnectActivity extends AppCompatActivity {

    private BluetoothDevice device;

    private BluetoothGatt get;

    private List<BluetoothGattService> serviceList;
    //服务列表
    private RecyclerView services;
    //character特征列表
    private RecyclerView characters;
    //特征值数据
    private List<BluetoothGattCharacteristic> characteristics;

    private BluetoothSocket socket;

    private InputStream input;

    private OutputStream output;

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        initeViews();
        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("device");
        device = b.getParcelable("device");
        //监听状态改变情况
        get = device.connectGatt(this, true, new BluetoothGattCallback() {

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("zzh", "成功啦");
                }
            }

            //服务发现
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);

                serviceList = gatt.getServices();

                Log.d("zzh","list="+serviceList);

                insertData(serviceList);

            }

            //读取
            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                byte[] data=characteristic.getValue();
                String value="";
                for(int i=0;i<data.length;i++){
                    //打印十六进制
                    value+=String.format("%02x",data[i]);
                }
                Toast.makeText(ConnectActivity.this,"按键内容:"+value,
                        Toast.LENGTH_SHORT).show();
                textView.setText(value);
            }
        });

//        if (get.connect()) {
//            //连接失败那么进入前一个活动之中
//            Toast.makeText(this, "连接失败", Toast.LENGTH_SHORT).show();
//            finish();
//        }
        //启动服务搜寻，之后会调用上面的回调接口
        Log.d("zzh", String.valueOf(get.discoverServices()));

    }

    //向service列表插入数据
    private void insertData(List<BluetoothGattService> serviceList) {
        MyAdapterForService myAdapterForService = new MyAdapterForService(this,
                serviceList);
        services.setAdapter(myAdapterForService);
        services.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 提醒向character中插入数据,在Adapter中调用
     */
    public void insertChar(List<BluetoothGattCharacteristic> characteristics) {
        this.characteristics = characteristics;
        //todo:传入数据
        MyAdapterForChar myAdapterForChar = new MyAdapterForChar(
                this.characteristics, this);
        characters.setLayoutManager(new LinearLayoutManager(this));
        characters.setItemAnimator(new DefaultItemAnimator());
        characters.setAdapter(myAdapterForChar);

    }

    /**
     * 初始化view
     */
    private void initeViews() {
        textView=findViewById(R.id.value);
        services = findViewById(R.id.serviceList);
        characters = findViewById(R.id.characteristicList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源，相当于断开了连接
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (get != null) {
            get.disconnect();
            get.close();
        }
    }

    /**
     * 连接操作,进入下一个界面
     */
    public void connectChar(UUID uuid, BluetoothGattCharacteristic characteristic) {
//        try {
//            //获取输入输出流
//            socket = device.createRfcommSocketToServiceRecord(uuid);
//            socket.connect();
//            input = socket.getInputStream();
//            output = socket.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (get.setCharacteristicNotification(characteristic, false)) {
//            Toast.makeText(this, "请求notify成功", Toast.LENGTH_SHORT)
//                    .show();
//        }
        get.setCharacteristicNotification(characteristic,true);

        List<BluetoothGattDescriptor> descriptors= characteristic
                .getDescriptors();
        for (BluetoothGattDescriptor b :descriptors){
            //先将功能打开
            b.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            get.writeDescriptor(b);
        }
    }
}
