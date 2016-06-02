package com.timerlink.httpservlet;

import com.alibaba.fastjson.JSON;
import com.timerlink.jsonUtil.JSONDate;
import com.timerlink.jsonUtil.JsonUis;
import com.timerlink.jsonUtil.Person;
import com.timerlink.jsonUtil.SensorData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public class HttpClient extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 15L;

    public HttpClient() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String title = "位置";
//        String docType =
//                "<!doctype html public \"-//w3c//dtd html 4.0 " +
//                        "transitional//en\">\n";
//        out.println(docType +
//                "<html>\n" +
//                "<head><title>" + title + "</title></head>\n" +
//                "<body bgcolor=\"#f0f0f0\">\n" +
//                "<h1 align=\"center\">" + title + "</h1>\n" +
//                "<ul>\n" +
//                "  <li><b>经度</b>："
//                + request.getParameter("l") + "\n" +
//                "  <li><b>纬度</b>："
//                + request.getParameter("b") + "\n" +
//                "</ul>\n" +
//                "</body></html>");
        /**
         * 衔接UDP,数据库操作
         */
        JDBCcon con = new JDBCcon();
        double l = Double.parseDouble(request.getParameter("l"));
        double b = Double.parseDouble(request.getParameter("b"));
        TinyPacket tinyPacket = new TinyPacket();
        try {
            con.update(8,l,b,11);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("插入出错!");
        }
        /**
         * 查询数据
         */
        try {
            tinyPacket = con.select(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.println(JSON.toJSONString(tinyPacket));

        /**
         * JSONData数据类
         */
        //126.631026,45.783751 兆麟公园
        JSONDate p = new JSONDate();
        p.error = "good";

        SensorData sensorData = new SensorData();
        sensorData.sensorId = 1;
        sensorData.lpNumber = "busG";
//        SensorData.Packet packet = new Package(0,45.783751,'a',126.631026,'a',0f,10,1,9);
//        OuterClass.InnerClass innerClass = outerClass.new InnerClass();
        SensorData.Packet packet = sensorData.new Packet();
        packet.latitude = 45.783751f;
        packet.longitude = 126.631026f;
        packet.latitude = (float) tinyPacket.latitude;
        packet.longitude = (float) tinyPacket.longitude;

        packet.ew = 'a';
        packet.ns = 'a';
        packet.passengerDown = 1;
        packet.passengerUp = 10;
        packet.passengerTotal = 9;
        packet.speed = 0;
        packet.timestamp = 0;

        sensorData.data.add(packet);
        p.data.add(sensorData);

//        out.println(JSON.toJSONString(tinyPacket));
        /**
         * 测试Person类没问题
         */
//        Person p = new Person();
//        p.busLatitude = 120;
//        p.busLongitude = 46;
//        p.personNumber = 9;
        out.println(JSON.toJSONString(p));
    }
}
