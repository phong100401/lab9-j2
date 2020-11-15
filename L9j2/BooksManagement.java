package L9j2;

import java.sql.*;
import java.util.Scanner;

public class BooksManagement {
    private BooksModel model;
    private static final Scanner sc = new Scanner(System.in);

    public void add() {
        model = new BooksModel();
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            System.out.print("Please enter the ID of the new book: ");
            model.setBookID(Integer.parseInt(sc.nextLine()));
            System.out.print("Please enter the name of the book to add: ");
            model.setName(sc.nextLine());
            System.out.print("Please enter the price for the book: ");
            model.setPrice(Double.parseDouble(sc.nextLine()));
            System.out.print("Please enter the status of the book (1: active, 2: ordering, 3: out of order): ");
            model.setStatus(Byte.parseByte(sc.nextLine()));

            String strAdd = "insert into books(BookId, Name, Price, qty, status) values (" + model.getBookID() + ", '" +
                    model.getName() + "', " + model.getPrice() + ", " + model.getQty() + ", " + model.getStatus() + ")";
            System.out.println("The SQL Statement is: " + strAdd);
            int numAdd = st.executeUpdate(strAdd);
            System.out.println(numAdd + " records inserted");

            String strCheckAdd = "select * from books";
            System.out.println("The SQL Statement is: " + strCheckAdd);
            showEdit(st, strCheckAdd);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void edit() {
        model = new BooksModel();
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            System.out.print("Please input the id you want to edit: ");
            int editID = Integer.parseInt(sc.nextLine());
            String strShowEdit = "select * from books where BookId = " + editID + ";";
            System.out.println("The strShowEdit SQL: " + strShowEdit);
            showEdit(st, strShowEdit);

            int editPrice, editQty;
            System.out.print("What do you want to edit (1 for price, 2 for qty, 3 for both): ");
            int editChoice = Integer.parseInt(sc.nextLine());
            String flag = "N";
            do {
                switch (editChoice) {
                    case 3:
                        System.out.print("Please enter new price for the book: ");
                        editPrice = Integer.parseInt(sc.nextLine());
                        System.out.print("Please enter new qty for the book: ");
                        editQty = Integer.parseInt(sc.nextLine());

                        String strEditPrice = "update books\n" +
                                "    set Price = " + editPrice + " \n" +
                                "    where BookId = " + editID + ";";
                        System.out.println("The SQL statement: " + strEditPrice);
                        int numCol = st.executeUpdate(strEditPrice);
                        System.out.println(numCol + " records updated\n");

                        String strEditQty = "update books\n" +
                                "    set qty = " + editQty + " \n" +
                                "    where BookId = " + editID + ";";
                        System.out.println("The SQL statement: " + strEditQty);
                        numCol = st.executeUpdate(strEditQty);
                        System.out.println(numCol + " records updated");

                        showEdit(st, strShowEdit);
                        break;
                    case 1:
                        System.out.print("Please enter new price for the book: ");
                        editPrice = Integer.parseInt(sc.nextLine());

                        strEditPrice = "update books\n" +
                                "    set Price = " + editPrice + " \n" +
                                "    where BookId = " + editID + ";";
                        System.out.println("The SQL statement: " + strEditPrice);
                        numCol = st.executeUpdate(strEditPrice);
                        System.out.println(numCol + " records updated");
                        showEdit(st, strShowEdit);
                        break;
                    case 2:
                        System.out.print("Please enter new qty for the book: ");
                        editQty = Integer.parseInt(sc.nextLine());
                        strEditQty = "update books\n" +
                                "    set qty = " + editQty + " \n" +
                                "    where BookId = " + editID + ";";
                        System.out.println("The SQL statement: " + strEditQty);
                        numCol = st.executeUpdate(strEditQty);
                        System.out.println(numCol + " records updated");
                        showEdit(st, strShowEdit);
                        break;
                    default:
                        System.out.println("You input the wrong action..., wanna try again?? (Y/N)");
                        flag = sc.nextLine();
                }
            } while (flag.equalsIgnoreCase("y"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void  del() {
        model = new BooksModel();
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement st = con.createStatement()
        ) {
            System.out.println("Below is the records for all of the books:");
            String strShowEdit = "select * from books";
            showEdit(st, strShowEdit);
            System.out.print("Please enter the id of the book you want to delete: ");
            int delID = Integer.parseInt(sc.nextLine());
            String strSelectAll = "select * from ordersdetails;";
            ResultSet rsAll = st.executeQuery(strSelectAll);

            rsAll.next();
            if (delID == rsAll.getInt("bookID")) {
                System.out.println("You cannot delete this book!!!");
            } else {
                String strDel = "delete from books\n" +
                        "    where bookID = " + delID + ";";
                System.out.println("The SQL Del Statement is: " + strDel);
                int numDels = st.executeUpdate(strDel);
                System.out.println(numDels + " records affected.");

                System.out.println("Updated Records are as below:");
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
            String strShowTop10 = "\nselect * from books\n" +
                    "    order by year desc\n" +
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
