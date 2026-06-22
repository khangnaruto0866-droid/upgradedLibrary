package storage;

import model.*;
import util.ExistCheck;
import util.Validator;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillStorage {
    private static final String FOLDER = "data";
    private static final String FILE_PATH = "data/bill.txt";

    private String billToLine(Bill bill){
        String returnDay = Validator.formatDay(bill.getReturnDay());
        return bill.getBillId() + '|'
                + bill.getBook().getBookId() + '|'
                + bill.getMember().getMemberId() + '|'
                + Validator.formatDay(bill.getBorrowDay()) + '|'
                + returnDay;
    }

    public void saveOne(Bill bill){

        //write this in util, write one check all
        File folder = new File(FOLDER);
        ExistCheck.fileMkdir(folder);

        try(FileWriter fileWriter = new FileWriter(FILE_PATH,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

            String line = billToLine(bill);
            bufferedWriter.write(line);
            bufferedWriter.newLine();


        }catch (IOException e){
            System.out.println("❌ Error: Failed to save the new bill to file!");
        }
    }

    public void saveAll(List<Bill>billList){
        File folder = new File(FOLDER);
        ExistCheck.fileMkdir(folder);


        try(FileWriter fileWriter = new FileWriter(FILE_PATH,false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

            for(Bill bill:billList){
                String line = billToLine(bill);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }


        }catch (IOException e){
            System.out.println("❌ Error: Failed to save the new bill to file!");
        }
    }

    private Bill lineToBill(String line, List<Book>bookList, List<Vip>vipList, List<Regular>regList){
        try{
            String[] part = line.split("\\|");
            String billId = part[0].trim();
            String bookId = part[1].trim();
            String memberId = part[2].trim();
            String borrowStr = part[3].trim();
            String returnStr = part[4].trim();

            Book foundBook = null;
            for(Book book:bookList){
                if(book.getBookId().equals(bookId)){
                    foundBook=book;
                    break;
                }
            }

            Member foundMem = null;
            if(memberId.startsWith("VIP")){
                for(Vip vip:vipList){
                    if(vip.getMemberId().equals(memberId)){
                        foundMem = vip;
                        break;
                    }
                }
            }

            else if(memberId.startsWith("REG")){
                for(Regular reg:regList){
                    if(reg.getMemberId().equals(memberId)){
                        foundMem = reg;
                        break;
                    }
                }
            }

            if(foundMem == null || foundBook == null){
                return null;
            }

            LocalDate borrowDay = Validator.validDate(borrowStr);
            Bill bill = new Bill(billId,foundBook,foundMem,borrowDay);
            if(!returnStr.equals("PENDING")){
                bill.setReturnDay(Validator.validDate(returnStr));
            }

            return bill;

        }catch (Exception e){
            System.out.println("❌ Error: Corrupted bill data line -> " + line);
            return null;
        }
    }

    public List<Bill>load(List<Book>bookList, List<Vip>vipList, List<Regular>regList){
        List<Bill>billList=new ArrayList<>();
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return billList;
        }

        try(FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader reader = new BufferedReader(fileReader)){

            String data;
            while((data=reader.readLine())!=null){
                if(data.trim().isEmpty()){
                    continue;
                }

                Bill bill = lineToBill(data,bookList,vipList,regList);
                if(bill!=null){
                    billList.add(bill);
                }
            }

        }catch (IOException e){
            System.out.println("⚠️ Warning: Failed to load Bills data!");
        }

        return billList;
    }

}
