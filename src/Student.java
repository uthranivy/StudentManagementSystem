public class Student {
    public static final int MIN_AGE = 17;
    public static final int MAX_AGE = 60;
    public static final double MIN_PERCENTAGE = 0.0;
    public static final double MAX_PERCENTAGE = 100.0;

    private int id;
    private String name;
    private int age;
    private String department;
    private double percentage;

    public Student(int id, String name, int age, String department, double percentage) throws NotValid {
        this.id = validateId(id);
        this.name = validateName(name);
        this.age = validateAge(age);
        this.department = validateDepartment(department);
        this.percentage = validatePercentage(percentage);
    }



    public static int validateId(int id) throws NotValid {
        if (id <= 0) {
            throw new NotValid("ID must be a positive number!");
        }
        return id;
    }

    public static String validateName(String name) throws NotValid {
        if (name == null || name.trim().isEmpty()) {
            throw new NotValid("Name cannot be empty!");
        }
        String clean = name.trim().replaceAll("\\s+", " ");
        if (clean.length() < 2 || clean.length() > 50) {
            throw new NotValid("Name must be 2 to 50 characters long!");
        }
        // Letters, spaces, dot, apostrophe, hyphen. No commas, so the CSV file is never broken.
        if (!clean.matches("[A-Za-z][A-Za-z .'-]*")) {
            throw new NotValid("Name can contain only letters, spaces, '.', ''' and '-'!");
        }
        return clean;
    }

    public static int validateAge(int age) throws NotValid {
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new NotValid("Age must be between " + MIN_AGE + " and " + MAX_AGE + "!");
        }
        return age;
    }

    public static String validateDepartment(String department) throws NotValid {
        if (department == null || department.trim().isEmpty()) {
            throw new NotValid("Department cannot be empty!");
        }
        String clean = department.trim().replaceAll("\\s+", " ");
        if (clean.length() < 2 || clean.length() > 50) {
            throw new NotValid("Department must be 2 to 50 characters long!");
        }
        if (!clean.matches("[A-Za-z][A-Za-z &.-]*")) {
            throw new NotValid("Department can contain only letters, spaces, '&', '.' and '-'!");
        }
        return clean;
    }

    public static double validatePercentage(double percentage) throws NotValid {
        if (Double.isNaN(percentage) || Double.isInfinite(percentage)
                || percentage < MIN_PERCENTAGE || percentage > MAX_PERCENTAGE) {
            throw new NotValid("Percentage must be between " + MIN_PERCENTAGE + " and " + MAX_PERCENTAGE + "!");
        }
        return percentage;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public double getPercentage() {
        return percentage;
    }



    public void setName(String name) throws NotValid {
        this.name = validateName(name);
    }

    public void setAge(int age) throws NotValid {
        this.age = validateAge(age);
    }

    public void setDepartment(String department) throws NotValid {
        this.department = validateDepartment(department);
    }

    public void setPercentage(double percentage) throws NotValid {
        this.percentage = validatePercentage(percentage);
    }
}
