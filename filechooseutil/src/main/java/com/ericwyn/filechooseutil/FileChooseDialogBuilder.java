package com.ericwyn.filechooseutil;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 文件管理器Dialog
 * Created by ericwyn on 17-4-22.
 */

public class FileChooseDialogBuilder extends AlertDialog.Builder {
    private Context mContext;
    private FileListView listView;

    /**
     * 默认选择全部的类型
     * @param context   上下文
     */
    public FileChooseDialogBuilder(@NonNull Context context){
        this(context,"all");
    }

    /**
     * 带有类型的选择
     * @param context   上下文
     * @param type  类型
     */
    public FileChooseDialogBuilder(@NonNull Context context, String type) {
        super(context);
        mContext=context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.activity_file_chosse, null);
        this.setView(view);
        //设置标题
        super.setTitle("选择文件");

        //设置积极按钮
        super.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //设置消极按钮
        super.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public AlertDialog.Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        return super.setOnKeyListener(onKeyListener);
    }

    @Override
    public AlertDialog.Builder setView(View view) {
        listView=(FileListView)view.findViewById(R.id.listView_FileChoose);
        return super.setView(view);
    }
}
