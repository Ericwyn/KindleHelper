package com.ericwyn.kindlesharehelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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
                addaData(filePath);
                break;
            default:
                break;
        }
    }
}
