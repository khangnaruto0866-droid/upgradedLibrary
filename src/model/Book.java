package model;

import util.Validator;

public class Book {
    private final String bookId;
    private String title,author,genre;
    private int pubYear,quantity,borrowCount;

    public Book(String bookId, String title, String author, String genre, int pubYear, int quantity) {
        this.bookId = Validator.validBookId(bookId);
        setTitle(title);
        setAuthor(author);
        setGenre(genre);
        setPubYear(pubYear);
        setQuantity(quantity);
        this.borrowCount=0;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = Validator.validTitle(title);
    }

    public String getAuthor() {
        return author;
    }

    public final void setAuthor(String author) {
        this.author = Validator.validName(author);
    }

    public String getGenre() {
        return genre;
    }

    public final void setGenre(String genre) {
        this.genre = Validator.validTitle(genre);
    }

    public int getPubYear() {
        return pubYear;
    }

    public final void setPubYear(int pubYear) {
        this.pubYear = Validator.validYear(pubYear);
    }

    public int getQuantity() {
        return quantity;
    }

    public final void setQuantity(int quantity) {
        this.quantity = Validator.validNoNegative(quantity);
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public final void setBorrowCount(int borrowCount) {
        this.borrowCount = Validator.validNoNegative(borrowCount);
    }

    public boolean isAvailable(){
        return(this.quantity-this.borrowCount)>0;
    }



    public void borrowOneBook(){
        if(!isAvailable()){
            throw new IllegalArgumentException("❌ Error: There's no book left");
        }
        this.borrowCount++;
    }

    public void returnOneBook(){
       if(borrowCount<=0){
           throw new IllegalArgumentException("❌ Error: Cannot return when not borrow");
       }

       borrowCount--;
    }

    public String getStatus(){
        return isAvailable() ? "AVAILABLE" : "OUT OF STOCK";
    }

    @Override
    public String toString() {
        return String.format("📚 ID: %s | Title: %s | Author: %s | Genre: %s | Year: %d | Stock: %d/%d | Status: %s",
                bookId, title, author, genre, pubYear, (quantity - borrowCount), quantity, getStatus());
    }
}
