package com.library;

public interface Borrowable {
    void borrowBook(Book book);
    void returnBook(Book book);
    int getMaxBorrowLimit();
}