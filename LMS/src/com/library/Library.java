package com.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Library implements Searchable {
    private final String name;
    private final List<Book> books;
    private final List<Member> members;

    public Library(String name) {
        this.name = name;
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public String getName() { return name; }

    // Book management
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Added to library: " + book.getTitle());
    }

    public boolean removeBook(String bookId) {
        return books.removeIf(b -> b.getId().equals(bookId));
    }

    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(books);
    }

    // Member management
    public void registerMember(Member member) {
        members.add(member);
        System.out.println("Registered member: " + member.getName());
    }

    public List<Member> getAllMembers() {
        return Collections.unmodifiableList(members);
    }

    // Searchable implementation
    @Override
    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase()))
                results.add(b);
        }
        return results;
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                results.add(b);
        }
        return results;
    }

    @Override
    public Book searchByISBN(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) return b;
        }
        return null;
    }

    public void printAllBooks() {
        System.out.println("\n=== " + name + " - All Books ===");
        books.forEach(System.out::println);
    }
}