package model;

import util.ExistCheck;
import util.Validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Bill {
    private final String billId;
    private Book book;
    private Member member;
    private LocalDate borrowDay;
    private LocalDate returnDay;

    public Bill(String billId, Book book, Member member, LocalDate borrowDay) {
        this.billId = Validator.validBillId(billId);
        setBook(book);
        setMember(member);
        setBorrowDay(borrowDay);
        this.returnDay=null;
    }

    public String getBillId() {
        return billId;
    }

    public Book getBook() {
        return book;
    }

    public final void setBook(Book book) {
        this.book = ExistCheck.noNull(book);
    }

    public Member getMember() {
        return member;
    }

    public final void setMember(Member member) {
        this.member = ExistCheck.noNull(member);
    }

    public LocalDate getBorrowDay() {
        return borrowDay;
    }

    public final void setBorrowDay(LocalDate borrowDay) {
        this.borrowDay = ExistCheck.noNull(borrowDay);
    }

    public LocalDate getReturnDay() {
        return returnDay;
    }

    public final void setReturnDay(LocalDate returnDay) {
        this.returnDay = returnDay;
    }

    public void markReturnedDay(LocalDate returnDay){
        ExistCheck.noNull(returnDay);

        if(returnDay.isBefore(borrowDay)){
            throw new IllegalArgumentException("return day must be after borrow day");
        }

        this.returnDay = returnDay;
    }

    public int calculateOverDay(){
        LocalDate dueDay = borrowDay.plusDays(member.getDueLimit());
        if(returnDay==null||returnDay.isBefore(dueDay)){
            return 0;
        }

        long dayBetween = ChronoUnit.DAYS.between(dueDay,returnDay);
        return (int) dayBetween;
    }

    @Override
    public String toString() {
        return String.format("🧾 Bill: %s | Book: %s | Member: %s | Borrow: %s | Return: %s | Overdue: %d days",
                billId, book.getBookId(), member.getMemberId(),
                Validator.formatDay(borrowDay), Validator.formatDay(returnDay), calculateOverDay());
    }
}
