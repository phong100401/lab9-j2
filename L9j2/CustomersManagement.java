package L9j2;

import java.sql.*;
import java.util.Scanner;

public class CustomersManagement {
    private CustomersModel model;
    private Scanner sc = new Scanner(System.in);

    public void add() {
        model = new CustomersModel();
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            System.out.println("Please enter info of new customer");
            System.out.println("==================================");
            System.out.print("Please enter new id: ");
            model.setCustomerID(Integer.parseInt(sc.nextLine()));
            System.out.print("Please enter new name: ");
            model.setName(sc.nextLine());
            System.out.print("PLease enter new gender: ");
            model.setGender(Byte.parseByte(sc.nextLine()));
            System.out.print("Please enter new level: ");
            model.setLevel(Byte.parseByte(sc.nextLine()));

            String strAdd = "insert into customers(CustomerID, Name, Gender, level) values " +
                    "(" + model.getCustomerID() + ", '" + model.getName() + "', " +
                    model.getGender() + ", " + model.getLevel() + ")";
            System.out.println("The SQL Statement is: " + strAdd);
            int numInserted = st.executeUpdate(strAdd);
            System.out.println(numInserted + " records updated.");
            String strShowEdit = "select * from customers;";
            showEdit(st, strShowEdit);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void edit() {
        model = new CustomersModel();
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            System.out.print("Please input the id you want to edit: ");
            int editID = Integer.parseInt(sc.nextLine());
            String strShowEdit = "select * from customers where CustomerID = " + editID + ";";
            System.out.println("The strShowEdit SQL: " + strShowEdit);
            showEdit(st, strShowEdit);

            String editAddress;
            int editGender, editLevel;

            System.out.println("Please enter new info of customer");
            System.out.println("==================================");
            System.out.print("Please enter new address : ");
            editAddress = sc.nextLine();
            System.out.print("Please enter new gender: ");
            editGender = Integer.parseInt(sc.nextLine());
            System.out.print("Please enter new level: ");
            editLevel = Integer.parseInt(sc.nextLine());
            System.out.println();

            String strEditPrice = "update customers\n" +
                    "    set Address = '" + editAddress + "',\n" +
                    "        Gender = " + editGender + ", \n" +
                    "        level = " + editLevel  + " \n" +
                    "    where CustomerID = " + editID + ";\n";
            System.out.println("The SQL statement: \n" + strEditPrice);
            int numCol = st.executeUpdate(strEditPrice);
            System.out.println(numCol + " records updated");
            showEdit(st, strShowEdit);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void del() {
        model = new CustomersModel();
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            System.out.println("Records for customers are as below:");
            String strShowEdit = "select * from customers";
            showEdit(st, strShowEdit);
            System.out.print("Please enter the id of the customer you want to delete: ");
            int delID = Integer.parseInt(sc.nextLine());
            String strSelectAll = "select * from orders;";
            ResultSet rsAll = st.executeQuery(strSelectAll);

            rsAll.next();
            if (delID == rsAll.getInt("CustomerID")) {
                System.out.println("You cannot delete this book!!!");
            } else {
                String strDel = "delete from customers\n" +
                        "    where CustomerID = " + delID + ";";
                System.out.println("The SQL Del Statement is: " + strDel);
                int numDels = st.executeUpdate(strDel);
                System.out.println(numDels + " records affected.");

                System.out.println("Records updated are as below:");
                showEdit(st, strShowEdit);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void showTop10() {
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            String strShowTop10 = "\nselect * from customers\n" +
                    "    order by level desc\n" +
                    "    limit 10;";
            showEdit(st, strShowTop10);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static void showEdit(Statement st, String strShowEdit) throws SQLException {
        ResultSet rs = st.executeQuery(strShowEdit);
        ResultSetMetaData rsMD = rs.getMetaData();

        int numCols = rsMD.getColumnCount();
        for (int i=1; i<=numCols; i++) {
            System.out.printf("%-30s", rsMD.getColumnName(i));
        }
        System.out.println();

        while (rs.next()) {
            for (int i=1; i<=numCols; i++) {
                System.out.printf("%-30s", rs.getString(i));
            }
            System.out.println();
        }
    }
}
