package app;

import model.Book;
import model.Regular;
import model.Vip;
import service.*;
import util.Validator;

import java.time.LocalDate;

public class ManualTest {
    private static int total=0;
    private static int passed=0;

    public static void main(String[] args) {


        testBorrowBookSuccessFully();
        testOutOfStockBorrowBook();
        testMemberBorrowLimit();
        testBorrowOneBookTwice();
        test31stFeb();
        testReturnBook();
        testRegFineFee();
        testVipFineFee();
        testReturnInPast();
        printSummary();

    }

    private static void check(String testName,boolean condition){
        total++;
        if(condition){
            passed++;
            System.out.println("passed "+ testName);
        }else{
            System.out.println("fail "+testName);
        }
    }

    private static void checkThrow(String testName,Runnable action){
        total++;
        try{
            action.run();
            System.out.println("fail "+testName+" did not throw exception");
        }catch (IllegalArgumentException e){
            passed++;
            System.out.println("passed "+testName+e.getMessage());
        }catch (Exception e){
            System.out.println("Wrong type of exception");
        }
    }


    private static void printSummary(){
        System.out.println();
        System.out.println("=====test sum=====");
        System.out.println("total test "+total);
        System.out.println("passed "+passed);
        System.out.println("fail "+(total-passed));
    }


/*
    public static void testDuplicateBook(){
        BookService bookService=new BookService();
        model.Book book1=new Book("BK000","testMulty","kanny","IT",2026,1);
        bookService.addBook(book1);
        checkThrow("test dup book",()->bookService.addBook(book1));
    }

    public static void testSameContactMember(){
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);

        Vip vip = new Vip("VIP000","kanziny","0907680362","khangnaruto0866@gmail.com");
        Vip vip1 = new Vip("VIP001","kanny","0907680362","kanny@gmail.com");
        Regular regular = new Regular("REG000","kani","0329334241","khangnaruto0866@gmail.com");

        memService.addVip(vip);

        checkThrow("similar phone test",()->memService.addVip(vip1));
        checkThrow("similar email test",()->memService.addReg(regular));
    }


 */
    //borrow book successfully->borrowCount + 1 and bookHold + 1
    //borrow out of stock book -> catch exception out of stock
    //member reach borrow limit -> catch exception limit
    //borrow duplicate book -> catch exception duplicate


    //return book successfully->borrowCount-1 and bookHold-1
    //return before borrow day->catch exception must be after
    //vip return late -> count fine fee 2 day = 6
    //regular return late -> count fine fee 2day = 10
    //31/2 cannot but 29/2 is valid

    public static void testBorrowBookSuccessFully(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK000","title","author","genre",1990,10);
        Vip vip = new Vip("VIP000","kanziny","0907680362","khangnaruto0866@gmail.com");

        int vipCurrentBookHold = vip.getBookHold();
        int bookCurrentBorrowedCount = book.getBorrowCount();

        bookService.addBook(book);
        memService.addVip(vip);
        billService.borrowBook(book.getBookId(), vip.getMemberId(), LocalDate.of(2019,1,2));

        check("vip borrow book",(vip.getBookHold()-vipCurrentBookHold==1) &&
                                                  (book.getBorrowCount()-bookCurrentBorrowedCount==1));
    }

    public static void testOutOfStockBorrowBook(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK001","title","author","genre",1990,10);
        Vip vip = new Vip("VIP001","kanziny","0907680363","naruto0866@gmail.com");

        book.setBorrowCount(10);
        bookService.addBook(book);
        memService.addVip(vip);

        checkThrow("out of stock test",()->billService.borrowBook(book.getBookId(),vip.getMemberId(), LocalDate.of(2020, 12, 12)));
    }

    public static void testMemberBorrowLimit(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK002","title1","author", "genre1",1990,20);
        Regular regular = new Regular("REG002","KhangDepTrai","0329334241","khangnaruto@gmail.com");

        regular.setBookHold(3);
        bookService.addBook(book);
        memService.addReg(regular);
        //error on name
        checkThrow("member get reach limit test",()->billService.borrowBook(book.getBookId(),regular.getMemberId(),LocalDate.of(2020,3,1)));
    }

    public static void testBorrowOneBookTwice(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK003","something","mrbeats","action",2000,3);
        Vip vip = new Vip("VIP003","kanziny","0907680364","khang0866@gmail.com");

        bookService.addBook(book);
        memService.addVip(vip);
        billService.borrowBook(book.getBookId(), vip.getMemberId(), LocalDate.of(2015,12,3));

        checkThrow("member borrow one book twice",()->billService.borrowBook(book.getBookId(),vip.getMemberId(),LocalDate.of(2020,3,1)));
    }

    public static void test31stFeb(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK004","what","mrbean","action",2001,5);
        Regular regular = new Regular("REG004","liz","0909099999","lizo@gmail.com");

        String day = "31/2/2026";
        bookService.addBook(book);
        memService.addReg(regular);

        checkThrow("31 February test",()->{
            LocalDate date = Validator.validDate(day);
            billService.borrowBook(book.getBookId(), regular.getMemberId(), date);
        });
    }


    public static void testReturnBook(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK005","what","mrbean","action",2001,5);
        Regular regular = new Regular("REG005","liz","0919191919","lizoklkl@gmail.com");

        LocalDate borrowDay = Validator.validDate("01/01/2026");

        bookService.addBook(book);
        memService.addReg(regular);
        billService.borrowBook(book.getBookId(), regular.getMemberId(), borrowDay);

        int regCurrentBookHold = regular.getBookHold();
        int bookCurrentBorrowedCount = book.getBorrowCount();
        LocalDate returnDay = Validator.validDate("07/01/2026");

        billService.returnBook("T003",returnDay);

        check("check return book successfully",(regular.getBookHold()-regCurrentBookHold == -1) &&
                (book.getBorrowCount()-bookCurrentBorrowedCount == -1));
    }

    public static void testRegFineFee(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK006","whatisthat","russian","horror",2024,5);
        Regular regular = new Regular("REG006","melon","0876012345","tnhn@gmail.com");

        LocalDate borrowDay = Validator.validDate("10/02/2026");

        bookService.addBook(book);
        memService.addReg(regular);
        billService.borrowBook(book.getBookId(), regular.getMemberId(), borrowDay);

        int regCurrentBookHold = regular.getBookHold();
        int bookCurrentBorrowedCount = book.getBorrowCount();
        LocalDate returnDay = Validator.validDate("19/02/2026");


        check("regular fine fee after 2 day late",billService.returnBook("T004",returnDay)==10);
    }

    public static void testVipFineFee(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK007","zenless","china","goon",2023,5);
        Vip vip = new Vip("VIP007","lemon","0376012345","dkldkl@gmail.com");

        LocalDate borrowDay = Validator.validDate("10/02/2026");

        bookService.addBook(book);
        memService.addVip(vip);
        billService.borrowBook(book.getBookId(), vip.getMemberId(), borrowDay);

        int vipCurrentBookHold = vip.getBookHold();
        int bookCurrentBorrowedCount = book.getBorrowCount();
        LocalDate returnDay = Validator.validDate("26/02/2026");


        check("VIP fine fee after 2 day late",billService.returnBook("T005",returnDay)==6);
    }

    public static void testReturnInPast(){
        BookService bookService = new BookService();
        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService,regService);
        BillService billService = new BillService(bookService,memService);

        Book book = new Book("BK008","zero","japan","action",2013,6);
        Vip vip = new Vip("VIP008","coco","0334559789","testthis@gmail.com");

        LocalDate borrowDay = Validator.validDate("10/02/2026");

        bookService.addBook(book);
        memService.addVip(vip);
        billService.borrowBook(book.getBookId(), vip.getMemberId(), borrowDay);

        int vipCurrentBookHold = vip.getBookHold();
        int bookCurrentBorrowedCount = book.getBorrowCount();
        LocalDate returnDay = Validator.validDate("26/02/2025");

        checkThrow("test return on past",()->billService.returnBook("T006",returnDay));
    }

}
