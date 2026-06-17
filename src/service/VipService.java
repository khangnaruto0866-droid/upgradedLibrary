package service;

import model.Vip;
import storage.VipStorage;
import util.ExistCheck;

import java.util.ArrayList;
import java.util.List;

public class VipService {
    private VipStorage vipStorage;
    private List<Vip> vips;

    public VipService(){
        vipStorage = new VipStorage();
        vips = validateVipList(vipStorage.load());
    }

    private List<Vip>validateVipList(List<Vip>vipList){
        if(vipList==null){
            vipList=new ArrayList<>();
        }

        return vipList;
    }

    Vip findVipById(String vipId){
        for(Vip vip:vips){
            if(vip.getMemberId().equals(vipId)){
                return vip;
            }
        }

        return null;
    }

    void addVip(Vip vip){
        if(findVipById(vip.getMemberId())!=null){
            throw new IllegalArgumentException("❌ Error: VIP ID already exists!");
        }

        vips.add(vip);
        vipStorage.saveOne(vip);
    }

    List<Vip> displayVipList(){
        return new ArrayList<>(this.vips);
    }

    List<Vip>searchVipByKey(String key){
        String keyword = key.toLowerCase();
        List<Vip>tempList = new ArrayList<>();

        for(Vip vip:vips){
            if(vip.getMemberId().toLowerCase().contains(keyword) ||
               vip.getName().toLowerCase().contains(keyword) ||
               vip.getPhone().contains(keyword)){
                tempList.add(vip);
            }
        }

        return tempList;
    }

    void removeVip(String vipId){
        Vip vip = ExistCheck.noNull(findVipById(vipId));
        if(vip.getBookHold()>0){
            throw new IllegalArgumentException("❌ Error: Cannot remove! This VIP is currently holding books.");
        }

        vips.remove(vip);
        vipStorage.saveAll(vips);
    }


    void updateVipData(){
        vipStorage.saveAll(vips);
    }

}
