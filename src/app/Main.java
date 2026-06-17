package app;

import service.*;
import ui.*;
import util.Helper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Helper helper = new Helper(sc);

        VipService vipService = new VipService();
        RegService regService = new RegService();
        MemService memService = new MemService(vipService, regService);
        BookService bookService = new BookService();
        BillService billService = new BillService(bookService, memService);
        ReportService reportService = new ReportService(billService);

        BookUi bookUi = new BookUi(sc, bookService);
        MemUi memberUi = new MemUi(sc,memService);
        BillUi billUi = new BillUi(sc,bookService,memService,billService);
        ReportUi reportUi = new ReportUi(sc,reportService);
        MainUi mainUi = new MainUi(sc,bookUi,memberUi,billUi,reportUi,bookService,memService,billService);

        mainUi.start();
        sc.close();
    }
}