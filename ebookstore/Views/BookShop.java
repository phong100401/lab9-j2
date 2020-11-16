package ebookstore.Views;

import ebookstore.Controller.BookController;
import ebookstore.Controller.UserController;
import ebookstore.Model.Users;

import java.sql.ResultSet;
import java.util.Scanner;

public class BookShop {
    public static String slt;
    public static ResultSet rst;
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        BookShop bookShop1 = new BookShop();
        System.out.println("Welcome to E-bookstore!!!");
        System.out.println("============================================================");
        bookShop1.menuVisit();
    }

    //menu cho moi role
    public void menuUser() {
        BookView bookView = new BookView();
        CartView cartView = new CartView();
        CustomerView customerView = new CustomerView();
        UserView userView = new UserView();
        BookShop bookShop = new BookShop();

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Sign In\n" +
                    "2. Sign Up\n" +
                    "3. Visit mode\n" +
                    "4. Exit");
            System.out.println("Your choice:");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    UserController reg = new UserController();
                    Users user = reg.signIn();
                    userView.checkUser(user);
                    break;
                case 2:
                    userView.createAccount();
                    bookShop.menuUser();
                    break;
                case 3:
                    bookShop.menuVisit();
                    break;
                case 4:
                    System.out.println("Goodbye! See you later");
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);
    }

    public void menuCustomer(Users user) {
        BookView bookView = new BookView();
        CartView cartView = new CartView();
        CustomerView customerView = new CustomerView();
        UserView userView = new UserView();
        BookShop bookShop = new BookShop();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Display your information\n" +
                    "2. Display your order\n" +
                    "3. Display your order detail\n" +
                    "4. Display books in store\n" +
                    "5. Find book by category\n" +
                    "6. Find book by Author\n" +
                    "7. Find book by Category\n" +
                    "8. Back");
            System.out.println("Your choice:");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    customerView.cusInfor(user);
                    break;
                case 2:
                    customerView.cusOrder(user);
                    break;
                case 3:
                    customerView.cusOrderDetail(user);
                    break;
                case 4:
                    BookController bookctrl = new BookController();
                    bookctrl.displayBook();
                    cartView.addToCart();
                    break;
                case 5:
                    bookView.findBookByCategory();
                    break;
                case 6:
                    bookView.findBookByAuthor();
                    break;
                case 7:
                    bookView.findBookByID();
                    break;
                case 8:
                    bookShop.menuUser();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 8);
    }

    public void menuAdmin(Users user) {
        BookView bookView = new BookView();
        CartView cartView = new CartView();
        CustomerView customerView = new CustomerView();
        UserView userView = new UserView();

        Scanner scanner = new Scanner(System.in);
        BookShop bookShop = new BookShop();
        int choice;
        do {
            System.out.println("1. Book management\n" +
                    "2. Customer management\n" +
                    "3. Order management\n" +
                    "4. Exit");
            System.out.println("Your choice:");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    bookView.bookManagement(user);
                    break;
                case 2:
                    customerView.customerManagement(user);
                    break;
                case 3:
                    bookView.orderDetail();
                    break;
                case 4:
                    bookShop.menuUser();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);
    }

    private void menuVisit() {
        CartView cartView = new CartView();
        BookShop bookShop = new BookShop();
        BookView bookView = new BookView();
        int choice;
        do {
            System.out.println(
                    "1. Display books in store\n" +
                            "2. Find book by category\n" +
                            "3. Find book by Author\n" +
                            "4. Find book by Category\n" +
                            "5. Top 10 newest books\n" +
                            "6. Top seller\n" +
                            "7. Sign in or Sign Up\n" +
                            "8. Exit");
            System.out.println("Your choice:");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    BookController bookctrl = new BookController();
                    bookctrl.displayBook();
                    cartView.addToCart();
                    break;
                case 2:
                    bookView.findBookByCategory();
                    break;
                case 3:
                    bookView.findBookByAuthor();
                    break;
                case 4:
                    bookView.findBookByID();
                    break;
                case 5:
                    bookView.top10newest();
                    break;
                case 6:
                    bookView.topSeller();
                    break;
                case 7:
                    bookShop.menuUser();
                    break;
                case 8:
                    System.out.println("Goodbye. See you later!!");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 8);

    }
}
