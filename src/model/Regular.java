package model;

import util.Validator;

public class Regular extends Member {
    public Regular(String memberId, String name, String phone, String email) {
        super(Validator.validReg(memberId), name, phone, email);
    }

    @Override
    public int getBorrowLimit(){
        return 3;
    }

    @Override
    public int getDueLimit(){
        return 7;
    }

    @Override
    public int getFineFee(int overDueDay){
        return overDueDay*5;
    }
}
