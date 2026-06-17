package service;

import model.Bill;
import model.Book;
import model.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private BillService billService;

    public ReportService(BillService billService) {
        this.billService = billService;
    }

    public List<Bill>beingBorrowBook(){
        return billService.displayOutBill();
    }

    public List<Bill>overDueBill(LocalDate today){
        List<Bill>tempList = new ArrayList<>();
        for(Bill bill:billService.displayOutBill()){
            LocalDate dueDay = bill.getBorrowDay().plusDays(bill.getMember().getDueLimit());
            if(dueDay.isBefore(today)){
                tempList.add(bill);
            }
        }

        return tempList;
    }

    public List<String>mostPopularBook(){
        Map<Book,Integer>countMap = new HashMap<>();
        for(Bill bill:billService.displayBillList()){
            Book book = bill.getBook();
            countMap.put(book,countMap.getOrDefault(book,0)+1);
        }

        List<Map.Entry<Book,Integer>> sortList=new ArrayList<>(countMap.entrySet());
        sortList.sort((e1,e2)-> e2.getValue()- e1.getValue());

        List<String>report = new ArrayList<>();
        for(Map.Entry<Book,Integer> entry:sortList){
            Book book = entry.getKey();
            int count = entry.getValue();
            report.add(String.format("%-10s | %-30s | %-15d", book.getBookId(), book.getTitle(), count));
        }

        return report;
    }

    public List<String>mostBorrowMem(){
        Map<Member,Integer>countMap = new HashMap<>();
        for(Bill bill:billService.displayBillList()){
            Member member = bill.getMember();
            countMap.put(member,countMap.getOrDefault(member,0)+1);
        }

        List<Map.Entry<Member,Integer>>sortList = new ArrayList<>(countMap.entrySet());
        sortList.sort((e1,e2)->e2.getValue()- e1.getValue());

        List<String>report = new ArrayList<>();
        for(Map.Entry<Member,Integer> entry:sortList){
            Member member = entry.getKey();
            int count = entry.getValue();
            report.add(String.format("%-10s | %-30s | %-15d", member.getMemberId(), member.getName(), count));
        }

        return report;
    }
}
