package com.ericwyn.kindlesharehelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ericwyn.filechooseutil.FileChooseDialogBuilder;
import com.ericwyn.kindlesharehelper.FileTools;
import com.ericwyn.kindlesharehelper.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 待发送文件列表的Fragment
 * Created by ericwyn on 17-5-5.
 */

public class FileChooseFragment extends Fragment{
    private ImageButton button;
    private ListView listView;
    private static SimpleAdapter adapter;
//    private ArrayList<HashMap<String,Object>> data;
    public static String sdPath= Environment.getExternalStorageDirectory().getPath();

    public static ArrayList<HashMap<String,Object>> appData=new ArrayList<>();

    public static FileChooseDialogBuilder fileChooseDialogBuilder;
    public static AlertDialog dialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initDataFirst();    //放进去初始数据，也就是欢迎文件的数据
        View view=inflater.inflate(R.layout.fragment_filechoose,container,false);
        button=(ImageButton)view.findViewById(R.id.fab_fileChoose);
        listView=(ListView)view.findViewById(R.id.listView_fileChooseList_fileChooseFragment);

        adapter=new SimpleAdapter(getContext(),
                appData,
                R.layout.item_lv_filchoose,
                new String[]{},
                new int[]{}
        );

        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),com.ericwyn.filechooseutil.FileChoose.class);
                startActivity(intent);
//                fileChooseDialogBuilder.getListView()
            }
        });

        return view;
    }

    /**
     * 设置默认数据
     */
    private static void initDataFirst(){
        HashMap<String,Object> map=new HashMap<>();
        map.put("name","欢迎文件.txt");
        map.put("size",FileTools.getAutoFileOrFilesSize(sdPath+"/KindleShareHelper/欢迎文件.txt"));
        map.put("path",sdPath+"/KindleShareHelper/欢迎文件.txt");
        appData.add(map);
    }

    /**
     * 增加一条文件数据
     * @param filePath 传入的文件路径
     */
    public static void addaData(String filePath){
        HashMap<String,Object> map=new HashMap<>();
        String[] pathTemp=filePath.split("/");
        map.put("name",pathTemp[pathTemp.length-1]);
        map.put("size", FileTools.getAutoFileOrFilesSize(filePath));
        map.put("path",filePath);
        appData.add(map);
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除一条文件数据
     * @param filePath 传入的文件路径
     */
    public static void deleteaData(String filePath){
        for(int i=0;i<appData.size();i++){
            if(((String)appData.get(i).get("path")).equals(filePath)){
                appData.remove(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }


}
