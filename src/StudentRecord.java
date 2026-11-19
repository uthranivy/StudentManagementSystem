import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


class NotValid extends Exception {
    public NotValid(String s) {
        super(s);
    }
}

public class StudentRecord {
    private static final String FILE = "student.txt";
    Scanner sc;
    List<Student> students = new ArrayList<>();
    public String line;

    public StudentRecord(Scanner sc) {
        this.sc = sc;
    }

    public static Student convertToStudent(String line) {

        if (line.trim().isEmpty()) {
            return null;
        }

        String[] data = line.split(",");

        return new Student(
                Integer.parseInt(data[0]),
                data[1],
                Integer.parseInt(data[2]),
                data[3],
                Double.parseDouble(data[4])
        );
    }
    private void print(Student s) {
        System.out.println("ID: " + s.getId() + " | Name: " + s.getName()
                + " | Age: " + s.getAge() + " | Department: " + s.getDepartment()
                + " | Percentage: " + s.getPercentage());
    }
    private boolean idExists(int id) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader("student.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student s = convertToStudent(line);
                if (s != null && s.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }


    public void add() throws IOException, NotValid {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("student.txt", true))) {

            int id;
            int age;
            double percentage;
            String name;
            String depart;


            try {
                System.out.println("Enter ID:");
                id = sc.nextInt();
                sc.nextLine();

                if (idExists(id)) {
                    throw new NotValid("ID " + id + " already exists!");
                }

            } catch (InputMismatchException e) {
                sc.nextLine();
                throw new NotValid("ID must be an integer!");
            }


            System.out.println("Enter Name:");
            name = sc.nextLine();

            if (!name.matches("[a-zA-Z ]+")) {
                throw new NotValid("Name must contain only letters!");
            }


            try {
                System.out.println("Enter Age:");
                age = sc.nextInt();
                sc.nextLine();

            } catch (InputMismatchException e) {
                sc.nextLine();
                throw new NotValid("Age must be an integer!");
            }


            System.out.println("Enter Department:");
            depart = sc.nextLine();

            if (!depart.matches("[a-zA-Z ]+") && depart.trim().isEmpty()) {
                throw new NotValid("Department name must contain only letters!");
            }

            try {
                System.out.println("Enter Percentage:");
                percentage = sc.nextDouble();

            } catch (InputMismatchException e) {
                sc.nextLine();
                throw new NotValid("Percentage must be a number!");
            }


            bw.write(id + "," + name + "," + age + ","
                    + depart + "," + percentage);

            bw.newLine();

            System.out.println("Student added successfully!");
        }
    }



    public void viewAll() {
        students.clear();
        System.out.println("                 !---------Student List---------!");
        try (BufferedReader reader = new BufferedReader(new FileReader("student.txt"))) {
            while ((line = reader.readLine()) != null) {
                Student s1 = convertToStudent(line);
                if (s1 != null) {
                    students.add(s1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Student student : students) {
            print(student);
        }
    }

    public void delete() throws IOException {
        System.out.println("Enter Student ID: ");
        int idValue = sc.nextInt();
        List<Student> tempList = new ArrayList<>();
        boolean found = false;

        try (BufferedReader bRead = new BufferedReader(new FileReader("student.txt"))) {
            while ((line = bRead.readLine()) != null) {
                Student s1 = convertToStudent(line);

                if (s1 == null) continue;
                
                if (s1.getId() == idValue) {
                    found = true;
                }   else {
                    tempList.add(s1);
                }

            }
        }


        if(found) {
            try (BufferedWriter bWrite = new BufferedWriter(new FileWriter("student.txt"))) {  // no "true"
                for (Student st : tempList) {
                    bWrite.write(st.getId() + "," + st.getName() + "," + st.getAge() + ","
                            + st.getDepartment() + "," + st.getPercentage());
                    bWrite.newLine();
                    print(st);
                }
            }
            System.out.println("Student Details Deleted Successfully!");
        } else {
            System.out.println("Enter Student ID not found!");
        }
    }


    public void replace() throws IOException, NotValid {
        System.out.println("Enter Student ID: ");
        int idStud = sc.nextInt();
        List<Student> studList = new ArrayList<>();
        Student target = null;




        try (BufferedReader bRead = new BufferedReader(new FileReader("student.txt"))) {
            while ((line = bRead.readLine()) != null) {
                Student s2 = convertToStudent(line);
                studList.add(s2);
                if (s2.getId() == idStud) {
                    target = s2;
                }
            }
        }
        if (target == null) {
            throw new NotValid("Student ID " + idStud + " not found!");
        }


        System.out.println("Enter Update Choice (1.Name, 2. Age, 3. Department, 4. Percentage): ");
        int update = sc.nextInt();
        switch(update){
            case 1:
                sc.nextLine();
                System.out.println("Enter name: ");
                String name = sc.nextLine();

                if (!name.matches("[a-zA-Z ]+")) {
                    throw new NotValid("Name must contain only letters!");
                }
                  target.setName(name);


                break;
            case 2:
                System.out.println("Enter Age: ");
                try {
                    int age = sc.nextInt();

                    if (age <= 0) {
                        throw new NotValid("Age must be positive!");
                    }

                    target.setAge(age);

                } catch (InputMismatchException e) {
                    sc.nextLine();
                    throw new NotValid("Age must be an integer!");
                }
                break;
            case 3:
                sc.nextLine();
                System.out.println("Enter Department: ");
                String depart = sc.nextLine();

                if (!depart.matches("[a-zA-Z ]+")) {
                    throw new NotValid(
                            "Department must contain only letters!"
                    );
                }

                target.setDepartment(depart);
                break;
            case 4:
                System.out.println("Enter Percentage: ");
                try {
                    double percentage = sc.nextDouble();

                    if (percentage < 0 || percentage > 100) {
                        throw new NotValid(
                                "Percentage must be between 0 and 100!"
                        );
                    }

                    target.setPercentage(percentage);

                } catch (InputMismatchException e) {
                    sc.nextLine();
                    throw new NotValid("Percentage must be a number!");
                }
                break;
            default:
                System.out.println("Invalid option!");
                return;
        }
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter("student.txt")))  {
            for (Student st : studList) {
                bWriter.write(st.getId() + "," + st.getName() + "," + st.getAge() + ","
                        + st.getDepartment() + "," + st.getPercentage());
                bWriter.newLine();
                print(st);
            }
        }
        System.out.println("Student details updated successfully.");
    }


    public void search(){
        System.out.println("Enter Student ID: ");
        int idSearch = sc.nextInt();
        List<Student> studSearch = new ArrayList<>();
        Student target = null;

        try (BufferedReader buffRead = new BufferedReader(new FileReader("student.txt"))) {
            while ((line = buffRead.readLine()) != null) {
                Student s2 = convertToStudent(line);

                if (s2.getId() == idSearch) {
                    studSearch.add(s2);
                    print(s2);
                    target=s2;
                }  
            }
            if (target == null) {
                System.out.println("Student ID " + idSearch + " not found!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}