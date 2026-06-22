package storage;

import model.Regular;
import util.ExistCheck;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegStorage {
    private static final String FOLDER = "data";
    private static final String FILE_PATH = "data/reg.txt";

    private String regToLine(Regular reg){
        return reg.getMemberId() + '|'
                + reg.getName() + '|'
                + reg.getPhone() + '|'
                + reg.getEmail() + '|'
                + reg.getBookHold();
    }

    public void saveOne(Regular reg){
        File folder = new File(FOLDER);
        ExistCheck.fileMkdir(folder);


        try(FileWriter fileWriter = new FileWriter(FILE_PATH,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

            String line=regToLine(reg);
            bufferedWriter.write(line);
            bufferedWriter.newLine();

        }catch (IOException e){
            System.out.println("❌ Error: Failed to save the new Regular member!");
        }
    }

    public void saveAll(List<Regular> regularList){
        File folder = new File(FOLDER);
        ExistCheck.fileMkdir(folder);


        try(FileWriter fileWriter = new FileWriter(FILE_PATH,false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){

            for(Regular reg:regularList){
                String line = regToLine(reg);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

        }catch (IOException e){
            System.out.println("❌ Error: Failed to save all Regular member!");
        }
    }

    private Regular lineToReg(String line){
        try{
            String[] part = line.split("\\|");
            String memberId = part[0].trim();
            String name = part[1].trim();
            String phone = part[2].trim();
            String email = part[3].trim();
            int bookHold = Integer.parseInt(part[4].trim());

            Regular reg = new Regular(memberId,name,phone,email);
            reg.setBookHold(bookHold);
            return reg;

        }catch (Exception e){
            System.out.println("❌ Error: Corrupted REG data line -> " + line);
            return null;
        }
    }

    public List<Regular>load(){
        List<Regular>regularList = new ArrayList<>();
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return regularList;
        }

        try(FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader reader = new BufferedReader(fileReader)){

            String data;
            while((data=reader.readLine())!=null){
                if(data.trim().isEmpty()){
                    continue;
                }

                Regular reg = lineToReg(data);
                if(reg!=null){
                    regularList.add(reg);
                }
            }

        }catch (IOException e){
            System.out.println("⚠️ Warning: Failed to load REG data from file!");
        }

        return regularList;
    }
}
