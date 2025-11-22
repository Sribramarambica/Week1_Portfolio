import java.util.Scanner;

public class StudentInfoManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Student Information System ===");

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Student Age: ");
        int age = sc.nextInt();

        System.out.print("Enter Student Grade: ");
        char grade = sc.next().charAt(0);

        System.out.println("\n--- Student Details ---");
        System.out.println("Name  : " + name);
        System.out.println("Age   : " + age);
        System.out.println("Grade : " + grade);

        System.out.println("\nRecord saved successfully!");
        
        sc.close();
    }
}
