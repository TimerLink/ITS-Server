package com.timerlink.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import com.timerlink.httpservlet.JDBCcon;

public class UDPsocket {
    public void UDPserver(){
        Connection conn = null;
        String str_send = "Hello UDPclient";
        byte[] buf = new byte[1024];
        //服务端在3000端口监听接收到的数据
        try {
            DatagramSocket ds = new DatagramSocket(3000);
            DatagramPacket dp_receive = new DatagramPacket(buf, 1024);
            System.out.println("server is on，waiting for client to send data......");
            boolean f = true;
            while(f){
                //服务器端接收来自客户端的数据
                ds.receive(dp_receive);
                System.out.println("server received data from client：");
                String str_receive = new String(dp_receive.getData(),0,dp_receive.getLength()) +
                        " from " + dp_receive.getAddress().getHostAddress() + ":" + dp_receive.getPort();
                System.out.println(str_receive);
                //数据发动到客户端的3000端口
                DatagramPacket dp_send= new DatagramPacket(str_send.getBytes(),str_send.length(),dp_receive.getAddress(),9000);
                ds.send(dp_send);
                //由于dp_receive在接收了数据之后，其内部消息长度值会变为实际接收的消息的字节数，
                //所以这里要将dp_receive的内部消息长度重新置为1024
                dp_receive.setLength(1024);
                int id = 0;
                double longi = 0;
                double latitu = 0;
                int num = 0;
                try {
                    String str[] = str_receive.split("&");
                    id = Integer.parseInt(str[0]);
                    longi = Double.parseDouble(str[1]);
                    latitu = Double.parseDouble(str[2]);
                    num = Integer.parseInt(str[3]);
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                    System.out.println("数据格式错误");
                }
                try {
                    JDBCcon con = new JDBCcon();
                    conn = con.getConnection();
                    conn.setAutoCommit(false);
                    con.update(id,longi,latitu,num);
                    conn.commit();
                } catch (SQLException e) {
                    System.out.println("======捕获SQL异常======");
                    e.printStackTrace();
                    try {
                        conn.rollback();
                        System.out.println("事务回滚成功");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }finally {
                        try {
                            if (conn!=null) {
                                conn.close();
                            }
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                }
            }
            ds.close();
        }catch (SocketException e){
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
