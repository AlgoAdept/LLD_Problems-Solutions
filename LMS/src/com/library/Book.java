package com.library;

public abstract class Book {
    private final String id;
    private final String title;
    private final String isbn;
    private final String author;
    private BookStatus status;

    public Book(String id, String title, String isbn, String author) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    // Getters
    public String getId()       { return id; }
    public String getTitle()    { return title; }
    public String getIsbn()     { return isbn; }
    public String getAuthor()   { return author; }
    public BookStatus getStatus() { return status; }

    // Setter (only status can change)
    public void setStatus(BookStatus status) { this.status = status; }

    public boolean isAvailable() { return status == BookStatus.AVAILABLE; }

    public abstract String getBookType();

    @Override
    public String toString() {
        return String.format("[%s] %s by %s | ISBN: %s | Status: %s",
                getBookType(), title, author, isbn, status);
    }
}