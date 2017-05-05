package com.ericwyn.kindlesharehelper;

//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//import com.ericwyn.filechooseutil.FileChooseDialogBuilder;
//import com.ericwyn.filechooseutil.FileChosse;
//
//public class MainActivity extends AppCompatActivity {
//    private Button mButton1;
//    private Button mButton2;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mButton1=(Button)findViewById(R.id.button_test1);
//        mButton2=(Button)findViewById(R.id.button_test2);
//        mButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this, FileChosse.class);
//                startActivity(intent);
//            }
//        });
//        mButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FileChooseDialogBuilder builder=new FileChooseDialogBuilder(MainActivity.this);
//                builder.show();
//            }
//        });
//
//
//    }
//}

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HttpServer server;
    private String sdPath= Environment.getExternalStorageDirectory().getPath();
    private ImageButton fab;
    private boolean isServiceOpen=false;
    private SimpleServer[] simpleServers;
    private TextView ip;
    public static String ipAdress="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createWelcomeFile();
        setContentView(R.layout.activity_main);
        fab=(ImageButton)findViewById(R.id.fab_main) ;
        ip=(TextView)findViewById(R.id.tv_ipadress_main);
        ArrayList<HashMap<String ,Object>> list=new ArrayList<>();
        HashMap<String,Object> map=new HashMap<>();
        map.put("name","欢迎文件.txt");
        map.put("size","1");
        map.put("path",sdPath+"/KindleShareHelper/欢迎文件.txt");
        map.put("port",9999);
        list.add(map);
        //启动主页服务
        server=new HttpServer(list);

        ipAdress=getLocalIpStr(MainActivity.this);
        //获取ip地址，供给SimpleServer构造下载地址端口
        simpleServers=new SimpleServer[list.size()];
        for(int i=0;i<list.size();i++){
            int portTemp=(int )list.get(i).get("port");
            simpleServers[i]=new SimpleServer(portTemp,(String)list.get(i).get("path"));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServiceOpen){
                    stopServer();
                }else {
                    openServer();
                    if(!server.isAlive()){
                        try {
                            server.start();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void openServer(){
        if(server==null){
            return;
        }

        try{
            server.start();
            fab.setBackground(getDrawable(R.drawable.ripple_bg_blue));
            try{
                for(SimpleServer simpleServer:simpleServers){
                    if(simpleServer!=null && !simpleServer.isAlive()){
                        simpleServer.start();
                    }
                }
            }catch (IOException e){
                Log.i("MainActivity","启动simple服务时候遇到失败");
            }

        }catch (IOException ioe){
            ioe.printStackTrace();
            Toast.makeText(MainActivity.this,"发生io错误，服务开启失败",Toast.LENGTH_SHORT).show();
            return;
        }

        ip.setText(getLocalIpStr(MainActivity.this));
        isServiceOpen=true;
    }

    private void stopServer(){
        if(server==null){
            return;
        }
        if(!server.isAlive()){
            return;
        }

        for(SimpleServer simpleServer:simpleServers){
            if(simpleServer.isAlive()){
                simpleServer.stop();
            }
        }

        server.stop();
        fab.setBackground(getDrawable(R.drawable.ripple_bg_red));
        ip.setText("服务未开启");
        isServiceOpen=false;
    }


    /**
     * 创建欢迎文件
     */
    private void createWelcomeFile(){
        File file=new File(sdPath+"/KindleShareHelper");
        if(!file.isDirectory()){
            file.mkdir();
            try{
                BufferedWriter welcomeFile=new BufferedWriter(new FileWriter(sdPath+"/KindleShareHelper/欢迎文件.txt"));
                welcomeFile.write("Welcome use KindleShareHelper");
                welcomeFile.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.stop();
    }

    public static String getHostIp1() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取机器本身的ip地址，首先判断wifi是否开启，如果开启的话返回wifi连接的ip地址，否则返回移动网络的ip地址
     * @param context 传入的上下文
     * @return 返回ip地址的字符串，形式类似于：192.168.1.1
     */
    private static String getLocalIpStr(Context context){
        if(isWiFi(context)){
            WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo=wifiManager.getConnectionInfo();
            return intToIpAddr(wifiInfo.getIpAddress());
        }else {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {
                Log.e("getIPTest", ex.toString());
            }
            return null;
        }

    }

    public static boolean isWiFi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    private static String intToIpAddr(int ip) {
        return (ip & 0xff) + "." + ((ip>>8)&0xff) + "." + ((ip>>16)&0xff) + "." + ((ip>>24)&0xff);
    }


}
