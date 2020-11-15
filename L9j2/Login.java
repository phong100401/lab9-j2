package L9j2;

import java.sql.*;
import java.util.Scanner;

public class Login {
    private UserModel model;
    Scanner sc = new Scanner(System.in);

    public void register() {
        model = new UserModel();
        System.out.print("Please enter new username: ");
        model.setUserName(sc.nextLine());
        System.out.print("Please enter new user password: ");
        model.setUserPassword(sc.nextLine());
        System.out.print("Enter role of User: ");
        int role = Integer.parseInt(sc.nextLine());
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            String strInsert = "insert into users values (null, '" + model.getUserName() + "', '" + model.getUserPassword() + "', " + role + ")";
            System.out.println("\nThe SQL Statement is: " + strInsert);
            int numUpdateCount = st.executeUpdate(strInsert);
            System.out.println(numUpdateCount + " records inserted");

            String strCheckInsert = "select * from users";
            System.out.println("\nThe SQL statement is: " + strCheckInsert);
            BooksManagement.showEdit(st, strCheckInsert);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void login(UserModel model) {
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            boolean again;
            do {
                System.out.print("Please enter username: ");
                model.setUserName(sc.nextLine());
                System.out.print("Please enter user password: ");
                model.setUserPassword(sc.nextLine());
                String strLogin = "select count(1) from users\n" +
                        "    where username = '" + model.getUserName() + "' and password = '" + model.getUserPassword() + "';";
                System.out.println("The SQL Statement is: " + strLogin);
                ResultSet rs = st.executeQuery(strLogin);

                rs.next();
                if (rs.getInt("count(1)") == 1) {
                    System.out.println("Login Successfully");
                    again = true;
                } else {
                    System.out.println("You have failed logging in, please try again :(");
                    again = false;
                }
                System.out.println();
            } while (!again);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
