package model;

import util.Validator;

public class Regular extends Member {
    public Regular(String memberId, String name, String phone, String email) {
        super(Validator.validReg(memberId), name, phone, email);
    }

    @Override
    public final int getBorrowLimit(){
        return 3;
    }

    @Override
    public final int getDueLimit(){
        return 7;
    }

    @Override
    public final int getFineFee(int overDueDay){
        return overDueDay*5;
    }
}
