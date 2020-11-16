package ebookstore.Views;

import ebookstore.Controller.CustomerController;
import ebookstore.Controller.UserController;
import ebookstore.Model.Customer;
import ebookstore.Model.Users;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class UserView {
    Scanner input = new Scanner(System.in);
    public String slt;
    public ResultSet rst;


    public int checkUser(Users user) {
        BookShop bookShop = new BookShop();
        int y = user.getRole();
        if (y == 1) {
            bookShop.menuCustomer(user);
            return 1;
        } else if (y == 2) {
            bookShop.menuAdmin(user);
            return 2;
        }
        return 0;
    }

    public Users createAccount(){
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ebookstore", "root", "");
                Statement stmt = conn.createStatement();
        ) {
            try {
                conn.setAutoCommit(false);
                Scanner input = new Scanner(System.in);
                CustomerController ctr2 = new CustomerController();
                UserController userCtr = new UserController();
                String account;
                int j = 0;
                do {
                    System.out.println("(SignUp)Enter your account:");
                    account = input.nextLine();
                    j = userCtr.checkAccount(account);
                } while (j != 0);
                System.out.println("(SignUp)Enter your password:");
                String password = input.nextLine();
                System.out.println("(SignUp)Enter your role: (1-customer, 2-admin)");
                int role = Integer.parseInt(input.nextLine());
                Users obj = new Users(account, password, role);
                String signup = "insert into users(name,password,role,createddate) values('" + obj.getUsername() + "','" + obj.getPassword() + "'," + obj.getRole() + ",CURRENT_DATE())";
                stmt.executeUpdate(signup);
                List<Users> list = userCtr.loading();
                int id = 0;
                String name = null;
                String address = null;
                String email = null;
                String phone = null;
                if (role == 1) {
                    int i = list.size() - 1;
                    id = list.get(i).getId();
                    System.out.println("(SignUp)Enter customer name: ");
                    name = input.nextLine();
                    System.out.println("(SignUp)Enter customer address: ");
                    address = input.nextLine();
                    j = 0;
                    do {
                        System.out.println("(SignUp)Enter customer email: ");
                        email = input.nextLine();
                        j = ctr2.checkEmail(email);
                        if (j != 1) {
                            System.out.println("This email already exists. Enter another email!");
                        }
                    } while (j != 1);
                    do {
                        System.out.println("(SignUp)Enter customer phone: ");
                        phone = input.nextLine();
                        j = ctr2.checkPhone(phone);
                        if (j != 1) {
                            System.out.println("This phone number already exists. Enter another phone number!");
                        }
                    } while (j != 1);
                    int member = 4;
                    Customer cus = new Customer(id, name, address, email, phone, member);
                    String insert = "Insert into customers(customerID,name,address,email,phone,member) values(" + cus.getCustomerID() + ",'" + cus.getName() + "','" + cus.getAddress() + "','" + cus.getEmail() + "','" + cus.getPhone() + "'," + cus.getMember() + ")";
                    stmt.executeUpdate(insert);
                    conn.commit();
                    return obj;
                }else if (role ==2){
                    System.out.println("Signup successful");
                    BookShop shop = new BookShop();
                    shop.menuAdmin(obj);
                    return obj;
                }
            } catch (SQLException ex) {
                System.out.println("Signup failed");
                conn.rollback();
                return null;
            }
        }catch(SQLException ex){
            System.out.println("Signup failed");
            ex.printStackTrace();
        }return null;
    }
}

