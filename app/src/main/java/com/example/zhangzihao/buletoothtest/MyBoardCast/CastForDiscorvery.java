package com.example.zhangzihao.buletoothtest.MyBoardCast;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import android.os.Handler;

/**
 * 用来监听蓝牙搜索结果的接收器
 */
public class CastForDiscorvery extends BroadcastReceiver {

    private Handler mhanler;

    public CastForDiscorvery(Handler handler){
        mhanler=handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //每次一个一个设备的发现
        String action = intent.getAction();
        //搜寻到新设备时进行如下

        switch (action){
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.
                        getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //传送可序列化对象
                Message ms=mhanler.obtainMessage();
                Bundle b=new Bundle();
                b.putParcelable("device",device);
                ms.setData(b);
                mhanler.handleMessage(ms);
                break;

        }

    }
}
