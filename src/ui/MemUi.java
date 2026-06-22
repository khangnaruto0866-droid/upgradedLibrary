package ui;

import model.Member;
import model.Regular;
import model.Vip;
import service.MemService;
import util.*;

import java.util.List;
import java.util.Scanner;

public class MemUi {
    private Scanner sc;
    private Helper helper;
    private MemService memService;
    private ConsoleHelper console;

    public MemUi(Scanner sc, MemService memService) {
        this.sc = sc;
        this.memService = memService;
        this.helper = new Helper(sc);
        this.console = new ConsoleHelper(sc);
    }

    public void start(){
        boolean isRunning = true;
        while(isRunning){
            console.clearScreen();
            System.out.println("\n==================================================");
            System.out.println("   👤✨ LIBRARY SYSTEM : MEMBER MANAGEMENT ✨👤   ");
            System.out.println("==================================================");
            System.out.println("  [1] ➕ Add New Member");
            System.out.println("  [2] ✏️  Update Member Information");
            System.out.println("  [3] 📋 View All Members");
            System.out.println("  [4] 🗑️  Remove a Member");
            System.out.println("  [5] 🔍 Search Members");
            System.out.println("  [0] 🔙 Back to Main Menu");
            System.out.println("==================================================");
            System.out.print(" 🎯 Select an option: ");

            try{
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice){
                    case 1:{
                        handleAddMem();
                        break;
                    }

                    case 2:{
                        handleUpdateMem();
                        break;
                    }

                    case 3:{
                        handleDisplayMem();
                        break;
                    }

                    case 4:{
                        handleRemoveMem();
                        break;
                    }

                    case 5:{
                        handleSearchMem();
                        break;
                    }

                    case 0:{
                        isRunning = false;
                        System.out.println(" 🔙 Returning to Main Menu...");
                        break;
                    }

                    default:
                        System.out.println(" ❌ Invalid choice! Please select from 0 to 5.");
                        console.pause();                }

            }catch (NumberFormatException e){
                System.out.println(" ❌ Invalid choice! Please select from 0 to 5.");
                console.pause();
            }
        }
    }

    private void handleAddMem(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("              ➕ ADD A NEW MEMBER ➕              ");
        System.out.println("--------------------------------------------------");

        String typeChoice;
        while (true){
            System.out.println("  [1] 🌟 VIP Member (ID starts with VIP)");
            System.out.println("  [2] 🟢 Regular Member (ID starts with REG)");
            System.out.println("  [0] ❌ Cancel");
            System.out.print(" 👉 Select Member Type: ");
            typeChoice = sc.nextLine().trim();

            if (typeChoice.equals("1") || typeChoice.equals("2") || typeChoice.equals("0")) {
                break;
            }
            System.out.println(" ❌ Invalid choice! Please select 1, 2, or 0.\n");
        }

        if(typeChoice.equals("0")){
            System.out.println(" 🚫 Action cancelled.");
            console.pause();
            return;
        }

        try{
            String memId;
            if(typeChoice.equals("1")){
                memId = helper.readVipId(" 🏷️ Enter VIP ID (e.g., VIP001): ");
            }

            else {
                memId = helper.readRegId(" 🏷️ Enter Regular ID (e.g., REG001): ");
            }

            String name = Beauty.beauty(helper.readName(" 👤 Enter Full Name: "));
            String phone = helper.readPhone(" 📱 Enter Phone Number: ");
            String email = helper.readEmail(" 📧 Enter Email Address: ");

            System.out.println("\n ❓ Do you want to save this member?");
            boolean confirm = helper.readYesNo(" 👉 Enter [Y] to Save or [N] to Cancel: ");

            if(confirm){
              if(typeChoice.equals("1")){
                  memService.addVip(new Vip(memId,name,phone,email));
              }

              else{
                  memService.addReg(new Regular(memId,name,phone,email));
              }

                System.out.println("\n ✅ SUCCESS: Member '" + name + "' added successfully!");
            }

            else{
                System.out.println("\n 🚫 Action cancelled.");
            }

        }catch (IllegalArgumentException e){
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }

    private void handleUpdateMem(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("            ✏️  UPDATE MEMBER INFO ✏️            ");
        System.out.println("--------------------------------------------------");

        try{
            String memId = helper.readMemId(" 🏷️ Enter Member ID to update: ");
            Member member = ExistCheck.noNull(memService.findMemById(memId));

            System.out.println("\n 📌 CURRENT INFORMATION:");
            System.out.println(member.toString());
            System.out.println("\n 💡 (Press ENTER to keep the current value and skip to the next)");

            System.out.print(" 👤 Enter new Name [" + member.getName() + "]: ");
            String name = sc.nextLine();
            String finalName = name.trim().isEmpty() ? member.getName() : Beauty.beauty(name);

            System.out.print(" 📱 Enter new Phone [" + member.getPhone() + "]: ");
            String phone = sc.nextLine();
            String finalPhone = phone.trim().isEmpty() ? member.getPhone() : Beauty.beauty(phone);

            System.out.print(" 📧 Enter new Email [" + member.getEmail() + "]: ");
            String email = sc.nextLine();
            String finalEmail = email.trim().isEmpty() ? member.getEmail() : email.trim();

            System.out.println("\n ❓ Save changes for this member?");
            boolean confirm = helper.readYesNo(" 👉 Enter [Y] to Update or [N] to Cancel: ");

            if(confirm){
                memService.updateMem(memId,finalName,finalPhone,finalEmail);
                System.out.println("\n ✅ SUCCESS: Member information updated successfully!");
            }

            else{
                System.out.println("\n 🚫 Action cancelled.");
            }

        }catch (IllegalArgumentException e){
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }
    }

    private void handleDisplayMem(){
        console.clearScreen();
        System.out.println("\n------------------------------------------------------------------------------------------------------");
        System.out.println("                                      📋 ALL REGISTERED MEMBERS 📋                                      ");
        System.out.println("------------------------------------------------------------------------------------------------------");

        List<Member>memberList = memService.displayMemberList();
        if(memberList.isEmpty()){
            System.out.println(" ⚠️ No members found in the library. Please add some first!");
        }

        else{
            System.out.printf(" %-10s | %-12s | %-25s | %-15s | %-25s%n",
                    "ID", "Type", "Full Name", "Phone", "Email");

            for(Member member : memberList){
                String type = (member instanceof Vip) ? "🌟 VIP" : "🟢 REGULAR";
                System.out.printf(" %-10s | %-12s | %-25s | %-15s | %-25s%n",
                        member.getMemberId(),
                        type,
                        Beauty.formatMemNameLength(member.getName(), 25),
                        member.getPhone(),
                        Beauty.formatStringLength(member.getEmail(), 25));
            }
        }

        System.out.println("------------------------------------------------------------------------------------------------------");
        console.pause();
    }

    private void handleRemoveMem(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("              🗑️  REMOVE A MEMBER 🗑️              ");
        System.out.println("--------------------------------------------------");

        try{
            String memId = helper.readMemId(" 🏷️ Enter Member ID to remove: ");
            Member member = ExistCheck.noNull(memService.findMemById(memId));

            System.out.println("\n 📌 MEMBER FOUND:");
            System.out.println(" 👤 Name: " + member.getName());
            System.out.println(" 📱 Phone: " + member.getPhone());

            System.out.println("\n ⚠️ WARNING: Are you sure you want to completely delete this member?");
            boolean confirm = helper.readYesNo(" 👉 Enter [Y] to Confirm or [N] to Cancel: ");

            if(confirm){
                memService.removeMem(memId);
                System.out.println("\n ✅ SUCCESS: Member removed successfully!");
            }

            else{
                System.out.println("\n 🚫 Action cancelled.");
            }

        }catch (IllegalArgumentException e){
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }

    private void handleSearchMem(){
        console.clearScreen();
        System.out.println("\n--------------------------------------------------");
        System.out.println("              🔍 SEARCH FOR MEMBERS 🔍            ");
        System.out.println("--------------------------------------------------");

        try{
            System.out.print(" 🔑 Enter keyword (Name, Phone, or ID): ");
            String keyword = Validator.validString(sc.nextLine());
            List<Member>result = memService.searchMemByKey(keyword);

            if (result.isEmpty()) {
                System.out.println("\n ⚠️ No matching members found for keyword: '" + keyword + "'");
            }

            else{
                System.out.println("\n 🎯 SEARCH RESULTS (" + result.size() + " found):");
                for (Member member : result) {
                    System.out.println(" " + member.toString());
                }
            }

        }catch (IllegalArgumentException e){
            System.out.println("\n ❌ ERROR: " + e.getMessage());
        }

        console.pause();
    }
}
