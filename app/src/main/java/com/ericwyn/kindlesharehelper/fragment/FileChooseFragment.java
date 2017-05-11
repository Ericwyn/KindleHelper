package com.ericwyn.kindlesharehelper.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ericwyn.filechooseutil.FileChooseDialogBuilder;
import com.ericwyn.kindlesharehelper.MainActivity;
import com.ericwyn.kindlesharehelper.R;
import com.ericwyn.kindlesharehelper.dialog.FileDetailsDialog;
import com.ericwyn.kindlesharehelper.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import hei.permission.PermissionActivity;

/**
 * 待发送文件列表的Fragment
 * Created by ericwyn on 17-5-5.
 */

public class FileChooseFragment extends Fragment{
    private static int portTemp=9527;
    private static boolean initData=true;
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

        View view=inflater.inflate(R.layout.fragment_filechoose,container,false);
        button=(ImageButton)view.findViewById(R.id.fab_fileChoose);
        listView=(ListView)view.findViewById(R.id.listView_fileChooseList_fileChooseFragment);

        adapter=new SimpleAdapter(getContext(),
                appData,
                R.layout.item_lv_filchoose,
                new String[]{"name","size"},
                new int[]{R.id.tv_fileName_item,R.id.tv_fileSize_item}
        );

        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PermissionActivity)getActivity()).checkPermission(
                        new PermissionActivity.CheckPermListener() {
                            @Override
                            public void superPermission() {
//                        TODO : 需要权限去完成的功能
                                Intent intent=new Intent(getActivity(),com.ericwyn.filechooseutil.FileChoose.class);
                                startActivityForResult(intent,1);
                            }
                        },
                        "请给予相关权限，以便系统运作",
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

//                fileChooseDialogBuilder.getListView()

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter=parent.getAdapter();
                HashMap<String ,Object> map=(HashMap<String ,Object>) adapter.getItem(position);
                FileDetailsDialog fileDetailsDialog=new FileDetailsDialog(getContext(),map);
                fileDetailsDialog.show();
            }
        });

        Log.i("FileChooseFragment","活动构建完成");
        if(!MainActivity.sharepath.equals("")){
            addaData(MainActivity.sharepath);
            MainActivity.sharepath="";
            Toast.makeText(getActivity(),"所选文件已加入分享列表\n启动服务即可分享",Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    /**
     * 设置默认数据,只供给MainActivity使用
     */
    public static void initDataFirst(){
        if(initData){
            if(new File(sdPath+"/KindleShareHelper/欢迎文件.txt").isFile()){
                HashMap<String,Object> map=new HashMap<>();
                map.put("name","欢迎文件.txt");
                map.put("size", FileUtils.getAutoFileOrFilesSize(sdPath+"/KindleShareHelper/欢迎文件.txt"));
                map.put("path",sdPath+"/KindleShareHelper/欢迎文件.txt");
                map.put("port",portTemp++);
                appData.add(map);
            }
            initData=false;
        }
    }

    /**
     * 增加一条文件数据
     * @param filePath 传入的文件路径
     */
    public void addaData(String filePath){
        HashMap<String,Object> map=new HashMap<>();
        String[] pathTemp=filePath.split("/");

        map.put("name",pathTemp[pathTemp.length-1]);
        map.put("size", FileUtils.getAutoFileOrFilesSize(filePath));
        map.put("path",filePath);
        map.put("port",portTemp++);


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
