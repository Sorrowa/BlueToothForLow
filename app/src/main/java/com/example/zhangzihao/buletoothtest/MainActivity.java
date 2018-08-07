package com.example.zhangzihao.buletoothtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zhangzihao.buletoothtest.MyAdapter.MyAdapterForRe;
import com.example.zhangzihao.buletoothtest.MyBoardCast.CastForDiscorvery;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //记录蓝牙可使用状态，作为标记位给之后的数据获取提供一个参考
    public static boolean isPermmison=false;



    private BluetoothAdapter adapter;

    private Button button1;
    private Button button2;

    private RecyclerView recyclerView;

    private Handler handler=new Myhandler();
    //动态广播监听
    private CastForDiscorvery cast;

    //存放device信息
    private ArrayList<BluetoothDevice> devices=new ArrayList<>();
    //relist适配器
    private MyAdapterForRe adapterForRe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取权限
        getPermmison();
        //检查权限
        checkPermisson();

        //下面开始蓝牙配置
        setUpBlueTooth();
        //初始化recycleListView
        initeReCy();
        //初始化button,用于搜索
        initeButton();


    }


    /**
     * 初始化功能
     */
    private void initeReCy() {
        recyclerView=findViewById(R.id.list);
        adapterForRe=new MyAdapterForRe(devices,this,getFragmentManager());
        recyclerView.setAdapter(adapterForRe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 初始化button
     */
    private void initeButton() {
        button1=findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始进行蓝牙搜索
                adapterForRe.clear();
                startSearchBlue();
            }
        });

        button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:拓展
                Toast.makeText(MainActivity.this
                        ,"还没想好",Toast.LENGTH_SHORT)
                        .show();
                adapter.cancelDiscovery();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.cancelDiscovery();
    }

    /**
     * 搜搜蓝牙
     */
    private void startSearchBlue() {
        if (adapter==null){
            return;
        }
        //在广播接收器中进行内容的监听
        adapter.startDiscovery();
        cast=new CastForDiscorvery(handler);
        IntentFilter filter=new IntentFilter();
        //添加过滤事件集合
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //注册广播
        registerReceiver(cast,filter);
    }


    /**
     * 打开蓝牙等配置操作
     */
    private void setUpBlueTooth() {
        adapter=BluetoothAdapter.getDefaultAdapter();
        if (adapter==null){
            Toast.makeText(this,"你的手机并没有蓝牙",Toast.LENGTH_SHORT).show();
            isPermmison=false;
            return;
        }

        if (!adapter.isEnabled()) {
            adapter.enable();
            Toast.makeText(this,"蓝牙开了",Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * 检测是否存在权限
     */
    private void checkPermisson() {
        if((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED))
        {
            isPermmison=true;
        }
    }


    /**
     * 动态获取权限
     * @return 返回获取结果
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void getPermmison(){

        if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED)|| (checkSelfPermission(Manifest
                .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            //如果没有权限，直接申请权限
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                    ,Manifest.permission.ACCESS_FINE_LOCATION}
                    ,1);
        }

    }

    /**
     * 对申请权限的回调结果进行处理
     * @param requestCode 发送的时候指定的参数
     * @param permissions 权限组
     * @param grantResults 权限申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode==1){
            boolean isOK=true;

            for (int i :grantResults){
                if (i==PackageManager.PERMISSION_DENIED){
                    isOK=false;
                    break;
                }
            }

            if (isOK){
                //todo:成功申请权限
            }else{
                Toast.makeText(getApplication(),"申请失败，别用啦",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cast);
    }



    class Myhandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            BluetoothDevice device=bundle.getParcelable("device");
            //todo:这里进行recycleListView内容更新
            adapterForRe.add_item(device);
        }
    }
}
