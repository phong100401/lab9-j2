package ebookstore.Controller;

import ebookstore.Model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookController {
    String url = "jdbc:mysql://127.0.0.1:3306/ebookstore";
    String username = "root";
    String password = "";

    List<Book> BookList = new ArrayList<>();

    public List<Book> loading() {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            String select = "Select * from books";
            ResultSet rset = stmt.executeQuery(select);
            while (rset.next()) {
                int id = rset.getInt("bookID");
                String title = rset.getString("title");
                String author = rset.getString("author");
                int years = rset.getInt("years");
                int genre = rset.getInt("genre");
                int qty = rset.getInt("qty");
                int status = rset.getInt("status");
                double price = rset.getDouble("price");
                String createddate = rset.getString("createddate");
                String updateddate = rset.getString("updateddate");
                Book obj = new Book(id, title, author, years, genre, status, qty, price, createddate, updateddate);
                BookList.add(obj);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return BookList;
    }

    public boolean insertBook(Book book) {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            String insert = "Insert into books(bookId,title,author,qty,price) values(" + book.getBookID() + ",'" + book.getTitle() + "','" + book.getAuthor() + "'," + book.getQty() + "," + book.getPrice() + ")";
            stmt.executeUpdate(insert);
            loading();
            System.out.println("Book has been inserted");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int updateBook(Book book) {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            String update = "update books set qty=" + book.getQty() + ",price=" + book.getPrice() + " where bookID =" + book.getBookID();
            int count = stmt.executeUpdate(update);
            loading();
            System.out.println("Book has been updated");
            return 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean deleteBook(Book book) {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            String delete = "delete from books where bookID =" + book.getBookID();
            stmt.executeUpdate(delete);
            loading();
            System.out.println("Book has been deleted");
            return true;
        } catch (SQLException ex) {
            System.out.println("Cannot delete this book");
            ex.printStackTrace();
            return false;
        }
    }

    public void displayBook() {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            String stt = "Select * from books";
            ResultSet rset = stmt.executeQuery(stt);
            ResultSetMetaData rsetMD = rset.getMetaData();
            int numColums = rsetMD.getColumnCount();
            for (int i = 1; i <= numColums; i++) {
                System.out.printf("%-15s", rsetMD.getColumnName(i));
            }
            loading();
            System.out.println();
            while (rset.next()) {
                for (int i = 1; i <= numColums; i++) {
                    System.out.printf("%-15s", rset.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int checkBookByID(int id) {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            ResultSet rset = stmt.executeQuery("Select * from books where bookID = "+id);
            int row = 0;
            while(rset.next()){
                ++row;
            }
            if(row == 0){
                System.out.println("Please enter the correct ID");
                return 1;
            } else
                return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }
    public int checkBookAmount(int id, int qty){
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
        ) {
            ResultSet rset = stmt.executeQuery("Select * from books where bookID = "+id);
            int amount = 0;
            while(rset.next()){
                amount = rset.getInt("qty");
            }
            if(qty > amount){
                System.out.println("We dont have enough books");
                return 1;
            } else
                return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }return -1;
    }
}
