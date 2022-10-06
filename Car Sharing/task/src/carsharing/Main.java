package carsharing;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);

        DatabaseAccess da = new DatabaseAccess();
        da.createTables();

        Menu menu = new Menu(scanner);
        menu.startMenu();
    }
}