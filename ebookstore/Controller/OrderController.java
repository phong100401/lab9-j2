package ebookstore.Controller;

import ebookstore.Model.Cart;
import ebookstore.Model.Order;
import ebookstore.Model.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    String url = "jdbc:mysql://127.0.0.1:3306/ebookstore";
    String username = "root";
    String password = "";
    List<Order> orderList = new ArrayList<>();

    public List<Order> loading() {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            String select = "Select * from orders";
            ResultSet rset = stmt.executeQuery(select);
            while (rset.next()) {
                int Oid = rset.getInt("orderID");
                int Cid = rset.getInt("customerID");
                int discount = rset.getInt("discount");
                double price = rset.getDouble("total");
                String orderdate = rset.getString("orderdate");
                int status = rset.getInt("status");
                String createddate = rset.getString("createddate");
                String updateddate = rset.getString("updateddate");
                Order obj = new Order(Oid, Cid, discount, price, orderdate, status, createddate, updateddate);
                orderList.add(obj);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orderList;
    }

    public void insertOrder(List<Cart> list, Users user) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            double sum = 0;
            for (int i = 0; i < list.size(); i++) {
                sum = sum + list.get(i).getPrice() * list.get(i).getQty();
            }
            String insert = "insert into orders (customerID,discount,total,orderdate,status,createddate) values(" + user.getId() + ",0" + "," + sum + ",current_Date()," + "1," + "current_Date())";
            stmt.executeUpdate(insert);
            loading();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayOrder(Users user) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            String slt = "Select * from orders where customerID =" + user.getId() + " order by orderID DESC LIMIT 1 ";
            ResultSet rst = stmt.executeQuery(slt);
            ResultSetMetaData rset = rst.getMetaData();
            int column = rset.getColumnCount();
            for (int i = 1; i <= column; i++) {
                System.out.printf("%-30s", rset.getColumnName(i));
            }
            System.out.println();
            while (rst.next()) {
                for (int i = 1; i <= column; i++) {
                    System.out.printf("%-30s", rst.getString(i));
                }
                System.out.println();
            }
            System.out.println("**Status: 0-Canceled, 1-Confirmed, 2-Pending,3-Ready,4-Delivering,5-Delivered");
            System.out.println("============================================================================================");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
