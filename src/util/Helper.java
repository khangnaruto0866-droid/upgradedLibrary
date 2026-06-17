package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Helper {
    private Scanner sc;

    public Helper(Scanner sc) {
        this.sc = sc;
    }

    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String readPhone(String message){
        while(true){
            System.out.print(message);
            String phone=sc.nextLine().trim();
            try{
                return Validator.validPhone(phone);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public String readEmail(String message){
        while(true){
            System.out.print(message);
            String email=sc.nextLine().trim();
            try{
                return Validator.validMail(email);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public String readMemId(String message){
        while(true){
            System.out.print(message);
            String memId=sc.nextLine().trim();
            try{
                return Validator.validMemId(memId);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public String readVipId(String message){
        while(true){
            System.out.print(message);
            String vipId =sc.nextLine().trim();
            try{
                return Validator.validVip(vipId);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public String readRegId(String message){
        while(true){
            System.out.print(message);
            String regId =sc.nextLine().trim();
            try{
                return Validator.validReg(regId);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public String readBookId(String message){
        while(true){
            System.out.print(message);
            String bookId=sc.nextLine().trim();
            try{
                return Validator.validBookId(bookId);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public String readBillId(String message){
        while(true){
            System.out.print(message);
            String billId=sc.nextLine().trim();
            try{
                return Validator.validBillId(billId);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public String readName(String message){
       while(true){
           System.out.print(message);
           String name=sc.nextLine().trim();
           try{
               return Validator.validName(name);
           }catch (IllegalArgumentException e){
               System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
           }
       }
    }

    public int readNum(String message){
        while(true){
            System.out.print(message);
            String number=sc.nextLine().trim();
            try{
                return Validator.validNoNegative(Integer.parseInt(number));
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public int readYear(String message){
        while(true){
            int year=readNum(message);
            try{
                return Validator.validYear(year);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public LocalDate readDate(String message){
        while (true){
            System.out.print(message);
            String date=sc.nextLine().trim();
            try{
                return Validator.validDate(date);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public String readTitle(String message){
        while(true){
            System.out.print(message);
            String title=sc.nextLine().trim();
            try{
                return Validator.validTitle(title);
            }catch(IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

    public boolean readYesNo(String message){
        while(true){
            System.out.print(message);
            String confirm=sc.nextLine().trim();
            try{
                return Validator.validYesNo(confirm);
            }catch (IllegalArgumentException e){
                System.out.println("❌ Error: " + e.getMessage() + "\n👉 Please try again!");
            }
        }
    }

}
