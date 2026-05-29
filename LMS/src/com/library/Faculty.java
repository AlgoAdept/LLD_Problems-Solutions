package com.library;

public class Faculty extends Member {

    public Faculty(String memberId, String name, String email) {
        super(memberId, name, email);
    }


    @Override
    public int getMaxBorrowLimit() { return 10; } // Faculty can borrow more

    @Override
    public String toString() {
        return String.format("Faculty[%s] %s | Borrowed: %d/%d",
                getMemberId(), getName(),
                getBorrowedBooks().size(), getMaxBorrowLimit());
    }
}