package ebookstore.Controller;


import ebookstore.Model.Book;
import ebookstore.Model.Cart;
import ebookstore.Model.Users;

import java.sql.*;
import java.util.List;

public class CartController {
    String url = "jdbc:mysql://127.0.0.1:3306/ebookstore";
    String username = "root";
    String password = "";

    public Cart getItem(Book book){
        Cart obj = new Cart();
        try (
                Connection conn = DriverManager.getConnection(url, username,password);
                Statement stmt = conn.createStatement();
        ) {
            String select = "Select * from books where bookID = "+book.getBookID();
            ResultSet rset = stmt.executeQuery(select);
            System.out.printf("%-30s%-30s%-30s%-30s%-30s\n","bookID","title","price","qty","discount");
            while (rset.next()) {
                int id = rset.getInt("bookID");
                String title = rset.getString("title");
                double price = rset.getDouble("price");
                obj = new Cart(id, title, price, book.getQty());
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
    public boolean afterPurchase(List<Cart> list, Users user) {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            double sum = 0;
            for (int i = 0; i < list.size(); i++) {
                sum = sum + list.get(i).getPrice() * list.get(i).getQty();
            }
            conn.setAutoCommit(false);
            String insert = "insert into orders (customerID,discount,total,orderdate,status,createddate) values(" + user.getId() + ",0," + sum + ",current_Date()," + "1," + "current_Date())";
            stmt.executeUpdate(insert);
            String select = "select * from orders order by orderID DESC LIMIT 1";
            ResultSet rset = stmt.executeQuery(select);
            int orderID = 0;
            while (rset.next()){
                orderID = rset.getInt("orderID");
            }
            for (int i = 0; i < list.size(); i++) {
                String update = "update books set qty = qty -" + list.get(i).getQty() + " where bookID = " + list.get(i).getBookID();
                stmt.executeUpdate(update);
                String insert2 = "insert into orderdetail(orderID, bookID,title,amount,price,createddate) values(" + orderID + "," + list.get(i).getBookID() + ",'" + list.get(i).getTitle() + "'," + list.get(i).getQty() + "," + list.get(i).getPrice() + "," + "CURRENT_DATE())";
                stmt.executeUpdate(insert2);
            }
            conn.commit();
            return true;
        } catch (SQLException ex) {
            System.out.println("Failed! Please try again");
            ex.printStackTrace();
            return false;
        }
    }
}
