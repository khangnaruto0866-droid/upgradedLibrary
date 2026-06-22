package model;

import util.Validator;

public abstract class Member {
    private final String memberId;
    private String name, phone, email;
    private int bookHold;

    public Member(String memberId, String name, String phone, String email) {
        this.memberId = Validator.validMemId(memberId);
        setName(name);
        setPhone(phone);
        setEmail(email);
        this.bookHold=0;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = Validator.validName(name);
    }

    public String getPhone() {
        return phone;
    }

    public final void setPhone(String phone) {
        this.phone = Validator.validPhone(phone);
    }

    public String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        this.email = Validator.validMail(email);
    }

    public int getBookHold() {
        return bookHold;
    }

    public final void setBookHold(int bookHold) {
        this.bookHold = Validator.validNoNegative(bookHold);
    }

    public abstract int getBorrowLimit();
    public abstract int getDueLimit();
    public abstract int getFineFee(int overDueDay);

    public void BookHoldForBorrowOneBook(){
        if(bookHold>=getBorrowLimit()){
            throw new IllegalArgumentException("member has reached borrow limit");
        }

        bookHold++;
    }

    public void BookHoldForReturnOneBook(){
        if(bookHold<=0){
            throw new IllegalArgumentException("cannot return book when not borrow");
        }

        bookHold--;
    }

    @Override
    public String toString() {
        String prefix = (this instanceof Vip) ? "👑 [VIP]" : "🚶 [REGULAR]";
        return String.format("%s | 🪪 ID: %s | Name: %s | Phone: %s | Email: %s | Holding: %d/%d",
                prefix, memberId, name, phone, email, bookHold, getBorrowLimit());
    }
}
