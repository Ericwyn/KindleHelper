package com.ericwyn.kindlesharehelper;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
            Log.i("XXXXXXXX",intent.getClipData().toString());
            bundle.putString("path",getFileUrl(intent.getClipData().toString()));
            Log.i("ShareActivity文件路径",getFileUrl(intent.getClipData().toString()));
            intent1.putExtras(bundle);
            intent1.setComponent(componentName);
            startActivity(intent1);
            ShareActivity.this.finish();

        }

    }

    private String getFileUrl(String clipdata){
        clipdata=clipdata.substring(clipdata.indexOf(sdPath)+1,clipdata.length());
        clipdata=clipdata.replace("}","");
//
//        String[] flags=clipdata.split("/");
//
//        String[] fileNames=flags[flags.length-1].split(".");
//        String fileType=fileNames[fileNames.length-1];
//
//        String znName="";
//        if(flags2.length!=0){
//            znName=decodeKeywords(flags2[0],"UTF-8");
//            Log.i("XXXXXXXX_UTF8",znName);
//        }else {
//            znName=decodeKeywords(flags[flags.length-1],"UTF-8");
//            Log.i("XXXXXXXX_222",znName);
//        }
//
//        StringBuffer newName=new StringBuffer("/");
//        for(int i=0;i<flags.length;i++){
//            if(i!=flags.length-1){
//                newName.append(flags[i]);
//                newName.append("/");
//            }else {
//                newName.append(znName);
//                newName.append(flags2[flags2.length-1]);
//            }
//        }
        Log.i("XXXX",decode(clipdata,"UTF-8"));
        return decode(clipdata,"UTF-8");

    }

    public static String decode(String codes, String encoding) {
        int byte_num = 0;
        if (encoding.equals("GB2312"))
            byte_num = 2;
        else if (encoding.equals("UTF-8"))
            byte_num = 3;
        else
            return ""; // 其他编码
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < codes.length(); i++) {

            char current_char = codes.charAt(i);

            if (current_char == '%') {
                if (i + 2 < codes.length()) {
                    int dec_number = Integer.valueOf(codes.substring(i + 1,
                            i + 3), 16);
                    if (dec_number < 128) { // 特殊符号
                        byte[] char_bytes = new byte[1];
                        char_bytes[0] = (byte) dec_number;
                        result.append(new String(char_bytes));
                        i += 2;
                    } else {
                        if (i + 3 * byte_num - 1 < codes.length()) { // 汉字
                            byte[] char_bytes = new byte[byte_num];
                            for (int j = 0; j < byte_num; j++) {
                                dec_number = Integer.valueOf(codes.substring(i
                                        + 3 * j + 1, i + 3 * j + 3), 16);
                                if (dec_number > 127)
                                    dec_number -= 256;
                                char_bytes[j] = (byte) dec_number;
                            }
                            try {
                                result.append(new String(char_bytes, encoding));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            i = i + 3 * byte_num - 1;
                        } else
                            return "";
                    }
                } else
                    return "";
            } else if (current_char > 'A' && current_char < 'Z'
                    || current_char > 'a' && current_char < 'z' || current_char=='.' || current_char=='/') { // 字母
                result.append(current_char);
            }
        }
        return result.toString();
    }

    public static String decodeKeywords(String codes, String encoding) {
        ArrayList<String> code_list = new ArrayList<String>();
        StringBuffer period = new StringBuffer();
        codes += '+';
        for (int i = 0; i < codes.length(); i++) { // 断开关键词
            if (codes.charAt(i) == '+') {
                code_list.add(period.toString());
                period = new StringBuffer();
            } else {
                period.append(codes.charAt(i));
            }
        }
        StringBuffer result = new StringBuffer();
        for (String code : code_list) {
            result.append(decode(code, encoding) + " ");
        }
        return result.toString();
    }

}
