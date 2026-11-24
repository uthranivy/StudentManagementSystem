import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentManage {
    Scanner sc = new Scanner(System.in);
    StudentRecord stud = new StudentRecord(sc);

    private void printMenu() {
        System.out.println("      !***** STUDENT RECORD SYSTEM *****!");
        System.out.println("1. Add Student Details");
        System.out.println("2. View All Students Details");
        System.out.println("3. Delete Student Details");
        System.out.println("4. Update Student Details");
        System.out.println("5. Search Student by ID");
        System.out.println("6. Exit");
    }

    public void select() throws IOException {
        int choice = 0;

        while (choice != 6) {

            try {
                printMenu();
                System.out.println("Enter Choice:");
                choice = sc.nextInt();

                if (choice < 1 || choice > 6) {
                    throw new NotValid("Invalid Choice! Enter 1 to 6.");
                }

                switch (choice) {
                    case 1:
                        stud.add();
                        break;
                    case 2:
                        stud.viewAll();
                        break;
                    case 3:
                        stud.delete();
                        break;
                    case 4:
                        stud.replace();
                        break;
                    case 5:
                        stud.search();
                        break;
                    case 6:
                        System.out.println("Exit!");
                        return;
                }

            } catch (NotValid e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Please enter numbers only.");
                sc.nextLine();
            }
        }
    }
}
