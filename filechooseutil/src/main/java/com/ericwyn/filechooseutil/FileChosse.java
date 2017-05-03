package com.ericwyn.filechooseutil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class FileChosse extends AppCompatActivity{

    private static FileListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chosse);
        initView();
    }

    private  void initView(){
        mListView=(FileListView)findViewById(R.id.listView_FileChoose);

    }

    @Override
    public void onBackPressed() {
        //如果底层mListView的返回按钮的事件没有执行的话
        if(!mListView.onBackPressed()){
            super.onBackPressed();
        }
    }
}
