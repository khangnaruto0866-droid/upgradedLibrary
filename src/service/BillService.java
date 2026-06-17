package service;

import model.Bill;
import model.Book;
import model.Member;
import storage.BillStorage;
import util.ExistCheck;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillService {
    private BillStorage billStorage;
    private BookService bookService;
    private MemService memService;
    private List<Bill> bills;

    public BillService(BookService bookService, MemService memService) {
        billStorage = new BillStorage();
        this.bookService = bookService;
        this.memService = memService;
        this.bills = loadBill();
    }

    private List<Bill>loadBill(){
        List<Bill>temList=billStorage.load(bookService.displayBookList(),
                         memService.getVipService().displayVipList(),
                         memService.getRegService().displayRegularList());

        if(temList==null){
            temList = new ArrayList<>();
        }

        return temList;
    }

    public Bill findBillById(String billId){
        for(Bill bill:bills){
            if(bill.getBillId().equals(billId)){
                return bill;
            }
        }

        return null;
    }

    private String generateBillId(){
        if(bills.isEmpty()){
            return "T001";
        }

        int maxId=0;
        for(Bill bill:bills){
            int currentNum = Integer.parseInt(bill.getBillId().substring(1));
            if(currentNum > maxId){
                maxId = currentNum;
            }
        }

        return String.format("T%03d",maxId+1);
    }

    public void borrowBook(String bookId, String memId, LocalDate borrowDay){
        Book book = ExistCheck.noNull(bookService.findBookById(bookId));
        Member member = ExistCheck.noNull(memService.findMemById(memId));
        String billId = generateBillId();

        if(!book.isAvailable()){
            throw new IllegalArgumentException("❌ Error: Book is out of stock!");
        }

        if(member.getBookHold() >= member.getBorrowLimit()){
            throw new IllegalArgumentException("❌ Error: Member reached borrow limit!");
        }

        /*if (borrowDay.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("❌ Error: Borrow date cannot be in the future!");
        }*/

        for(Bill bill:bills){
            if(bill.getMember().getMemberId().equals(memId)&&
               bill.getBook().getBookId().equals(bookId)&&
               bill.getReturnDay()==null){
                throw new IllegalArgumentException("❌ Error: Cannot borrow the same book twice!");
            }
        }

        book.setBorrowCount(book.getBorrowCount()+1);
        member.setBookHold(member.getBookHold()+1);
        Bill bill = new Bill(billId,book,member,borrowDay);
        bills.add(bill);
        billStorage.saveOne(bill);
        bookService.updateBookData();
        memService.updateMemData();
    }

    public int returnBook(String billId, LocalDate returnDay){
        Bill bill = ExistCheck.noNull(findBillById(billId));

        if(bill.getReturnDay()!=null){
            throw new IllegalArgumentException("❌ Error: This bill has already been returned!");
        }

        if(returnDay.isBefore(bill.getBorrowDay())) {
            throw new IllegalArgumentException("❌ Error: Return date cannot be BEFORE borrow date!");
        }

        bill.setReturnDay(returnDay);
        bill.getBook().setBorrowCount(bill.getBook().getBorrowCount()-1);
        bill.getMember().setBookHold(bill.getMember().getBookHold()-1);
        billStorage.saveAll(bills);
        bookService.updateBookData();
        memService.updateMemData();

        int overdueDay = bill.calculateOverDay();
        if(overdueDay>0){
            return bill.getMember().getFineFee(overdueDay);
        }

        return 0;
    }

    public List<Bill>displayBillList(){
        return new ArrayList<>(bills);
    }

    public List<Bill>displayOutBill(){
        List<Bill>tempList = new ArrayList<>();
        for(Bill bill:bills){
            if(bill.getReturnDay()==null){
                tempList.add(bill);
            }
        }
        return tempList;
    }

    public List<Bill>memberHistory(String memId){
        ExistCheck.noNull(memService.findMemById(memId));
        List<Bill> tempList = new ArrayList<>();
        for(Bill bill:bills){
            if(bill.getMember().getMemberId().equals(memId)){
                tempList.add(bill);
            }
        }

        return tempList;
    }

    public void updateBillData(){
        billStorage.saveAll(bills);
    }


}
