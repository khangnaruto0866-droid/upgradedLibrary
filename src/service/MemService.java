package service;

import model.Member;
import model.Regular;
import model.Vip;
import util.ExistCheck;

import java.util.ArrayList;
import java.util.List;

public class MemService {
    private VipService vipService;
    private RegService regService;

    public MemService(VipService vipService, RegService regService) {
        this.vipService = vipService;
        this.regService = regService;
    }

    public VipService getVipService(){
        return vipService;
    }

    public RegService getRegService(){
        return regService;
    }

    public Member findMemById(String memId){
        if(memId.startsWith("VIP")){
            return vipService.findVipById(memId);
        }

        else if(memId.startsWith("REG")){
            return regService.findRegById(memId);
        }

        return null;
    }

    public List<Member> displayMemberList(){
        List<Member>memberList=new ArrayList<>();
        memberList.addAll(vipService.displayVipList());
        memberList.addAll(regService.displayRegularList());
        return memberList;
    }

    private void checkDupContact(String phone,String email,String excludeId){
        for(Member member:displayMemberList()){
            if(excludeId != null && member.getMemberId().equals(excludeId)){
                continue;
            }

            if (member.getPhone().equals(phone)){
                throw new IllegalArgumentException("❌ Error: Phone number already belongs to another member!");
            }

            if (member.getEmail().equals(email)){
                throw new IllegalArgumentException("❌ Error: Email already belongs to another member!");
            }
        }
    }

    public void addVip(Vip vip){
        checkDupContact(vip.getPhone(),vip.getEmail(),null);
        vipService.addVip(vip);
    }

    public void addReg(Regular regular){
        checkDupContact(regular.getPhone(),regular.getEmail(),null);
        regService.addReg(regular);
    }

    public void removeMem(String memId){
        if(memId.startsWith("VIP")){
            vipService.removeVip(memId);
        }

        else if(memId.startsWith("REG")){
            regService.removeReg(memId);
        }

        else{
            throw new IllegalArgumentException("❌ Error: Invalid Member ID format!");
        }
    }

    public void updateMem(String memId, String name, String phone, String email) {
        Member member = ExistCheck.noNull(findMemById(memId));

        checkDupContact(phone, email,memId);

        member.setName(name);
        member.setPhone(phone);
        member.setEmail(email);
        updateMemData();
    }

    public List<Member>searchMemByKey(String key){
        String keyword = key.toLowerCase();
        List<Member>tempList = new ArrayList<>();
        tempList.addAll(vipService.searchVipByKey(keyword));
        tempList.addAll(regService.searchRegByKey(keyword));

        return tempList;
    }

    public void updateMemData(){
        vipService.updateVipData();
        regService.updateRegData();
    }

}
