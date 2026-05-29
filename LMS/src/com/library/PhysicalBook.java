package com.library;

public class PhysicalBook extends Book {
    private final int totalPages;
    private final String shelfLocation;

    public PhysicalBook(String id, String title, String isbn, String author,
                        int totalPages, String shelfLocation) {
        super(id, title, isbn, author);
        this.totalPages = totalPages;
        this.shelfLocation = shelfLocation;
    }

    public int getTotalPages()       { return totalPages; }
    public String getShelfLocation() { return shelfLocation; }

    @Override
    public String getBookType() { return "Physical Book"; }
}