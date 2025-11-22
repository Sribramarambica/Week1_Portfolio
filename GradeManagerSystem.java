import java.util.Scanner;

public class GradeManagerSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== Grade Management System ===");

        // Step 1: Take input for number of subjects
        System.out.print("Enter number of subjects: ");
        int subjectCount = sc.nextInt();

        // Step 2: Create array to store marks
        int[] marks = new int[subjectCount];

        // Step 3: Take marks input using loop
        System.out.println("\nEnter marks for each subject:");
        for (int i = 0; i < subjectCount; i++) {
            System.out.print("Subject " + (i + 1) + ": ");
            marks[i] = sc.nextInt();
        }

        // Step 4: Calculate total
        int total = 0;
        for (int m : marks) {
            total += m;
        }

        // Step 5: Calculate average
        double average = (double) total / subjectCount;

        // Step 6: Determine grade category using if-else
        String gradeCategory;

        if (average >= 90) {
            gradeCategory = "A - Excellent";
        } else if (average >= 75) {
            gradeCategory = "B - Very Good";
        } else if (average >= 60) {
            gradeCategory = "C - Good";
        } else if (average >= 40) {
            gradeCategory = "D - Needs Improvement";
        } else {
            gradeCategory = "F - Failed";
        }

        // Step 7: Display Results
        System.out.println("\n--- Result Summary ---");
        System.out.println("Total Marks : " + total);
        System.out.println("Average     : " + average);
        System.out.println("Grade       : " + gradeCategory);

        sc.close();
    }
}
