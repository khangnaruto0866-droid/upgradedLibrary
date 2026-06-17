package model;

import util.Validator;

public class Vip extends Member{
    public Vip(String memberId, String name, String phone, String email) {
        super(Validator.validVip(memberId), name, phone, email);
    }

    @Override
    public int getBorrowLimit(){
        return 5;
    }

    @Override
    public int getDueLimit(){
        return 14;
    }

    @Override
    public int getFineFee(int overDueDay){
        return overDueDay*3;
    }
}
