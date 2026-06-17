package storage;

import model.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookStorage {
    private static final String FOLDER = "data";
    private static final String FILE_PATH = "data/book.txt";

    private String bookToLine(Book book){
        return book.getBookId() + '|'
                + book.getTitle() + '|'
                + book.getAuthor() + '|'
                + book.getGenre() + '|'
                + book.getPubYear() + '|'
                + book.getQuantity() + '|'
                + book.getBorrowCount();
    }

    public void saveOne(Book book){
        File folder = new File(FOLDER);
        if(!folder.exists()){
            folder.mkdir();
        }

        try(FileWriter fileWriter = new FileWriter(FILE_PATH,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

                String line=bookToLine(book);
                bufferedWriter.write(line);
                bufferedWriter.newLine();

        }catch (IOException e){
            System.out.println("❌ Error: Failed to save the new book to file!");
        }
    }

    public void saveAll(List<Book>bookList){
        File folder = new File(FOLDER);
        if(!folder.exists()){
            folder.mkdir();
        }

        try(FileWriter fileWriter = new FileWriter(FILE_PATH,false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

            for(Book book:bookList){
                String line = bookToLine(book);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

        }catch (IOException e){
            System.out.println("❌ Error: Failed to save all book to file!");
        }
    }

    private Book lineToBook(String line){
       try{
           String[] part = line.split("\\|");
           String bookId = part[0].trim();
           String title = part[1].trim();
           String author = part[2].trim();
           String genre = part[3].trim();
           int pubYear = Integer.parseInt(part[4]);
           int quantity = Integer.parseInt(part[5]);
           int borrowCount = Integer.parseInt(part[6]);

           Book book = new Book(bookId,title,author,genre,pubYear,quantity);
           book.setBorrowCount(borrowCount);
           return book;
       }catch (Exception e){
           System.out.println("❌ Error: Corrupted book data line -> " + line);
           return null;
       }
    }

    public List<Book> load(){
        List<Book>bookList=new ArrayList<>();
        File file = new File(FILE_PATH);

        if(!file.exists()){
            return bookList;
        }

        try(FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader reader = new BufferedReader(fileReader)){

            String data;
            while((data=reader.readLine())!=null){
                if(data.trim().isEmpty()){
                    continue;
                }

                Book book = lineToBook(data);
                if(book!=null){
                    bookList.add(book);
                }
            }

        }catch (IOException e){
            System.out.println("⚠️ Warning: Failed to load Books data!");
        }

        return bookList;
    }
}
