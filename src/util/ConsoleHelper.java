package util;

import java.util.Scanner;

public class ConsoleHelper {
    private Scanner sc;

    public ConsoleHelper(Scanner sc) {
        this.sc = sc;
    }

    public void clearScreen(){
        for(int i=0;i<50;i++){
            System.out.println();
        }
    }

    public void pause(){
        System.out.println("👉 Press Enter to continue...");
        while(sc.hasNextLine()){
            String input = sc.nextLine();
            if(input.trim().isEmpty()) {
                break;
            }
        }
    }
}
