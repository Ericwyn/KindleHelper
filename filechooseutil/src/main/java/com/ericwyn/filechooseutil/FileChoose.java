package com.ericwyn.filechooseutil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class FileChoose extends AppCompatActivity{

    private static FileListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chosse);
        initView();
    }

    private  void initView(){
        mListView=(FileListView)findViewById(R.id.listView_FileChoose);
        mListView.setActivity(this);

    }

    @Override
    public void onBackPressed() {
        //如果底层mListView的返回按钮的事件没有执行的话
        if(!mListView.onBackPressed()){
            super.onBackPressed();
        }
    }

    public void resultActivity(){
        Intent intent2 = new Intent();
        intent2.putExtra("filePath", mListView.getPathData());
        // 通过调用setResult方法返回结果给前一个activity。
        FileChoose.this.setResult(RESULT_OK, intent2);
        //关闭当前activity
        FileChoose.this.finish();
    }


}
