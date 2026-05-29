package com.library;
import java.util.List;
import java.util.Scanner;

public class LibraryApp {

    private static final Library library = new Library("City Library");
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("  Welcome to " + library.getName());
        System.out.println("==============================");

        while (true) {
            printMainMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> bookMenu();
                case 2 -> memberMenu();
                case 3 -> borrowMenu();
                case 4 -> searchMenu();
                case 5 -> library.printAllBooks();
                case 6 -> printAllMembers();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ─── MENUS ────────────────────────────────────────────

    private static void printMainMenu() {
        System.out.println("\n──── MAIN MENU ────");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Borrow / Return");
        System.out.println("4. Search Books");
        System.out.println("5. View All Books");
        System.out.println("6. View All Members");
        System.out.println("0. Exit");
    }

    // ─── BOOK MENU ────────────────────────────────────────

    private static void bookMenu() {
        System.out.println("\n──── BOOK MANAGEMENT ────");
        System.out.println("1. Add Physical Book");
        System.out.println("2. Add Ebook");
        System.out.println("3. Add AudioBook");
        System.out.println("4. Remove Book");
        System.out.println("0. Back");

        int choice = readInt("Enter choice: ");
        switch (choice) {
            case 1 -> addPhysicalBook();
            case 2 -> addEbook();
            case 3 -> addAudioBook();
            case 4 -> removeBook();
            case 0 -> { }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void addPhysicalBook() {
        System.out.println("\n-- Add Physical Book --");
        String id     = readString("Book ID       : ");
        String title  = readString("Title         : ");
        String isbn   = readString("ISBN          : ");
        String author = readString("Author        : ");
        int pages     = readInt   ("Total Pages   : ");
        String shelf  = readString("Shelf Location: ");

        library.addBook(new PhysicalBook(id, title, isbn, author, pages, shelf));
    }

    private static void addEbook() {
        System.out.println("\n-- Add Ebook --");
        String id     = readString("Book ID    : ");
        String title  = readString("Title      : ");
        String isbn   = readString("ISBN       : ");
        String author = readString("Author     : ");
        String format = readString("Format (PDF/EPUB): ");
        double size   = readDouble("File Size (MB)   : ");

        library.addBook(new Ebook(id, title, isbn, author, format, size));
    }

    private static void addAudioBook() {
        System.out.println("\n-- Add AudioBook --");
        String id       = readString("Book ID        : ");
        String title    = readString("Title          : ");
        String isbn     = readString("ISBN           : ");
        String author   = readString("Author         : ");
        String narrator = readString("Narrator       : ");
        double duration = readDouble("Duration (hrs) : ");

        library.addBook(new AudioBook(id, title, isbn, author, narrator, duration));
    }

    private static void removeBook() {
        String id = readString("Enter Book ID to remove: ");
        boolean removed = library.removeBook(id);
        System.out.println(removed ? "Book removed." : "Book not found.");
    }

    // ─── MEMBER MENU ──────────────────────────────────────

    private static void memberMenu() {
        System.out.println("\n──── MEMBER MANAGEMENT ────");
        System.out.println("1. Register Member");
        System.out.println("2. Register Faculty");
        System.out.println("0. Back");

        int choice = readInt("Enter choice: ");
        switch (choice) {
            case 1 -> registerMember();
            case 2 -> registerFaculty();
            case 0 -> { }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void registerMember() {
        System.out.println("\n-- Register Member --");
        String id    = readString("Member ID: ");
        String name  = readString("Name     : ");
        String email = readString("Email    : ");

        library.registerMember(new Member(id, name, email));
    }

    private static void registerFaculty() {
        System.out.println("\n-- Register Faculty --");
        String id   = readString("Faculty ID : ");
        String name = readString("Name       : ");
        String email = readString("Email      : ");

        library.registerMember(new Faculty(id, name, email));
    }

    // ─── BORROW MENU ──────────────────────────────────────

    private static void borrowMenu() {
        System.out.println("\n──── BORROW / RETURN ────");
        System.out.println("1. Borrow a Book");
        System.out.println("2. Return a Book");
        System.out.println("0. Back");

        int choice = readInt("Enter choice: ");
        switch (choice) {
            case 1 -> borrowBook();
            case 2 -> returnBook();
            case 0 -> { }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void borrowBook() {
        String memberId = readString("Enter Member ID : ");
        String bookId   = readString("Enter Book ID   : ");

        Member member = findMember(memberId);
        Book   book   = findBook(bookId);

        if (member == null) { System.out.println("Member not found."); return; }
        if (book   == null) { System.out.println("Book not found.");   return; }

        member.borrowBook(book);
    }

    private static void returnBook() {
        String memberId = readString("Enter Member ID : ");
        String bookId   = readString("Enter Book ID   : ");

        Member member = findMember(memberId);
        Book   book   = findBook(bookId);

        if (member == null) { System.out.println("Member not found."); return; }
        if (book   == null) { System.out.println("Book not found.");   return; }

        member.returnBook(book);
    }

    // ─── SEARCH MENU ──────────────────────────────────────

    private static void searchMenu() {
        System.out.println("\n──── SEARCH BOOKS ────");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by ISBN");
        System.out.println("0. Back");

        int choice = readInt("Enter choice: ");
        switch (choice) {
            case 1 -> {
                String title = readString("Enter title: ");
                List<Book> results = library.searchByTitle(title);
                printBooks(results);
            }
            case 2 -> {
                String author = readString("Enter author: ");
                List<Book> results = library.searchByAuthor(author);
                printBooks(results);
            }
            case 3 -> {
                String isbn = readString("Enter ISBN: ");
                Book result = library.searchByISBN(isbn);
                System.out.println(result != null ? result : "No book found.");
            }
            case 0 -> { }
            default -> System.out.println("Invalid choice.");
        }
    }

    // ─── HELPERS ──────────────────────────────────────────

    private static void printBooks(List<Book> books) {
        if (books.isEmpty()) { System.out.println("No books found."); return; }
        books.forEach(System.out::println);
    }

    private static void printAllMembers() {
        List<Member> members = library.getAllMembers();
        if (members.isEmpty()) { System.out.println("No members registered."); return; }
        members.forEach(System.out::println);
    }

    private static Member findMember(String id) {
        return library.getAllMembers().stream()
                .filter(m -> m.getMemberId().equals(id))
                .findFirst().orElse(null);
    }

    private static Book findBook(String id) {
        return library.getAllBooks().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst().orElse(null);
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int val;
                val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}