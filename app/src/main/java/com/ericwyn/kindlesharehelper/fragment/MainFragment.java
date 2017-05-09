package com.ericwyn.kindlesharehelper.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ericwyn.kindlesharehelper.HttpServer;
import com.ericwyn.kindlesharehelper.R;
import com.ericwyn.kindlesharehelper.SimpleServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static com.ericwyn.kindlesharehelper.fragment.FileChooseFragment.sdPath;

/**
 *
 * Created by ericwyn on 17-5-5.
 */

public class MainFragment extends Fragment{
    private HttpServer server;
    private ImageButton fab;
    private boolean isServiceOpen=false;
    private SimpleServer[] simpleServers;
    private TextView ip;
    public  static String ipAdress="";
    public static Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        mContext=getContext();
        fab=(ImageButton)view.findViewById(R.id.fab_main) ;
        ip=(TextView)view.findViewById(R.id.tv_ipadress_main);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServiceOpen){
                    buildServer();
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
        return view;
    }

    /**
     * 构建服务，主业服务的构建，每次点击启动按钮的时候都要重新构建服务
     */
    private void buildServer(){
        stopServer();
        //构建主页服务
        server=new HttpServer(FileChooseFragment.appData);

        //构建文件下载页面服务
        ipAdress=getLocalIpStr(mContext);
        //获取ip地址，供给SimpleServer构造下载地址端口
        simpleServers=new SimpleServer[FileChooseFragment.appData.size()];
        for(int i=0;i<FileChooseFragment.appData.size();i++){
            int portTemp=10000+i;   //文件端口从10000开始，依次增加，于是就不用文件端口了
            simpleServers[i]=new SimpleServer(portTemp,(String)FileChooseFragment.appData.get(i).get("path"));
        }

    }


    private void openServer(){
        if(server==null){
            return;
        }

        try{
            server.start();
            fab.setBackground(getContext().getDrawable(R.drawable.ripple_bg_green));
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
            Toast.makeText(getContext(),"发生io错误，服务开启失败",Toast.LENGTH_SHORT).show();
            return;
        }

        ip.setText(getLocalIpStr(getContext())+":"+HttpServer.DEFAULT_SERVER_PORT);
        isServiceOpen=true;
    }

    public void stopServer(){
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
        fab.setBackground(getActivity().getDrawable(R.drawable.ripple_bg_red));
        ip.setText("请先开启服务");
        isServiceOpen=false;
    }

    /**
     * 重写生命周期方法，保证销毁Http服务，不占用端口
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
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


    public static String getHostIp() {
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
    public static String getLocalIpStr(Context context){
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

    /**
     * 判断wifi是否开启
     * @param context
     * @return
     */
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
