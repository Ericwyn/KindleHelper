package com.ericwyn.kindlesharehelper.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ericwyn.kindlesharehelper.R;
import com.ericwyn.kindlesharehelper.fragment.FileChooseFragment;
import com.ericwyn.kindlesharehelper.fragment.MainFragment;

import java.util.HashMap;

/**
 * 文件详情的Dialog
 * Created by ericwyn on 17-5-11.
 */

public class FileDetailsDialog extends AlertDialog.Builder {
    private Context mContext;
    private TextView fileName;
    private TextView filePath;
    private TextView fileUrl;

    private HashMap<String ,Object> dataMap;

    public FileDetailsDialog(@NonNull Context context,HashMap<String ,Object> dataMap) {
        super(context);
        mContext=context;
        //获取传进来的上下文元素的布局文件并且实例化
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dialog_filedetails, null);

        this.dataMap=dataMap;

        this.setView(view);
        //设置标题
        super.setTitle("文件详情");
        //设置积极按钮
        super.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //设置中性按钮
        super.setNeutralButton("从列表移除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FileChooseFragment.deleteaData(FileChooseFragment.sdPath+"/"+filePath.getText().toString());
            }
        });

    }
    //载入布局，绑定各种元素
    @Override
    public AlertDialog.Builder setView(View view) {
        fileName=(TextView)view.findViewById(R.id.tv_fileName_dialog);
        filePath=(TextView)view.findViewById(R.id.tv_filePath_dialog);
        fileUrl=(TextView)view.findViewById(R.id.tv_fileUrl_dialog);

        fileName.setText((String)dataMap.get("name"));


        filePath.setText(((String)dataMap.get("path")).replace(FileChooseFragment.sdPath+"/",""));
        fileUrl.setText(MainFragment.getLocalIpStr(getContext())+":"+dataMap.get("port"));

        return super.setView(view);
    }
}
