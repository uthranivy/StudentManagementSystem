import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class StudentRecord {
    private static final Path FILE = Paths.get("student.txt");
    private static final Path TEMP = Paths.get("student.txt.tmp");
    private static final Path BACKUP = Paths.get("student.txt.bak");

    private final Scanner sc;

    private final List<Student> students = new ArrayList<>();

    public StudentRecord(Scanner sc) {
        this.sc = sc;
        load();
    }




    public static Student parseLine(String line) throws NotValid {
        String[] data = line.split(",", -1);
        if (data.length != 5) {
            throw new NotValid("expected 5 comma-separated fields but found " + data.length);
        }
        try {
            return new Student(
                    Integer.parseInt(data[0].trim()),
                    data[1],
                    Integer.parseInt(data[2].trim()),
                    data[3],
                    Double.parseDouble(data[4].trim())
            );
        } catch (NumberFormatException e) {
            throw new NotValid("a number field is not a valid number");
        }
    }

    private static String toLine(Student s) {
        return s.getId() + "," + s.getName() + "," + s.getAge() + ","
                + s.getDepartment() + "," + s.getPercentage();
    }

    /** Loads the file once. Bad lines are reported and skipped, never crash the program. */
    private void load() {
        students.clear();
        if (!Files.exists(FILE)) {
            return;
        }

        int bad = 0;
        int lineNo = 0;
        try (BufferedReader reader = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Student s = parseLine(line);
                    if (indexOf(s.getId()) >= 0) {
                        throw new NotValid("duplicate ID " + s.getId());
                    }
                    students.add(s);
                } catch (NotValid e) {
                    bad++;
                    System.out.println("Warning: skipped line " + lineNo + " in " + FILE + " (" + e.getMessage() + ")");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: could not read " + FILE + " (" + e.getMessage() + ")");
            return;
        }

        if (bad > 0) {
            // Keep the original so skipped lines are not lost when the file is saved again
            try {
                Files.copy(FILE, BACKUP, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(bad + " bad line(s) skipped. Original file saved as " + BACKUP + ".");
            } catch (IOException e) {
                System.out.println(bad + " bad line(s) skipped. Could not create backup: " + e.getMessage());
            }
        }
    }

    /** Writes everything to a temp file, then replaces the real file in one step. */
    private void save() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(TEMP, StandardCharsets.UTF_8)) {
            for (Student s : students) {
                bw.write(toLine(s));
                bw.newLine();
            }
        }
        try {
            Files.move(TEMP, FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(TEMP, FILE, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // ---------- Input helpers ----------

    private int readInt(String prompt, String field) throws NotValid {
        System.out.println(prompt);
        try {
            int value = sc.nextInt();
            sc.nextLine();
            return value;
        } catch (InputMismatchException e) {
            sc.nextLine();
            throw new NotValid(field + " must be an integer!");
        }
    }

    private double readDouble(String prompt, String field) throws NotValid {
        System.out.println(prompt);
        try {
            double value = sc.nextDouble();
            sc.nextLine();
            return value;
        } catch (InputMismatchException e) {
            sc.nextLine();
            throw new NotValid(field + " must be a number!");
        }
    }

    private int indexOf(int id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void print(Student s) {
        System.out.println("ID: " + s.getId() + " | Name: " + s.getName()
                + " | Age: " + s.getAge() + " | Department: " + s.getDepartment()
                + " | Percentage: " + s.getPercentage());
    }

    // ---------- Operations ----------

    public void add() throws IOException, NotValid {
        int id = Student.validateId(readInt("Enter ID:", "ID"));
        if (indexOf(id) >= 0) {
            throw new NotValid("ID " + id + " already exists!");
        }

        System.out.println("Enter Name:");
        String name = Student.validateName(sc.nextLine());

        int age = Student.validateAge(readInt("Enter Age:", "Age"));

        System.out.println("Enter Department:");
        String depart = Student.validateDepartment(sc.nextLine());

        double percentage = Student.validatePercentage(readDouble("Enter Percentage:", "Percentage"));

        Student s = new Student(id, name, age, depart, percentage);
        students.add(s);
        try {
            save();
        } catch (IOException e) {
            students.remove(s);   // keep memory and file consistent
            throw e;
        }
        System.out.println("Student added successfully!");
    }

    public void viewAll() {
        System.out.println("                 !---------Student List---------!");
        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (Student s : students) {
            print(s);
        }
    }

    public void delete() throws IOException, NotValid {
        int id = readInt("Enter Student ID: ", "ID");
        int index = indexOf(id);
        if (index < 0) {
            System.out.println("Student ID not found!");
            return;
        }

        Student removed = students.remove(index);
        try {
            save();
        } catch (IOException e) {
            students.add(index, removed);
            throw e;
        }
        System.out.println("Student Details Deleted Successfully!");
    }

    public void replace() throws IOException, NotValid {
        int id = readInt("Enter Student ID: ", "ID");
        int index = indexOf(id);
        if (index < 0) {
            throw new NotValid("Student ID " + id + " not found!");
        }
        Student old = students.get(index);

        int choice = readInt("Enter Update Choice (1.Name, 2. Age, 3. Department, 4. Percentage): ", "Choice");

        String name = old.getName();
        int age = old.getAge();
        String depart = old.getDepartment();
        double percentage = old.getPercentage();

        switch (choice) {
            case 1:
                System.out.println("Enter name: ");
                name = sc.nextLine();
                break;
            case 2:
                age = readInt("Enter Age: ", "Age");
                break;
            case 3:
                System.out.println("Enter Department: ");
                depart = sc.nextLine();
                break;
            case 4:
                percentage = readDouble("Enter Percentage: ", "Percentage");
                break;
            default:
                System.out.println("Invalid option!");
                return;
        }

        // The constructor validates everything; the list is only touched if it passes
        Student updated = new Student(id, name, age, depart, percentage);
        students.set(index, updated);
        try {
            save();
        } catch (IOException e) {
            students.set(index, old);
            throw e;
        }
        System.out.println("Student details updated successfully.");
    }

    public void search() throws NotValid {
        int id = readInt("Enter Student ID: ", "ID");
        int index = indexOf(id);
        if (index < 0) {
            System.out.println("Student ID " + id + " not found!");
        } else {
            print(students.get(index));
        }
    }
}