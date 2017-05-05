package com.ericwyn.filechooseutil;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by ericwyn on 17-4-22.
 */

public class FileListView extends ListView implements AdapterView.OnItemClickListener{

    private LayoutInflater inflater;
    private ListView mListView;
    private SimpleAdapter adapter;

    private List<HashMap<String ,Object>> dataList;

    private String historyPath=
            Environment.getExternalStorageDirectory().getPath();    //当前显示列表的父目录路径，默认是sd卡
    //委曲求全，记录当前滚动到的item编号
    private int position=0;
    private int lastPosition=0;

    public FileListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.listview_item, null);
        mListView=this;
        dataList=getDataList(historyPath);
        adapter=new SimpleAdapter(context,
                dataList,R.layout.listview_item,
                new String[]{"img","name"},
                new int[]{R.id.imgView_item,R.id.textName_item});
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//
                    position =mListView.getFirstVisiblePosition();
                    Log.i("测试","position:"+position+"lastPosition:"+lastPosition);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 用来得到<文件名称—文件具体路径>的map
     * 进入新路径时候更新
     * @param path  文件夹路径
     * @return  返回一个map
     */
    private HashMap<String ,String> getNameMapList(String path){
        List<HashMap<String ,String>> list=new ArrayList<>();
        File file=new File(path);
        HashMap<String ,String> map=new HashMap<>();
        if(file.isDirectory()){
            File[] files=file.listFiles();
            for(File fileFlag:files){
                map.put(fileFlag.getName(),fileFlag.getAbsolutePath());
            }
        }

        return map;
    }

    /**
     * 得到特定目录的信息，map存储img_代表类型缩略图，存储name_代表文件名称
     * 进入新路径时候更新
     * @param path  文件夹路径
     * @return  返回的具体数据列表
     */
    private ArrayList<HashMap<String ,Object>> getDataList(String path){
        ArrayList<HashMap<String ,Object>> list=new ArrayList<>();
        File file=new File(path);

        Log.i("AbsolutePath",file.getAbsolutePath());

        File[] files=file.listFiles();
        for(File fileFlag:files){
            HashMap<String ,Object> map=new HashMap<>();
            if(fileFlag.isDirectory()){
                map.put("img",R.drawable.directory_icon);
                map.put("name",fileFlag.getName());
                list.add(map);
            }
        }
        //按照名字顺序排序文件夹
        Collections.sort(list, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                return ((String)o1.get("name")).compareTo((String)(o2.get("name")));
            }
        });

        ArrayList<HashMap<String ,Object>> filelist=new ArrayList<>();
        for(File fileFlag:files){
            HashMap<String ,Object> map=new HashMap<>();
            if(fileFlag.isFile()){
                if (fileFlag.getName().matches("^(.*?)[.][3][gG][pP]$")){
                    map.put("img",R.drawable._3gp_icon);
                }
                else if (fileFlag.getName().matches("^(.*?)[.][aA][vV][iI]$")){
                    map.put("img",R.drawable.avi_icon);
                }
                else if (fileFlag.getName().matches("^(.*?)[.][aA][wW][zZ]$")){
                    map.put("img",R.drawable.book_awz_icon);
                }
                else if (fileFlag.getName().matches("^(.*?)[.][aA][wW][zZ][3]$")){
                    map.put("img",R.drawable.book_awz3_icon);
                }
                else if (fileFlag.getName().matches("^(.*?)[.][mM][oO][bB][iI]$")){
                    map.put("img",R.drawable.book_mobi_icon);
                }
                else if (fileFlag.getName().matches("^(.*?)[.][eE][pP][uU][bB]$")){
                    map.put("img",R.drawable.book_epub_icon);
                }
                else if(fileFlag.getName().matches("^(.*?)[.][d][o][c]$")
                        || fileFlag.getName().matches("^(.*?)[.][d][o][c][x]$") ){
                    map.put("img",R.drawable.doc_icon);
                }
                else if(fileFlag.getName().matches("^(.*?)[.][p][p][t]$")
                        || fileFlag.getName().matches("^(.*?)[.][p][p][t][x]$") ){
                    map.put("img",R.drawable.ppt_icon);
                }
                else if(fileFlag.getName().matches("^(.*?)[.][gG][iI][fF]$")){
                    map.put("img",R.drawable.gif_icon);
                }
                else if(fileFlag.getName().matches("^(.*?)[.][hH][tT][mM][lL]$")){
                    map.put("img",R.drawable.html_icon);
                }
                else if(fileFlag.getName().matches("^(.*?)[.][tT][xX][tT]$")){
                    map.put("img",R.drawable.book_txt_icon);
                }
                else if (fileFlag.getName().matches("^(.*?)[.][jJ][pP][gG]$")){
                    map.put("img",R.drawable.jpg_icon);
                }
                else if (fileFlag.getName().matches("^(.*?)[.][mM][pP][4]$")){
                    map.put("img",R.drawable.mp4_icon);
                }
                else if (fileFlag.getName().matches("^(.*?)[.][z][i][p]$")){
                    map.put("img",R.drawable.jpg_icon);
                }
                else {
                    map.put("img",R.drawable.file_icon);
                }

                map.put("name",fileFlag.getName());
                filelist.add(map);
            }
        }

        //按照名字排序文件
        Collections.sort(filelist, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                return ((String)o1.get("name")).compareTo((String)(o2.get("name")));
            }
        });

        //整合两个list
        list.addAll(filelist);

        //如果目录不是sd卡片跟目录，那么应该加上一个返回上一级目录的提示
        if(!file.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getPath())){
            HashMap<String ,Object> map=new HashMap<>();
            map.put("img",R.drawable.directory_icon);
            map.put("name","返回上一级");
            list.add(0,map);
        }

        return list;
    }

    /**
     * 进入另一个的目录
     * @param path  目录的路径
     */
    private void toPath(String path){
        lastPosition=position;
        Log.i("测试","position:"+position+"lastPosition:"+lastPosition);
        //更新列表数据
        historyPath=path;
        dataList.clear();
        dataList.addAll(getDataList(historyPath));
        adapter.notifyDataSetChanged();
        mListView.setSelection(0);

    }

    /**
     * 返回上一级目录
     * @param path  目录
     */
    private void backToPath(String path){
        //更新列表数据
        historyPath=path;
        dataList.clear();
        dataList.addAll(getDataList(historyPath));
        adapter.notifyDataSetChanged();
        Log.i("测试","position:"+position+"lastPosition:"+lastPosition);
        mListView.setSelection(lastPosition);
    }

//    /**
//     * 修改返回按键的事件
//     */
//    @Override

    /**
     * 对返回按钮的事件处理，返回事件处理是否执行
     * @return
     */
    public boolean onBackPressed() {
        if(historyPath.equals(Environment.getExternalStorageDirectory().getPath())){
            //返回按钮 事件不执行，返回false给上一个层级
            return false;
        }else {
            String flags[]=historyPath.split("/");
            String pathFlag="";
            for (int i=0;i<flags.length-1;i++){
                pathFlag=pathFlag+flags[i]+"/";
            }
            pathFlag=pathFlag.substring(0,pathFlag.length()-1);
            backToPath(pathFlag);
            //返回按钮 事件执行
            return true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Adapter adapter=parent.getAdapter();
        HashMap<String ,Object> map=(HashMap<String ,Object>)adapter.getItem(position);
        HashMap<String,String> nameMapFlag=getNameMapList(historyPath);
        Log.i("点击的项目是",(String)map.get("name"));
        if(((String)map.get("name")).equals("返回上一级")){
            //重新构造上一级目录
            String flags[]=historyPath.split("/");
            String pathFlag="";
            for (int i=0;i<flags.length-1;i++){
                pathFlag=pathFlag+flags[i]+"/";
            }
            pathFlag=pathFlag.substring(0,pathFlag.length()-1);

            backToPath(pathFlag);
        }else {
            File file=new File(nameMapFlag.get(map.get("name")));
            String name="";
            if(file.isDirectory()){
                name=file.getAbsolutePath();
                Log.i("选中的路径为:",name);

                toPath(name);
            }else {
                name=file.getAbsolutePath();
                Log.i("选中的文件为:",name);
            }
        }
    }

}
