
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;




public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);


        BufferedReader br = new BufferedReader(new FileReader("student.txt"));
        System.out.println("      !***** STUDENT RECORD SYSTEM *****!");
        System.out.println("** Please select the choices in the given order **");
        System.out.println("1. Add Student Details");
        System.out.println("2. View All Students Details");
        System.out.println("3. Delete Student Details");
        System.out.println("4. Update Student Details");
        System.out.println("5. Search Student by ID ");
        System.out.println("6. Exit");
        StudentManage Ch = new StudentManage();
         Ch.select();




 
    }
}
