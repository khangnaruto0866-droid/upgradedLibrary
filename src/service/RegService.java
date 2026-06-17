package service;

import model.Regular;
import storage.RegStorage;
import util.ExistCheck;

import java.util.ArrayList;
import java.util.List;

public class RegService {
    private RegStorage regStorage;
    private List<Regular> regulars;

    public RegService(){
        regStorage = new RegStorage();
        regulars = validateRegList(regStorage.load());
    }

    private List<Regular>validateRegList(List<Regular>regularList){
        if(regularList==null){
            regularList = new ArrayList<>();
        }

        return regularList;
    }

    Regular findRegById(String regId){
        for(Regular regular:regulars){
            if(regular.getMemberId().equals(regId)){
                return regular;
            }
        }

        return null;
    }

    void addReg(Regular reg){
        if(findRegById(reg.getMemberId())!=null){
            throw new IllegalArgumentException("❌ Error: Regular ID already exists!");
        }

        regulars.add(reg);
        regStorage.saveOne(reg);
    }

    List<Regular> displayRegularList(){
        return new ArrayList<>(this.regulars);
    }

    List<Regular>searchRegByKey(String key){
        String keyword = key.toLowerCase();
        List<Regular>tempList = new ArrayList<>();

        for(Regular reg:regulars){
            if (reg.getMemberId().toLowerCase().contains(keyword) ||
                reg.getName().toLowerCase().contains(keyword) ||
                reg.getPhone().contains(keyword)) {
                tempList.add(reg);
            }
        }

        return tempList;
    }

    void removeReg(String regId){
        Regular reg = ExistCheck.noNull(findRegById(regId));
        if(reg.getBookHold()>0){
            throw new IllegalArgumentException("❌ Error: Cannot remove! This Regular member is currently holding books.");
        }

        regulars.remove(reg);
        regStorage.saveAll(regulars);
    }

    void updateRegData(){
        regStorage.saveAll(regulars);
    }
}
