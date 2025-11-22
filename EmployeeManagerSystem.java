import java.io.*;
import java.util.*;

// ---------------- Employee Class ----------------
class Employee {
    private int id;
    private String name;
    private double salary;

    // Constructor
    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    // Getters & Setters
    public int getId() { return id; }

    public String getName() { return name; }

    public double getSalary() { return salary; }

    public void setName(String name) { this.name = name; }

    public void setSalary(double salary) { this.salary = salary; }

    // Format for file saving
    public String toFileFormat() {
        return id + "," + name + "," + salary;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Salary: " + salary;
    }
}


// ---------------- Employee Manager Class ----------------
class EmployeeManager {

    private HashMap<Integer, Employee> employees = new HashMap<>();
    private final String FILE_NAME = "employees.txt";

    // Constructor → Load file data at start
    public EmployeeManager() {
        loadFromFile();
    }

    // CREATE
    public void addEmployee(Employee e) {
        if (employees.containsKey(e.getId())) {
            System.out.println("Employee ID already exists!");
        } else {
            employees.put(e.getId(), e);
            saveToFile();
            System.out.println("Employee added successfully!");
        }
    }

    // READ
    public void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("\n--- Employee List ---");
        for (Employee e : employees.values()) {
            System.out.println(e);
        }
    }

    // UPDATE
    public void updateEmployee(int id, String newName, double newSalary) {
        if (employees.containsKey(id)) {
            Employee e = employees.get(id);
            e.setName(newName);
            e.setSalary(newSalary);
            saveToFile();
            System.out.println("Employee updated successfully!");
        } else {
            System.out.println("Employee not found!");
        }
    }

    // DELETE
    public void deleteEmployee(int id) {
        if (employees.containsKey(id)) {
            employees.remove(id);
            saveToFile();
            System.out.println("Employee deleted successfully!");
        } else {
            System.out.println("Employee not found!");
        }
    }

    // SAVE to File
    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Employee e : employees.values()) {
                pw.println(e.toFileFormat());
            }
        } catch (Exception ex) {
            System.out.println("Error saving file: " + ex.getMessage());
        }
    }

    // LOAD from File
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                double salary = Double.parseDouble(data[2]);
                employees.put(id, new Employee(id, name, salary));
            }

        } catch (Exception ex) {
            System.out.println("Error loading file: " + ex.getMessage());
        }
    }
}


// ---------------- MAIN APP CLASS ----------------
public class EmployeeManagerSystem {

    public static void main(String[] args) {

        EmployeeManager manager = new EmployeeManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Employee Management System ===");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble();

                    manager.addEmployee(new Employee(id, name, salary));
                    break;

                case 2:
                    manager.viewEmployees();
                    break;

                case 3:
                    System.out.print("Enter ID to update: ");
                    int uid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new name: ");
                    String uname = sc.nextLine();

                    System.out.print("Enter new salary: ");
                    double usalary = sc.nextDouble();

                    manager.updateEmployee(uid, uname, usalary);
                    break;

                case 4:
                    System.out.print("Enter ID to delete: ");
                    int did = sc.nextInt();
                    manager.deleteEmployee(did);
                    break;

                case 5:
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
