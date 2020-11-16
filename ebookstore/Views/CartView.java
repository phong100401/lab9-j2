package ebookstore.Views;

import ebookstore.Controller.*;
import ebookstore.Model.Book;
import ebookstore.Model.Cart;
import ebookstore.Model.Checkout;
import ebookstore.Model.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CartView {
    public static String slt;
    public static ResultSet rst;


    String url = "jdbc:mysql://127.0.0.1:3306/ebookstore";
    String username = "root";
    String password = "";


    public List<Cart> addToCart() {
        List<Cart> items = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        BookController bookCtr= new BookController();
        char choice = 0;
        int i=0;
        int id = 0;
        int qty;
        int sum =0;
        do {
            System.out.println("Proceed/Continue purchasing book? (Y/N)");
            choice = scanner.nextLine().charAt(0);
            switch (choice) {
                case 'y':
                    Book book = new Book();
                    do {
                        System.out.println("Enter book ID you want to buy: ");
                        id = Integer.parseInt(scanner.nextLine());
                        i = bookCtr.checkBookByID(id);
                        book.setBookID(id);
                    }while(i !=0);
                    do {

                        System.out.println("Enter amount you want to buy: ");
                        qty = Integer.parseInt(scanner.nextLine());
                        if(qty <0){
                            System.out.println("Enter another number");
                            i=1;
                        }
                        else{
                            sum += qty;
                            i = bookCtr.checkBookAmount(id,sum);
                            if(i!=0){
                                sum -= qty;
                            }
                            book.setQty(qty);}
                    }while(i !=0);
                    CartController obj = new CartController();
                    Cart item = obj.getItem(book);
                    items.add(item);
                    for(int x = 0;x<items.size();x++){
                        System.out.print(items.get(x));
                    }
                    break;
                case 'n':
                    if (items.size() == 0) {
                        System.out.println("Thank you");
                    } else {
                        do {
                            System.out.println("Go to checkout? (Y/N):");
                            choice = scanner.nextLine().charAt(0);
                            switch (choice) {
                                case 'y':
                                    UserController reg = new UserController();
                                    int checkout;
                                    do {
                                        System.out.println("Sign in (enter 1) or sign up (Enter 2) to checkout: ");
                                        checkout = Integer.parseInt(scanner.nextLine());
                                        Users userSI;
                                        switch (checkout){
                                            case 1:
                                                do {
                                                    userSI = reg.signIn();
                                                }while (userSI==null);
                                                if(userSI.getRole() == 1)
                                                {
                                                    previewOrder(items, userSI);}
                                                else if(userSI.getRole() == 2){
                                                    System.out.println("You cannot buy by this account");
                                                    checkout = 3;
                                                }
                                                break;
                                            case 2:
                                                UserView ctr = new UserView();
                                                userSI = ctr.createAccount();
                                                previewOrder(items, userSI);
                                                break;
                                            default:
                                                System.out.println("Invalid choice");
                                        }
                                    }while(checkout != 1 && checkout != 2);
                                    break;
                                case 'n':
                                    System.out.println("Thanks!");
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                                    break;
                            }
                        } while (choice != 'n');
                    }
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 'n');
        return items;
    }

    public Checkout checkout(Users user) {
        Scanner scanner = new Scanner(System.in);
        CustomerController cusCtrl = new CustomerController();
        int check;
        String email;
        do {
            System.out.println("Enter your email:");
            email = scanner.nextLine();
            check = cusCtrl.checkEmail(user, email);
            if(check==1){
                System.out.println("Wrong email");
            }
            if(check==0){
                System.out.println("Correct email address");
            }
        } while (check != 0);

        System.out.println("Choose payment method:\n" +
                "1. COD\n" +
                "2. Visa Master/Visa Credit");
        int x = Integer.parseInt(scanner.nextLine());
        String payment = null;
        if(x == 1) {
            payment = "COD";
        } else if (x == 2) {
            payment = "Visa Master/Visa Credit";
        }
        System.out.println("Enter your address: ");
        String address = scanner.nextLine();
        Checkout checks = new Checkout(user, email, payment, address);
        return checks;
    }

    public void previewOrder(List<Cart> list, Users user) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            Checkout checks = checkout(user);
            System.out.println("Your information:");
            System.out.printf("%-30s%-30s%-30s%-30s%-30s\n", "ID","Username", "Email", "Payment", "Address");
            System.out.println(checks);
            System.out.println("============================================================================================");
            System.out.println("Your order:");
            System.out.printf("%-30s%-30s%-30s%-30s%-30s\n", "BookID", "Title", "Price", "Quantity", "Discount");
            for(int i =0;i<list.size();i++) {
                System.out.print(list.get(i));
            }
            char choice;
            do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Confirm your order:(Y/N)");
                choice = scanner.nextLine().charAt(0);
                switch (choice){
                    case 'y':
                        CartController ctr = new CartController();
                        boolean check = ctr.afterPurchase(list, user);
                        if (check) {

                            OrderController ord = new OrderController();
                            ord.displayOrder(user);
                            OrderDetailController ordd= new OrderDetailController();
                            ordd.displayOrderDetail(user);
                            System.out.println("Your order has been recorded.\n Thank you for shopping with us!");
                            BookShop bookShop = new BookShop();
                            bookShop.menuCustomer(user);
                        } else {
                            addToCart();
                        }
                        break;
                    case 'n':
                        System.out.println("We would like to serve you next time");
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            }while (choice !='n'&& choice !='y');
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public List<Cart> addToCart(Users user) {
        List<Cart> items = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        BookController bookCtr= new BookController();
        char choice = 0;
        int i=0;
        int id = 0;
        int qty;
        int sum =0;
        do {
            System.out.println("Proceed/Continue purchasing book? (Y/N)");
            choice = scanner.nextLine().charAt(0);
            switch (choice) {
                case 'y':
                    Book book = new Book();
                    do {
                        System.out.println("Enter book ID you want to buy: ");
                        id = Integer.parseInt(scanner.nextLine());
                        i = bookCtr.checkBookByID(id);
                        book.setBookID(id);
                    }while(i !=0);
                    do {

                        System.out.println("Enter amount you want to buy: ");
                        qty = Integer.parseInt(scanner.nextLine());
                        if(qty <0){
                            System.out.println("Enter another number");
                            i=1;
                        }
                        else{
                            sum += qty;
                            i = bookCtr.checkBookAmount(id,sum);
                            if(i!=0){
                                sum -= qty;
                            }
                            book.setQty(qty);}
                    }while(i !=0);
                    CartController obj = new CartController();
                    Cart item = obj.getItem(book);
                    items.add(item);
                    for(int x = 0;x<items.size();x++){
                        System.out.print(items.get(x));
                    }
                    break;
                case 'n':
                    if (items.size() == 0) {
                        System.out.println("Thank you");
                    } else {
                        do {
                            System.out.println("Go to checkout? (Y/N):");
                            choice = scanner.nextLine().charAt(0);
                            switch (choice) {
                                case 'y':
                                    UserController reg = new UserController();
                                    previewOrder(items, user);
                                    break;
                                case 'n':
                                    System.out.println("Thanks!");
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                                    break;
                            }
                        } while (choice != 'n');
                    }
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 'n');
        return items;
    }
}
