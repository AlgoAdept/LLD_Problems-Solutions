package com.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Member implements Borrowable {
    private final String memberId;
    private final String name;
    private final String email;
    private final List<Book> borrowedBooks;

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getMemberId() { return memberId; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }

    public List<Book> getBorrowedBooks() {
        return Collections.unmodifiableList(borrowedBooks);
    }

    @Override
    public int getMaxBorrowLimit() { return 3; }

    @Override
    public void borrowBook(Book book) {
        if (!book.isAvailable()) {
            System.out.println("Book is not available: " + book.getTitle());
            return;
        }
        if (borrowedBooks.size() >= getMaxBorrowLimit()) {
            System.out.println(name + " has reached the borrow limit of " + getMaxBorrowLimit());
            return;
        }
        borrowedBooks.add(book);
        book.setStatus(BookStatus.BORROWED);
        System.out.println(name + " borrowed: " + book.getTitle());
    }

    @Override
    public void returnBook(Book book) {
        if (borrowedBooks.remove(book)) {
            book.setStatus(BookStatus.AVAILABLE);
            System.out.println(name + " returned: " + book.getTitle());
            return;
        }
        System.out.println(name + " does not have this book: " + book.getTitle());
    }

    @Override
    public String toString() {
        return String.format("Member[%s] %s | Borrowed: %d/%d",
                memberId, name, borrowedBooks.size(), getMaxBorrowLimit());
    }
}