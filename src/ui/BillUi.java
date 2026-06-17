package ui;

import model.Bill;
import model.Book;
import model.Member;
import service.BillService;
import service.BookService;
import service.MemService;
import util.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class BillUi {
    private Scanner sc;
    private BookService bookService;
    private MemService memService;
    private BillService billService;
    private Helper helper;
    private ConsoleHelper console;

    public BillUi(Scanner sc, BookService bookService, MemService memService, BillService billService) {
        this.sc = sc;
        this.bookService = bookService;
        this.memService = memService;
        this.billService = billService;
        this.helper = new Helper(sc);
        this.console = new ConsoleHelper(sc);
    }

    public void start(){
        boolean isRunning = true;
        while(isRunning){
            console.clearScreen();
            System.out.println("\n==================================================");
            System.out.println("   📜✨ LIBRARY SYSTEM : BILL MANAGEMENT ✨📜   ");
            System.out.println("==================================================");
            System.out.println("  [1] 📤 Borrow a Book");
            System.out.println("  [2] 📥 Return a Book");
            System.out.println("  [3] 📋 View All Bills");
            System.out.println("  [4] 👤 View Member's Borrow History");
            System.out.println("  [0] 🔙 Back to Main Menu");
            System.out.println("==================================================");
            System.out.print(" 🎯 Select an option: ");

            try{
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice){
                    case 1:{
                        handleBorrow();
                        break;
                    }

                    case 2:{
                        handleReturn();
                        break;
                    }

                    case 3:{
                        handleDisplayAllBill();
                        break;
                    }

                    case 4:{
                        handleMemHistory();
                        break;
                    }

                    case 0:{
                        isRunning = false;
                        System.out.println(" 🔙 Returning to Main Menu...");
                        break;
                    }

                    default:
                        System.out.println(" ❌ Invalid choice! Please select from 0 to 4.");
                        console.pause();
                }

            }catch (NumberFormatException e){
                System.out.println(" ❌ Invalid choice! Please select from 0 to 4.");
                console.pause();
            }
        }
    }

    private void handleBorrow(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("                 📤 BORROW A BOOK 📤              ");
        System.out.println("--------------------------------------------------");

        try{
            String memId = helper.readMemId(" 🏷️ Enter Member ID (VIP001 or REG001): ");
            Member member = ExistCheck.noNull(memService.findMemById(memId));
            System.out.println(" 👤 Member Name: " + member.getName() + " | Currently holding: " + member.getBookHold() + " book(s)");

            String bookId = helper.readBookId(" 🏷️ Enter Book ID (e.g., BK001): ");
            Book book = ExistCheck.noNull(bookService.findBookById(bookId));
            int available = book.getQuantity() - book.getBorrowCount();
            System.out.println(" 📖 Book Title: " + book.getTitle() + " | Stock available: " + available);

            LocalDate borrowDay = helper.readDate(" 📅 Enter Borrow Date (dd/MM/yyyy): ");

            System.out.println("\n ❓ Confirm borrowing this book?");
            boolean confirm = helper.readYesNo(" 👉 Enter [Y] to Confirm or [N] to Cancel: ");

            if(confirm){
                billService.borrowBook(bookId,memId,borrowDay);
                System.out.println("\n ✅ SUCCESS: Book borrowed successfully!");
            }

            else{
                System.out.println("\n 🚫 Action cancelled.");
            }

        }catch (IllegalArgumentException e) {
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }

    private void handleReturn(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("                 📥 RETURN A BOOK 📥              ");
        System.out.println("--------------------------------------------------");

        try{
            System.out.print(" 🏷️ Enter Bill ID to return: ");
            String billId = sc.nextLine().trim().toUpperCase();
            Bill bill = ExistCheck.noNull(billService.findBillById(billId));

            System.out.println("\n 📌 BILL FOUND:");
            System.out.println(" 👤 Member: " + bill.getMember().getName());
            System.out.println(" 📖 Book: " + bill.getBook().getTitle());
            System.out.println(" 📅 Borrowed on: " + bill.getBorrowDay().format(Helper.DTF));

            LocalDate returnDay = helper.readDate("\n 📅 Enter Return Date (dd/MM/yyyy): ");

            System.out.println("\n ❓ Confirm returning this book?");
            boolean confirm = helper.readYesNo(" 👉 Enter [Y] to Confirm or [N] to Cancel: ");

            if(confirm){
                int fine = billService.returnBook(billId,returnDay);
                System.out.println("\n ✅ SUCCESS: Book returned successfully!");

                if(fine > 0){
                    System.out.println(" ⚠️ OVERDUE WARNING! Member must pay a fine of: $" + fine + " 💸");
                }

                else {
                    System.out.println(" 🟢 Returned on time. No fine applied.");
                }
            }

            else{
                System.out.println("\n 🚫 Action cancelled.");
            }

        }catch (IllegalArgumentException e) {
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }

    private void handleDisplayAllBill(){
        console.clearScreen();
        System.out.println("\n------------------------------------------------------------------------------------------------------");
        System.out.println("                                       📋 ALL LIBRARY BILLS 📋                                          ");
        System.out.println("------------------------------------------------------------------------------------------------------");

        List<Bill>billList = billService.displayBillList();
        if(billList.isEmpty()){
            System.out.println(" ⚠️ No records found.");
            return;
        }

        printBillTable(billList);

        System.out.println("------------------------------------------------------------------------------------------------------");
        console.pause();
    }

    public void handleMemHistory(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("           👤 VIEW MEMBER'S BORROW HISTORY          ");
        System.out.println("--------------------------------------------------");

        try{
            String memId = helper.readMemId(" 🏷️ Enter Member ID: ");
            Member member = ExistCheck.noNull(memService.findMemById(memId));

            System.out.println("\n 📌 BORROW HISTORY FOR: " + member.getName() + " (" + memId + ")");
            List<Bill>history = billService.memberHistory(memId);

            if(history.isEmpty()){
                System.out.println(" ⚠️ No records found.");
                return;
            }

            printBillTable(history);

        }catch (IllegalArgumentException e){
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }

    private void printBillTable(List<Bill>billList){
        System.out.printf(" %-8s | %-20s | %-20s | %-12s | %-12s | %-10s%n",
                "Bill ID", "Member Name", "Book Title", "Borrow Date", "Return Date", "Status");

        for(Bill bill:billList){
            String returnDay = (bill.getReturnDay()==null) ? "Borrowing" : bill.getReturnDay().format(Helper.DTF);
            String status = (bill.getReturnDay() != null) ? "🟢 DONE" : "🔴 ACTIVE";

            System.out.printf(" %-8s | %-20s | %-20s | %-12s | %-12s | %-10s%n",
                    bill.getBillId(),
                    Beauty.formatMemNameLength(bill.getMember().getName(), 20),
                    Beauty.formatStringLength(bill.getBook().getTitle(), 20),
                    bill.getBorrowDay().format(Helper.DTF),
                    returnDay,
                    status);
        }
    }
}
