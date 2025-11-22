import java.util.Scanner;

// ---------------------- Book Class ----------------------
class Book {
    private String title;
    private String author;
    private boolean isIssued;

    // Constructor
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    // Getter methods (Encapsulation)
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isIssued() {
        return isIssued;
    }

    // Issue the book
    public void issueBook() {
        if (!isIssued) {
            isIssued = true;
            System.out.println("Book issued successfully!");
        } else {
            System.out.println("Sorry! Book already issued.");
        }
    }

    // Return the book
    public void returnBook() {
        if (isIssued) {
            isIssued = false;
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("This book was not issued.");
        }
    }
}


// ---------------------- Member Class ----------------------
class Member {
    private String name;
    private String memberId;
    private Book issuedBook;  // Relationship: Member HAS-A Book

    // Constructor
    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
        this.issuedBook = null;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getMemberId() {
        return memberId;
    }

    // Issue a book to the member
    public void issueBook(Book book) {
        if (issuedBook == null) {
            book.issueBook();
            issuedBook = book;
        } else {
            System.out.println("You already have a book issued!");
        }
    }

    // Return the book
    public void returnBook() {
        if (issuedBook != null) {
            issuedBook.returnBook();
            issuedBook = null;
        } else {
            System.out.println("No book to return!");
        }
    }
}


// ---------------------- Main Class ----------------------
public class SimpleLibrarySystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Creating sample objects
        Book book1 = new Book("Java Programming", "James Gosling");
        Book book2 = new Book("OOP Concepts", "Dennis Ritchie");

        System.out.println("=== Library Management System ===");

        System.out.print("Enter Member Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Member ID: ");
        String id = sc.nextLine();

        Member member = new Member(name, id);

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Issue Book 1");
            System.out.println("2. Issue Book 2");
            System.out.println("3. Return Book");
            System.out.println("4. View Member Details");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    member.issueBook(book1);
                    break;

                case 2:
                    member.issueBook(book2);
                    break;

                case 3:
                    member.returnBook();
                    break;

                case 4:
                    System.out.println("\n--- Member Info ---");
                    System.out.println("Name      : " + member.getName());
                    System.out.println("Member ID : " + member.getMemberId());
                    break;

                case 5:
                    System.out.println("Thank you for using the Library System!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
