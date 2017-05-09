package com.ericwyn.kindlesharehelper;

//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//import com.ericwyn.filechooseutil.FileChooseDialogBuilder;
//import com.ericwyn.filechooseutil.FileChoose;
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
//                Intent intent=new Intent(MainActivity.this, FileChoose.class);
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ericwyn.kindlesharehelper.fragment.MyPagerAdapter;

import static com.ericwyn.kindlesharehelper.fragment.FileChooseFragment.addaData;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.main_viewPager);
        pagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                String filePath = bundle.getString("filePath");
                Log.i("FileChooseFragment_测试",filePath);
                addaData(filePath);
                break;
            default:
                break;
        }
    }
}
