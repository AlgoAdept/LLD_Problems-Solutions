package com.library;

public class Main {
    public static void main(String[] args) {
        Library library = new Library("City Library");

        // Add books
        Book b1 = new PhysicalBook("B001", "Clean Code", "978-0132350884", "Robert Martin", 431, "A1");
        Book b2 = new Ebook("B002", "Effective Java", "978-0134685991", "Joshua Bloch", "PDF", 5.2);
        Book b3 = new AudioBook("B003", "The Pragmatic Programmer", "978-0135957059", "Dave Thomas", "John Smith", 10.5);

        library.addBook(b1);
        library.addBook(b2);
        library.addBook(b3);

        // Register members
        Member alice = new Member("M001", "Alice", "alice@email.com");
        Faculty prof = new Faculty("F001", "Prof. Bob", "bob@uni.edu");

        library.registerMember(alice);
        library.registerMember(prof);

        // Borrow books
        alice.borrowBook(b1);
        prof.borrowBook(b1); // already borrowed
        prof.borrowBook(b2);

        // Search
        System.out.println("\nSearch by author 'Robert':");
        library.searchByAuthor("Robert").forEach(System.out::println);

        System.out.println("\nSearch by title 'Effective Java':");
        library.searchByTitle("Effective Java").forEach(System.out::println);

        // Print all books
        library.printAllBooks();

        // Return a book
        alice.returnBook(b1);
        prof.borrowBook(b1); // now available

        System.out.println("\n" + alice);
        System.out.println(prof);
    }
}