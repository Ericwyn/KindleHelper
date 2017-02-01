package ericwyn.kindlehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private Button button;
    private Button close;

    private HttpServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.btn_open_main);
        close=(Button)findViewById(R.id.btn_close_main);
        text=(TextView)findViewById(R.id.text_main);
        server=new HttpServer();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    server.start();
                    text.setText("服务已经开启\n请使用浏览器访问 "+"192.168.1.104"+":"+HttpServer.DEFAULT_SERVER_PORT);
                }catch (IOException e){
                    e.printStackTrace();
                    text.setText(e.getMessage());
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                server.stop();
                text.setText("服务已关闭，点击按钮开启局域网http服务");
            }
        });


    }
//    private static String getLocalIpStr(Context context){
//        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
//        return intToIpAddr(wifiInfo.getIpAddress());
//    }
//
//    private static String intToIpAddr(int ip) {
//        return (ip & 0xff) + "." + ((ip>>8)&0xff) + "." + ((ip>>16)&0xff) + "." + ((ip>>24)&0xff);
//    }
}
