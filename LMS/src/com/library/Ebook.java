package com.library;

public class Ebook extends Book {
    private final String fileFormat; // PDF, EPUB, etc.
    private final double fileSizeMB;

    public Ebook(String id, String title, String isbn, String author,
                 String fileFormat, double fileSizeMB) {
        super(id, title, isbn, author);
        this.fileFormat = fileFormat;
        this.fileSizeMB = fileSizeMB;
    }

    public String getFileFormat() { return fileFormat; }
    public double getFileSizeMB() { return fileSizeMB; }

    @Override
    public String getBookType() { return "E-Book"; }
}