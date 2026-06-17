package ui;

import service.BillService;
import service.BookService;
import service.MemService;
import util.ConsoleHelper;

import java.util.Scanner;

public class MainUi {
    private Scanner sc;
    private BookUi bookUi;
    private MemUi memUi;
    private BillUi billUi;
    private ReportUi reportUi;
    private BookService bookService;
    private MemService memService;
    private BillService billService;
    private ConsoleHelper console;

    public MainUi(Scanner sc, BookUi bookUi, MemUi memUi, BillUi billUi, ReportUi reportUi,
                  BookService bookService, MemService memService, BillService billService) {
        this.sc = sc;
        this.bookUi = bookUi;
        this.memUi = memUi;
        this.billUi = billUi;
        this.reportUi = reportUi;
        this.bookService = bookService;
        this.memService = memService;
        this.billService = billService;
        this.console = new ConsoleHelper(sc);
    }

    public void start(){
        boolean isRunning = true;
        while (isRunning) {
            console.clearScreen();
            System.out.println("\n██████████████████████████████████████████████████");
            System.out.println("█                                                █");
            System.out.println("█       📚 KANNY LIBRARY MANAGEMENT SYSTEM 📚    █");
            System.out.println("█                                                █");
            System.out.println("██████████████████████████████████████████████████");
            System.out.println("                  [1] 📖 BOOK MANAGEMENT          ");
            System.out.println("                  [2] 👤 MEMBER MANAGEMENT        ");
            System.out.println("                  [3] 📜 BILL / TRANSACTIONS      ");
            System.out.println("                  [4] 📊 REPORTS & STATISTICS     ");
            System.out.println("                  [0] 🚪 EXIT SYSTEM              ");
            System.out.println("==================================================");
            System.out.print(" 🎯 Select a module to enter: ");

            try{
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch(choice){
                    case 1:{
                        bookUi.start();
                        break;
                    }

                    case 2:{
                        memUi.start();
                        break;
                    }


                    case 3:{
                        billUi.start();
                        break;
                    }


                    case 4:{
                        reportUi.start();
                        break;
                    }

                    case 0:{
                        bookService.updateBookData();
                        memService.updateMemData();
                        billService.updateBillData();
                        isRunning = false;
                        System.out.println("\n 💾 Saving all data and shutting down...");
                        System.out.println(" 👋 Thank you for using Kanny Library System. Goodbye!");
                        break;
                    }

                    default:
                        System.out.println(" ❌ Invalid choice! Please select from 0 to 4.");
                        console.pause();
                }

            }catch (NumberFormatException e) {
                System.out.println(" ❌ Invalid choice! Please select from 0 to 4.");
                console.pause();

            }
        }
    }


}
