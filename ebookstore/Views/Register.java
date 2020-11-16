package ebookstore.Views;

import ebookstore.Model.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Register {
    List<Users> UserList = new ArrayList<>();
    public List<Users> loading() {
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement stmt = conn.createStatement();
        ) {
            String select = "Select * from users";
            ResultSet rset = stmt.executeQuery(select);
            while (rset.next()) {
                Users obj = new Users();
                obj.setId(rset.getInt("id"));
                obj.setUsername(rset.getString("name"));
                obj.setPassword(rset.getString("password"));
                obj.setRole(rset.getInt("role"));
                obj.setCreate(rset.getString("createddate"));
                obj.setUpdate(rset.getString("updateddate"));
                UserList.add(obj);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return UserList;
    }

    public boolean signUp(Users user) {
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement stmt = conn.createStatement();
        ) {
            String signup = "insert into users(name,password,role,createddate) values('" + user.getUsername() + "','" + user.getPassword() + "'," + user.getRole() + "," + "CURRENT_DATE())";

            stmt.executeUpdate(signup);
            loading();
            System.out.println("Signup successfully");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Users signIn() {
        Users obj = new Users();
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement stmt = conn.createStatement();
        ) {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter your username: ");
            String name = input.nextLine();
            System.out.println("Enter your password: ");
            String pass = input.nextLine();
            String select = "Select * from users where name ='" + name + "' AND password ='" + pass + "'";
            ResultSet rset = stmt.executeQuery(select);
            int row = 0;
            while (rset.next()) {
                int id = rset.getInt("id");
                String username = rset.getString("name");
                String password=rset.getString("password");
                int role=rset.getInt("role");
                String create = rset.getString("createddate");
                String update=rset.getString("updateddate");
                obj = new Users(id,username,password,role,create,update);
                ++row;
            }
            if(row==0){
                System.out.println("Wrong username or password");
            }else
                System.out.println("Sign in successfully");
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return obj;
    }
    public void displayUser(){
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement stmt = conn.createStatement();
        ) {
            String stt = "Select * from users";
            ResultSet rset = stmt.executeQuery(stt);
            ResultSetMetaData rsetMD = rset.getMetaData();
            int numColums = rsetMD.getColumnCount();
            for (int i = 1; i <= numColums; i++) {
                System.out.printf("%-30s", rsetMD.getColumnName(i));
            }
            loading();
            for(int i = 0; i< UserList.size(); i++){
                System.out.println(UserList.get(i).toString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
