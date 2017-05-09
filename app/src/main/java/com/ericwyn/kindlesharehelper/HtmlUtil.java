package com.ericwyn.kindlesharehelper;

import com.ericwyn.kindlesharehelper.fragment.MainFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 在这个类里面构建Html网页源代码
 * Created by ericwyn on 17-5-4.
 */

public class HtmlUtil {

    /**
     * 构造Html网页的方法
     * @param dataList  穿进去数据List，
     *                  每个Map包含一个name代表文件名称，一个size代表文件大小,一个path代表路径，一个port代表下载端口
     * @return
     */

    public static String makeHtml(ArrayList<HashMap<String,Object>> dataList){
        StringBuffer result=new StringBuffer();
        result.append(head);

        /* 循环类似于生成一下的Html代码
        <tr>
            <td>
                &nbsp;&nbsp;
                <a href="http://192.168.199.154:9999" download="欢迎文件.txt.txt">欢迎文件.txt</a>
                &nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td>
                &nbsp; 1 kB &nbsp;
            </td>
        </tr>
        */

        for (HashMap map:dataList){
            result.append("<tr>\n" +
                    "     <td >\n" +
                    "     &nbsp&nbsp<a href=\""+"http://"+ MainFragment.getLocalIpStr(MainFragment.mContext)+":"+map.get("port")+
                    "\" download=\""+(String )map.get("name")+"\">"+(String )map.get("name")+"</a>&nbsp&nbsp&nbsp&nbsp</td>\n" +
                    "     <td>&nbsp "+(String )map.get("size")+" kB &nbsp</td>\n" +
                    "     </tr>");
        }

        result.append(tail);
        return result.toString();
    }

    //html上半部分
    private static String head=
            "<!DOCTYPE html>\n" +
            "<html lang=\"zh\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <title id=\"title\">KindleHelper 文件下载页</title>\n" +
            "    <style type=\"text/css\">\n" +
            "      h1{\n" +
            "        font-size:46px\n" +
            "      }\n" +
            "    </style>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "<h1>KindleHelper 文件下载页</h1>\n" +
            "<table style=\"font-size:24px\">\n" +
            "    <thead>\n" +
            "    <tr class=\"header\" id=\"theader\">\n" +
            "        <th>名称</th>\n" +
            "        <th>大小</th></tr>\n" +
            "    </thead>\n" +
            "    <tbody style=\"\">";

    //html下半部分
    private static String tail=
            "    </tbody>\n" +
            "</table>\n" +
            "</body>\n" +
            "\n" +
            "</html>";

}
