package storage;

import model.Vip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VipStorage {
    private static final String FOLDER = "data";
    private static final String FILE_PATH = "data/vip.txt";

    private String vipToLine(Vip vip){
        return vip.getMemberId() + '|'
                + vip.getName() + '|'
                + vip.getPhone() + '|'
                + vip.getEmail() + '|'
                + vip.getBookHold();
    }

    public void saveOne(Vip vip){
        File folder = new File(FOLDER);
        if(!folder.exists()){
            folder.mkdir();
        }

        try(FileWriter fileWriter = new FileWriter(FILE_PATH,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

            String line = vipToLine(vip);
            bufferedWriter.write(line);
            bufferedWriter.newLine();

        }catch (IOException e){
            System.out.println("❌ Error: Failed to save the new VIP member!");
        }
    }

    public void saveAll(List<Vip>vipList){
        File folder = new File(FOLDER);
        if(!folder.exists()){
            folder.mkdir();
        }

        try(FileWriter fileWriter = new FileWriter(FILE_PATH,false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

            for(Vip vip:vipList){
                String line = vipToLine(vip);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

        }catch (IOException e){
            System.out.println("❌ Error: Failed to save all VIP member!");
        }
    }


    private Vip lineToVip(String line){
        try{
            String[] part = line.split("\\|");
            String memberId = part[0].trim();
            String name = part[1].trim();
            String phone = part[2].trim();
            String email = part[3].trim();
            int bookHold = Integer.parseInt(part[4].trim());

            Vip vip = new Vip(memberId,name,phone,email);
            vip.setBookHold(bookHold);
            return vip;

        }catch (Exception e){
            System.out.println("❌ Error: Corrupted VIP data line -> " + line);
            return null;
        }
    }

    public List<Vip>load(){
        List<Vip>vipList=new ArrayList<>();
        File file = new File(FILE_PATH);

        if(!file.exists()){
            return vipList;
        }

        try(FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader reader = new BufferedReader(fileReader)){

            String data;
            while((data= reader.readLine())!=null){
                if(data.trim().isEmpty()){
                    continue;
                }

                Vip vip = lineToVip(data);
                if(vip!=null){
                    vipList.add(vip);
                }
            }

        }catch (IOException e){
            System.out.println("⚠️ Warning: Failed to load VIP data from file!");
        }

        return vipList;
    }
}
