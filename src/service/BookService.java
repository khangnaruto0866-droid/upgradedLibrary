package service;

import model.Book;
import storage.BookStorage;
import util.ExistCheck;
import util.Validator;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private List<Book> books;
    private BookStorage bookStorage;

    public BookService(){
        bookStorage = new BookStorage();
        books = validateBookList(bookStorage.load());
    }


    private List<Book>validateBookList(List<Book>bookList){
        if(bookList==null){
            bookList = new ArrayList<>();
        }

        return bookList;
    }

    public Book findBookById(String bookId){
        for(Book book:books){
            if(book.getBookId().equals(bookId)){
                return book;
            }
        }
        return null;
    }

    public void addBook(Book book){
        if(findBookById(book.getBookId())!=null){
            throw new IllegalArgumentException("❌ Error: Book ID already exists!");
        }

        books.add(book);
        bookStorage.saveOne(book);
    }

    public List<Book> displayBookList(){
        return new ArrayList<>(books);
    }

    public List<Book>searchBookByKey(String key){
        List<Book>tempList = new ArrayList<>();
        String keyword = Validator.validString(key.toLowerCase());

        for(Book book : books){
            if((book.getBookId().toLowerCase().contains(keyword))||
                book.getTitle().toLowerCase().contains(keyword)||
                book.getAuthor().toLowerCase().contains(keyword)||
                book.getGenre().toLowerCase().contains(keyword)){
                tempList.add(book);
            }
        }
        return tempList;
    }

    public void removeBook(String bookId){
        Book book = ExistCheck.noNull(findBookById(bookId));
        if(book.getBorrowCount()>0){
            throw new IllegalArgumentException("❌ Error: Cannot remove! Book is currently borrowed.");
        }

        books.remove(book);
        bookStorage.saveAll(books);
    }

    public void updateBook(String bookId, String title, String author, String genre, int pubYear, int quantity) {
        Book book = ExistCheck.noNull(findBookById(bookId));

        if (book.getBorrowCount() > 0) {
            throw new IllegalArgumentException("❌ Error: Cannot update! There are still " + book.getBorrowCount() + " copies currently borrowed.");
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPubYear(pubYear);
        book.setQuantity(quantity);
        updateBookData();
    }

    public void updateBookData(){
        bookStorage.saveAll(books);
    }
}
