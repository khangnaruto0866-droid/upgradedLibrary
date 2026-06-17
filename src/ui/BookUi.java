package ui;

import model.Book;
import service.BookService;
import util.*;

import java.util.List;
import java.util.Scanner;

public class BookUi {
    private Scanner sc;
    private Helper helper;
    private BookService bookService;
    private ConsoleHelper console;

    public BookUi(Scanner sc, BookService bookService) {
        this.sc = sc;
        this.bookService = bookService;
        this.helper = new Helper(sc);
        this.console = new ConsoleHelper(sc);
    }

    public void start(){
        boolean isRunning = true;
        while(isRunning){
            console.clearScreen();
            System.out.println("\n==================================================");
            System.out.println("    📚✨ LIBRARY SYSTEM : BOOK MANAGEMENT ✨📚    ");
            System.out.println("==================================================");
            System.out.println("  [1] ➕ Add New Book");
            System.out.println("  [2] ✏️  Update Book Information");
            System.out.println("  [3] 📋 View All Books");
            System.out.println("  [4] 🗑️  Remove a Book");
            System.out.println("  [5] 🔍 Search Books");
            System.out.println("  [0] 🔙 Back to Main Menu");
            System.out.println("==================================================");
            System.out.print(" 🎯 Select an option: ");

            try{
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice){
                    case 1:{
                        handleAddBook();
                        break;
                    }

                    case 2:{
                        handleUpdateBookInf();
                        break;
                    }

                    case 3:{
                        handleDisplayBook();
                        break;
                    }

                    case 4:{
                        handleRemoveBook();
                        break;
                    }

                    case 5:{
                        handleFindBook();
                        break;
                    }

                    case 0:{
                        isRunning=false;
                        System.out.println(" 🔙 Returning to Main Menu...");
                        break;
                    }

                    default:
                        System.out.println(" ❌ Invalid choice! Please select from 0 to 5.");
                        console.pause();
                }

            }catch (NumberFormatException e){
                System.out.println(" ❌ Invalid choice! Please select from 0 to 5.");
                console.pause();
            }
        }
    }

    private void handleAddBook(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("               ➕ ADD A NEW BOOK ➕               ");
        System.out.println("--------------------------------------------------");

        try{
            String bookId = helper.readBookId(" 🏷️ Enter Book ID (e.g., BK001): ");
            String title = Beauty.beauty(helper.readTitle(" 📖 Enter Book Title: "));
            String author = Beauty.beauty(helper.readName(" ✍️  Enter Author Name: "));
            String genre = Beauty.beauty(helper.readTitle(" 🎭 Enter Genre: "));
            int pubYear = helper.readYear(" 📅 Enter Publication Year: ");
            int quantity = helper.readNum(" 📦 Enter Total Quantity: ");

            System.out.println("\n ❓ Do you want to save this book?");
            boolean confirm = helper.readYesNo(" 👉 Enter [Y] to Save or [N] to Cancel: ");

            if(confirm){
                bookService.addBook(new Book(bookId,title,author,genre,pubYear,quantity));
                System.out.println("\n ✅ SUCCESS: Book '" + title + "' has been added to the library!");
            }

            else{
                System.out.println("\n 🚫 Action cancelled.");
            }

        }catch (IllegalArgumentException e){
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }

    private void handleUpdateBookInf(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("             ✏️  UPDATE BOOK INFO ✏️             ");
        System.out.println("--------------------------------------------------");

        try{
            String bookId = helper.readBookId(" 🏷️ Enter Book ID to update: ");
            Book book = ExistCheck.noNull(bookService.findBookById(bookId));

            System.out.println("\n 📌 CURRENT INFORMATION:");
            System.out.println(book.toString());
            System.out.println("\n 💡 (Press ENTER to keep the current value and skip to the next)");

            System.out.print(" 📖 Enter new Title [" + book.getTitle() + "]: ");
            String title = sc.nextLine();
            String finalTitle = title.trim().isEmpty() ? book.getTitle() : Beauty.beauty(title);

            System.out.print(" ✍️  Enter new Author [" + book.getAuthor() + "]: ");
            String author = sc.nextLine();
            String finalAuthor = author.trim().isEmpty() ? book.getAuthor() : Beauty.beauty(author);

            System.out.print(" 🎭 Enter new Genre [" + book.getGenre() + "]: ");
            String genre = sc.nextLine();
            String finalGenre = genre.trim().isEmpty() ? book.getGenre() : Beauty.beauty(genre);

            System.out.print(" 📅 Enter new Publication Year [" + book.getPubYear() + "]: ");
            String pubYear = sc.nextLine();
            int finalPubYear = pubYear.trim().isEmpty() ? book.getPubYear() : Integer.parseInt(pubYear.trim());

            System.out.print(" 📦 Enter new Total Quantity [" + book.getQuantity() + "]: ");
            String quantity = sc.nextLine();
            int finalQuantity = quantity.trim().isEmpty() ? book.getQuantity() : Integer.parseInt(quantity.trim());

            System.out.println("\n ❓ Save changes for this book?");
            boolean confirm = helper.readYesNo(" 👉 Enter [Y] to Update or [N] to Cancel: ");

            if(confirm){
                bookService.updateBook(bookId,finalTitle,finalAuthor,finalGenre,finalPubYear,finalQuantity);
                System.out.println("\n ✅ SUCCESS: Book information updated successfully!");
            }

            else{
                System.out.println("\n 🚫 Action cancelled.");
            }

        }catch (NumberFormatException e) {
            System.out.println("\n ❌ ERROR: Year and Quantity must be valid numbers!");
        } catch (IllegalArgumentException e) {
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }

    private void handleDisplayBook(){
        console.clearScreen();
        System.out.println("\n------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                          📋 ALL BOOKS IN LIBRARY 📋                                              ");
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        List<Book> bookList = bookService.displayBookList();
        if(bookList.isEmpty()){
            System.out.println(" ⚠️ No books found in the library. Please add some first!");
        }

        else{
            System.out.printf("%-10s | %-20s | %-20s | %-15s | %-6s | %-5s%n", "ID", "Title", "Author", "Genre", "Year", "Stock");
            System.out.println("-----------------------------------------------------------------------------------------");
            for(Book book : bookList){
                int available=book.getQuantity()-book.getBorrowCount();
                System.out.printf("%-10s | %-20s | %-20s | %-15s | %-6d | %-5d%n",
                        book.getBookId(),
                        Beauty.formatStringLength(book.getTitle(), 20),
                        Beauty.formatStringLength(book.getAuthor(), 20),
                        Beauty.formatStringLength(book.getGenre(), 15),
                        book.getPubYear(), available);
            }
        }

        console.pause();
    }

    private void handleRemoveBook(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("               🗑️  REMOVE A BOOK 🗑️               ");
        System.out.println("--------------------------------------------------");

        try{
            String bookId = helper.readBookId(" 🏷️ Enter Book ID to remove: ");
            Book book = ExistCheck.noNull(bookService.findBookById(bookId));

            System.out.println("\n 📌 BOOK FOUND:");
            System.out.println(" 📖 Title: " + book.getTitle());
            System.out.println(" ✍️  Author: " + book.getAuthor());

            System.out.println("\n ⚠️ WARNING: Are you sure you want to completely delete this book?");
            boolean confirm = helper.readYesNo(" 👉 Enter [Y] to Confirm or [N] to Cancel: ");

            if(confirm){
                bookService.removeBook(bookId);
                System.out.println("\n ✅ SUCCESS: Book removed successfully!");
            }

            else{
                System.out.println("\n 🚫 Action cancelled.");
            }

        }catch (IllegalArgumentException e){
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }

    private void handleFindBook(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("               🔍 SEARCH FOR BOOKS 🔍             ");
        System.out.println("--------------------------------------------------");

        try {
            System.out.print(" 🔑 Enter keyword (Title, Author, Genre, or ID): ");
            String keyword = Validator.validString(sc.nextLine());
            List<Book>result = bookService.searchBookByKey(keyword);

            if (result.isEmpty()) {
                System.out.println("\n ⚠️ No matching books found for keyword: '" + keyword + "'");
            }

            else{
                System.out.println("\n 🎯 SEARCH RESULTS (" + result.size() + " found):");
                for(Book book:result){
                    System.out.println(" "+book.toString());
                }
            }

        }catch (IllegalArgumentException e){
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }



}
