package ericwyn.kindlehelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private HttpServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        server=new HttpServer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.stop();
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
