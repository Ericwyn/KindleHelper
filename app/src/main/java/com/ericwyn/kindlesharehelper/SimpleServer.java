package com.ericwyn.kindlesharehelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;

/**
 * 文件下载server
 * Created by ericwyn on 17-5-4.
 */

public class SimpleServer extends NanoHTTPD {

    public static void main(String[] args) {
        ServerRunner.run(SimpleServer.class);
    }
    String filePath="";

    public SimpleServer(int port,String filePath) {
        super(port);
        this.filePath=filePath;
    }
    //http://192.168.199.154:10010/
    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        String username = session.getQueryParameterString();

        System.out.println("method "+method+" uri "+uri+" "+username);

        InputStream inputStream;
        Response response = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
            response = newChunkedResponse(Response.Status.OK, "application/octet-stream", inputStream);//这代表任意的二进制数据传输。
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] temp=filePath.split("/");

        response.addHeader("Content-Disposition", "attachment; filename="+temp[temp.length-1]);
        return response;

    }
}
