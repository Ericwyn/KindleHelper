package com.ericwyn.kindlesharehelper;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ericwyn.kindlesharehelper.fragment.FileChooseFragment;
import com.ericwyn.kindlesharehelper.fragment.MyPagerAdapter;
import com.ericwyn.kindlesharehelper.utils.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import hei.permission.PermissionActivity;

import static com.ericwyn.kindlesharehelper.fragment.FileChooseFragment.addaData;
import static com.ericwyn.kindlesharehelper.fragment.FileChooseFragment.sdPath;

public class MainActivity extends PermissionActivity {

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.main_viewPager);
        pagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        createWelcomeFile();
    }

    /**
     * 创建欢迎文件
     */
    private void createWelcomeFile(){
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
//                                TODO : 需要权限去完成的功能

                File file=new File(sdPath+"/KindleShareHelper");
                if(!file.isDirectory()){
                    file.mkdir();
                    try{
                        BufferedWriter welcomeFile=new BufferedWriter(new FileWriter(sdPath+"/KindleShareHelper/欢迎文件.txt"));
                        welcomeFile.write("欢迎使用kindleShareHelper,你可以使用本应用，经由Http服务，自由的分享你手机上面的内容" +
                                "\n\n\n" +
                                "\n来自Ericwyn" +
                                "\n欢迎访问Github : Https://github.com/Ericwyn/KindleHelper");
                        welcomeFile.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else {
                    if(!new File(sdPath+"/KindleShareHelper/欢迎文件.txt").isFile()){
                        try{
                            BufferedWriter welcomeFile=new BufferedWriter(new FileWriter(sdPath+"/KindleShareHelper/欢迎文件.txt"));
                            welcomeFile.write(TextUtils.WELCOME_FILE_TEXT);
                            welcomeFile.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                FileChooseFragment.initDataFirst();

            }
        },
        "请给予相关权限，以便生成欢迎文件",
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            //获取文件选择器选中的文件名称，然后添加文件
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                String filePath = bundle.getString("filePath");
                addaData(filePath);
                break;
            default:
                break;
        }
    }
}
