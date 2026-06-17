package util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Validator {
    private static final String PHONE_FORMAT = "^0[35789][0-9]{8}$";
    private static final String EMAIL_FORMAT = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String MEMBER_ID = "^(VIP|REG)\\d{3}$";
    private static final String REGULAR_MEM_ID_FORMAT = "^REG\\d{3}$";
    private static final String VIP_MEM_ID_FORMAT = "^VIP\\d{3}$";
    private static final String BOOK_ID_FORMAT = "^BK\\d{3}$";
    private static final String BILL_ID_FORMAT = "^T\\d{3}$";
    private static final String NAME_FORMAT = "^[\\p{L}\\s]+$";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter FORMATTER=DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static String validString(String string){
        if(string==null||string.trim().isEmpty()){
            throw new IllegalArgumentException("❌ Input string cannot be null or empty");
        }

        return string.trim();
    }

    public static int validNoNegative(int num){
           if(num<0){
               throw new IllegalArgumentException("Value cannot be negative.");
           }
           return num;
    }

    public static String validPhone(String phone){
        phone=validString(phone);
        if(!phone.matches(PHONE_FORMAT)){
            throw new IllegalArgumentException("Phone number must be exactly 10 digits and start with '0', the second must be '3, 5, 7, 8, 9'.");
        }
        return phone;
    }

    public static String validMail(String email){
        email=validString(email);
        if(!email.matches(EMAIL_FORMAT)){
            throw new IllegalArgumentException("Invalid email format (e.g., user@gmail.com).");
        }
        return email;
    }

    public static String validMemId(String memId){
        memId=validString(memId);
        if(!memId.matches(MEMBER_ID)){
            throw new IllegalArgumentException("Member ID must start with 'VIP' or 'REG' followed by 3 digits (e.g., VIP001, REG001).");
        }
        return memId;
    }

    public static String validVip(String vip){
        vip=validString(vip);
        if(!vip.matches(VIP_MEM_ID_FORMAT)){
            throw new IllegalArgumentException("VIP ID must start with 'VIP' followed by 3 digits (e.g., VIP001).");
        }
        return vip;
    }

    public static String validReg(String reg){
        reg=validString(reg);
        if(!reg.matches(REGULAR_MEM_ID_FORMAT)){
            throw new IllegalArgumentException("Regular ID must start with 'REG' followed by 3 digits (e.g., REG001).");
        }
        return reg;
    }

    public static String validBookId(String bookId){
        bookId=validString(bookId);
        if(!bookId.matches(BOOK_ID_FORMAT)){
            throw new IllegalArgumentException("Book ID must start with 'BK' followed by 3 digits (e.g., BK001).");
        }
        return bookId;
    }

    public static String validBillId(String billId){
        billId=validString(billId);
        if(!billId.matches(BILL_ID_FORMAT)){
            throw new IllegalArgumentException("Bill ID must start with 'T' followed by 3 digits (e.g., T001)");
        }
        return billId;
    }

    public static String validName(String name){
        name=validString(name);
        if(!name.matches(NAME_FORMAT)){
            throw new IllegalArgumentException("Name cannot be empty and must contain only letters and spaces.");
        }
        return name;
    }

    public static int validYear(int year){
        int current=LocalDate.now().getYear();
        if(year<1440||year>current){
            throw new IllegalArgumentException("❌ Error: Invalid publication year!");
        }
        return year;
    }

    public static LocalDate validDate(String date){
        date=validString(date);
        try{
            return LocalDate.parse(date.trim(),FORMATTER);
        }catch (DateTimeException e){
            throw new IllegalArgumentException("❌ Error: Invalid date format. Must be " +DATE_FORMAT);
        }
    }


    public static String formatDay(LocalDate date){
        if(date==null){
            return "PENDING";
        }
        return date.format(FORMATTER);
    }

    public static String validTitle(String title){
        title=validString(title);
        for (int i = 0; i < title.length(); i++) {
            char t = title.charAt(i);
            boolean isValid = Character.isLetter(t) || Character.isDigit(t) || Character.isWhitespace(t) ||
                    t == '.' || t == ',' || t == '_' ||
                    t == '-' || t == ':' || t == '(' ||
                    t == ')' || t == '?' || t == '!';

            if (!isValid) {
                throw new IllegalArgumentException("❌ Error: Invalid title format, cannot contain special character in title");
            }
        }
        return title;
    }

    public static boolean validYesNo(String answer){
        validString(answer);
        String val = answer.trim().toUpperCase();
        if(val.equals("Y")||val.equals("YES")){
            return true;
        }
        if(val.equals("N")||val.equals("NO")){
            return false;
        }
        throw new IllegalArgumentException("Invalid choice! Please type 'Y' for Yes, or 'N' for No.");
    }
}
