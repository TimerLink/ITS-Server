package com.timerlink.httpservlet;

import java.sql.*;


public class JDBCcon {

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/itsystemdb", "root", "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void insert(int i, double l, double b, int n) throws SQLException {
        Connection conn = getConnection();
        String sql = "INSERT INTO tbl_user(id, longitude, latitude, number)" +
                "VALUES(" + i + "," + l + "," + b + "," + n + ")";//用来插入记录
        Statement st = conn.createStatement();
        int count = st.executeUpdate(sql);
        System.out.println("向用户表中插入了" + count + "条记录");
        conn.close();
    }

    public void update(int i, double l, double b, int n) throws SQLException {
        Connection conn = getConnection();
        String sql = "update tbl_user set longitude=" + l + ",latitude=" + b + ",number=" + n + " where id = " + i;
        Statement st = conn.createStatement();
        int count = st.executeUpdate(sql);
        System.out.println("向用户表中更新了" + count + "条记录");
        conn.close();
    }

    public TinyPacket select(int i) throws SQLException {
        Connection conn = getConnection();
        String sql = "select longitude ,latitude ,number from tbl_user where id =" + i;
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        double longitude = 0;
        double latitude = 0;
        int number = 1;
        while (rs.next()) {
            longitude = rs.getDouble("longitude");
            latitude = rs.getDouble("latitude");
            number = rs.getInt("number");
        }
        rs.close();
        conn.close();
        TinyPacket tinyPacket = new TinyPacket();
        tinyPacket.latitude = latitude;
        tinyPacket.longitude = longitude;
        tinyPacket.number = number;
        return tinyPacket;
        //SELECT itemList FROM table Where id = 1
    }
}
/*
      //STEP 5: Extract data from result set
      while(rs.next()){
         //Retrieve by column name
         int id  = rs.getInt("id");
         int age = rs.getInt("age");
         String first = rs.getString("first");
         String last = rs.getString("last");

         //Display values
         System.out.print("ID: " + id);
         System.out.print(", Age: " + age);
         System.out.print(", First: " + first);
         System.out.println(", Last: " + last);
      }
      rs.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            conn.close();
      }catch(SQLException se){
      }// do nothing
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}//end JDBCExample*/