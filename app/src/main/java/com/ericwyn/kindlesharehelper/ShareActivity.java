package com.ericwyn.kindlesharehelper;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class ShareActivity extends AppCompatActivity {

    public static String sdPath= Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();

        String action=intent.getAction();
        String type=intent.getType();

        if(Intent.ACTION_SEND.equals(action) && type!=null){

            Intent intent1=new Intent();
            ComponentName componentName=new ComponentName("com.ericwyn.kindlesharehelper","com.ericwyn.kindlesharehelper.MainActivity");
            Bundle bundle=new Bundle();
            bundle.putString("path",getFileUrl(intent.getClipData().toString()));
            Log.i("传入文件路径",getFileUrl(intent.getClipData().toString()));
            intent1.putExtras(bundle);
            intent1.setComponent(componentName);
            startActivity(intent1);
            ShareActivity.this.finish();

        }

    }

    private String getFileUrl(String clipdata){
        clipdata=clipdata.substring(clipdata.indexOf(sdPath),clipdata.length()-2);
        clipdata=clipdata.replace("}","");
        try{
            String decode=java.net.URLDecoder.decode(clipdata,"utf-8");
            return decode;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return clipdata;
        }

    }

}
