package com.library;

public class AudioBook extends Book {
    private final String narrator;
    private final double durationHours;

    public AudioBook(String id, String title, String isbn, String author,
                     String narrator, double durationHours) {
        super(id, title, isbn, author);
        this.narrator = narrator;
        this.durationHours = durationHours;
    }

    public String getNarrator()      { return narrator; }
    public double getDurationHours() { return durationHours; }

    @Override
    public String getBookType() { return "AudioBook"; }
}