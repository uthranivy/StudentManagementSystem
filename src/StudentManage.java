import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;



public class StudentManage {
    Scanner sc = new Scanner(System.in);
    StudentRecord stud = new StudentRecord(sc);
    public void select() throws IOException {
//        List<Student> students = new ArrayList<>();
        int choice = 0;

        while (choice != 6) {

            try {
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
