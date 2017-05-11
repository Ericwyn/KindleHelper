package com.ericwyn.kindlesharehelper.httpServers;


import com.ericwyn.kindlesharehelper.utils.HtmlUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 主页的http服务类
 * Created by Ericwyn on 2017/2/1.
 */

public class HttpServer extends fi.iki.elonen.NanoHTTPD {

    public static final int DEFAULT_SERVER_PORT=1111;
    private static ArrayList<HashMap<String,Object>> data;

    public HttpServer(ArrayList<HashMap<String,Object>> list){
        super(DEFAULT_SERVER_PORT);
        data=list;
    }

    @Override
    public Response serve(IHTTPSession session) {

        return newFixedLengthResponse(HtmlUtil.makeHtml(data));
    }


}
