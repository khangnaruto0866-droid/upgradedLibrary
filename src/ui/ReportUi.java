package ui;

import model.Bill;
import service.ReportService;
import util.Beauty;
import util.ConsoleHelper;
import util.Helper;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReportUi {
    private Scanner sc;
    private ReportService reportService;
    private Helper helper;
    private ConsoleHelper console;

    public ReportUi(Scanner sc, ReportService reportService) {
        this.sc = sc;
        this.reportService = reportService;
        this.helper = new Helper(sc);
        this.console = new ConsoleHelper(sc);
    }

    public void start(){
        boolean isRunning = true;
        while(isRunning){
            console.clearScreen();
            System.out.println("\n==================================================");
            System.out.println("   📊✨ LIBRARY SYSTEM : REPORT & STATISTICS ✨📊   ");
            System.out.println("==================================================");
            System.out.println("  [1] 📤 View Currently Borrowed Books (Active)");
            System.out.println("  [2] ⏰ View Overdue Books");
            System.out.println("  [3] 🔥 View Most Popular Books (Ranking)");
            System.out.println("  [4] 🏆 View Top Borrowing Members (Ranking)");
            System.out.println("  [0] 🔙 Back to Main Menu");
            System.out.println("==================================================");
            System.out.print(" 🎯 Select an option: ");

            try{
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch(choice){
                    case 1:{
                        handleDisplayBeingBorrow();
                        break;
                    }

                    case 2:{
                        handleDisplayOverdue();
                        break;
                    }

                    case 3:{
                        handleMostPopBook();
                        break;
                    }

                    case 4:{
                        handleMostBorrowMem();
                        break;
                    }

                    case 0:{
                        isRunning = false;
                        System.out.println(" 🔙 Returning to Main Menu...");
                        break;
                    }

                    default:
                        System.out.println(" ❌ Invalid choice! Please select 1, 2, 3, 4 or 0.");
                        console.pause();
                }

            }catch (NumberFormatException e){
                System.out.println(" ❌ Invalid choice! Please select 1, 2, 3, 4 or 0.");
                console.pause();
            }
        }
    }

    private void handleDisplayBeingBorrow(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("         📤 CURRENTLY BORROWED BOOKS (OUT)        ");
        System.out.println("--------------------------------------------------");

        List<Bill>outList = reportService.beingBorrowBook();

        if(outList.isEmpty()){
            System.out.println(" 🟢 All books are currently in the library. No active borrowing.");
        }

        else{
            System.out.println(" 📊 Total books currently out: " + outList.size());
            printBillTable(outList);
        }
    }

    private void handleDisplayOverdue(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("               ⏰ OVERDUE BOOKS REPORT            ");
        System.out.println("--------------------------------------------------");

        try{
            LocalDate today = helper.readDate(" 📅 Enter Check Date (dd/MM/yyyy) ");
            List<Bill> overdue = reportService.overDueBill(today);

            if(overdue.isEmpty()){
                System.out.println("\n 🟢 Great! No overdue books as of " + today.format(Helper.DTF));
            }

            else{
                System.out.println("\n 🔴 WARNING: Found " + overdue.size() + " overdue record(s)!");
                printBillTable(overdue);
            }

        }catch (Exception e){
            System.out.println("\n ❌ ERROR: Invalid date input.");
        }

        console.pause();
    }

    private void handleMostPopBook(){
        console.clearScreen();
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("              🔥 MOST POPULAR BOOKS RANKING 🔥             ");
        System.out.println("-------------------------------------------------------------");

        List<String>bookRanking = reportService.mostPopularBook();

        if(bookRanking.isEmpty()){
            System.out.println(" ⚠️ No borrowing records found.");
        }

        else{
            System.out.printf(" %-10s | %-30s | %-15s%n", "Book ID", "Title", "Times Borrowed");
            for(String line:bookRanking){
                System.out.println(" " + line);
            }
        }

        System.out.println("-------------------------------------------------------------");
        console.pause();
    }

    private void handleMostBorrowMem(){
            console.clearScreen();
            System.out.println("\n-------------------------------------------------------------");
            System.out.println("              🏆 TOP BORROWING MEMBERS RANKING 🏆          ");
            System.out.println("-------------------------------------------------------------");

            List<String>memRanking = reportService.mostBorrowMem();

            if(memRanking.isEmpty()){
                System.out.println(" ⚠️ No borrowing records found.");
            }

            else{
                System.out.printf(" %-10s | %-30s | %-15s%n", "Member ID", "Name", "Times Borrowed");
                for(String line:memRanking){
                    System.out.println(" " + line);
                }
            }

        System.out.println("-------------------------------------------------------------");
        console.pause();
    }

    private void printBillTable(List<Bill> bills) {
        System.out.println();
        System.out.printf(" %-8s | %-20s | %-20s | %-12s | %-12s%n",
                "Bill ID", "Member Name", "Book Title", "Borrow Date", "Due Date");

        for (Bill bill : bills) {
            LocalDate dueDate = bill.getBorrowDay().plusDays(bill.getMember().getDueLimit());

            System.out.printf(" %-8s | %-20s | %-20s | %-12s | %-12s%n",
                    bill.getBillId(),
                    Beauty.formatMemNameLength(bill.getMember().getName(), 20),
                    Beauty.formatStringLength(bill.getBook().getTitle(), 20),
                    bill.getBorrowDay().format(Helper.DTF),
                    dueDate.format(Helper.DTF));
        }
        System.out.println("-----------------------------------------------------------------------------------");
    }
}
