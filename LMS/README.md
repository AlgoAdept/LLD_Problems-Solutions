# Library Management System
## Detailed Requirements Analysis & Implementation Document

**Project Purpose:** A comprehensive learning project demonstrating OOP concepts and SOLID design principles through a practical library management system.

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Functional Requirements](#functional-requirements)
3. [Technical Architecture](#technical-architecture)
4. [OOP Concepts Analysis](#oop-concepts-analysis)
5. [SOLID Principles Analysis](#solid-principles-analysis)
6. [Class Hierarchy & Design](#class-hierarchy--design)
7. [Design Patterns Implemented](#design-patterns-implemented)

---

## Project Overview

### System Description
The Library Management System is a Java-based application designed to manage:
- Multiple types of books (Physical Books, E-books, AudioBooks)
- Library members with different roles (Regular Members, Faculty)
- Book borrowing and returning operations
- Book search and management functionalities

### Core Components
- **7 Classes**: Book (abstract), PhysicalBook, Ebook, AudioBook, Member, Faculty, Library
- **2 Interfaces**: Borrowable, Searchable
- **1 Enum**: BookStatus
- **2 Application Classes**: LibraryApp (CLI), Main (demonstration)

---

## Functional Requirements

### 1. Book Management
- **Add Books**: System supports adding three types of books (Physical, E-book, AudioBook)
- **Remove Books**: Delete books from library by book ID
- **View Books**: Display all books in the library
- **Book Information**: Track book metadata (ID, Title, ISBN, Author, Status)

### 2. Member Management
- **Register Members**: Add new members (students/regular members) with ID, name, and email
- **Register Faculty**: Add faculty members with higher borrowing limits
- **View Members**: Display all registered members with borrowing details

### 3. Borrowing Operations
- **Borrow Books**: Members can borrow available books with constraints
- **Return Books**: Members return borrowed books, making them available again
- **Borrow Limits**: Regular members: 3 books, Faculty: 10 books
- **Availability Check**: System prevents borrowing unavailable books

### 4. Search Functionality
- **Search by Title**: Find books by partial/full title match (case-insensitive)
- **Search by Author**: Find books by partial/full author match (case-insensitive)
- **Search by ISBN**: Find exact book by ISBN number

### 5. Status Management
- **Book Status Tracking**: AVAILABLE, BORROWED, RESERVED states
- **Real-time Updates**: Status changes when books are borrowed/returned

---

## Technical Architecture

### Package Structure
```
com.library
├── Book (abstract class)
├── PhysicalBook (concrete class)
├── Ebook (concrete class)
├── AudioBook (concrete class)
├── Member (concrete class)
├── Faculty (concrete class)
├── Library (concrete class)
├── Borrowable (interface)
├── Searchable (interface)
├── BookStatus (enum)
├── LibraryApp (CLI application)
└── Main (demo application)
```

### Dependencies & Relationships
```
Book (abstract)
  ├── PhysicalBook (inherits from Book)
  ├── Ebook (inherits from Book)
  └── AudioBook (inherits from Book)

Member (implements Borrowable)
  └── Faculty (inherits from Member, overrides getMaxBorrowLimit())

Library (implements Searchable)
  └── Manages Books and Members

Associations:
  - Library has-many Books
  - Library has-many Members
  - Member has-many Books (borrowed)
```

---

## OOP Concepts Analysis

### 1. **Abstraction**

#### Concept Definition
Abstraction is the process of hiding complex implementation details and exposing only essential features to the outside world.

#### Implementation in Project

**A. Abstract Classes**
- **Book (Abstract Class)**
  ```java
  public abstract class Book {
      private final String id;
      private final String title;
      private final String isbn;
      private final String author;
      private BookStatus status;
      
      public abstract String getBookType();  // Forcing subclasses to implement
  }
  ```
  **Why?** The `Book` class is abstract because:
  - Different book types (Physical, E-book, AudioBook) have fundamentally different characteristics
  - We don't want to instantiate a generic "Book" without a specific type
  - It enforces that all books must implement `getBookType()` method
  - Common attributes (id, title, isbn, author) are abstracted at the parent level
  - Specific attributes are handled by subclasses

**B. Interfaces**
- **Borrowable Interface**
  ```java
  public interface Borrowable {
      void borrowBook(Book book);
      void returnBook(Book book);
      int getMaxBorrowLimit();
  }
  ```
  **Why?** 
  - Abstracts the concept of "borrowing capability"
  - Only entities that can borrow books implement this interface
  - Hides borrowing logic implementation from other classes
  - Currently implemented by `Member`, but could be extended to other entities

- **Searchable Interface**
  ```java
  public interface Searchable {
      List<Book> searchByTitle(String title);
      List<Book> searchByAuthor(String author);
      Book searchByISBN(String isbn);
  }
  ```
  **Why?**
  - Abstracts search capability from Library
  - Library could be replaced with other searchable entities
  - Clients depend on the search contract, not implementation details
  - Enables future extension with different search strategies

**C. Enum**
- **BookStatus Enum**
  ```java
  public enum BookStatus {
      AVAILABLE, BORROWED, RESERVED
  }
  ```
  **Why?**
  - Abstracts book status into discrete, predefined values
  - Prevents invalid status assignments
  - Type-safe alternative to string-based status tracking

#### Benefits
✅ Reduces complexity by hiding implementation details
✅ Provides clear contracts through interfaces
✅ Prevents instantiation of incomplete objects (abstract Book class)
✅ Ensures type safety (BookStatus enum)

---

### 2. **Encapsulation**

#### Concept Definition
Encapsulation is the bundling of data (attributes) and methods that operate on that data into a single unit (class), while hiding internal details from the outside world.

#### Implementation in Project

**A. Private Fields**
```java
public abstract class Book {
    private final String id;           // Hidden from external modification
    private final String title;        // Hidden from external modification
    private final String isbn;         // Hidden from external modification
    private final String author;       // Hidden from external modification
    private BookStatus status;         // Hidden but changeable via setStatus()
}
```
**Why?**
- Book attributes cannot be directly modified from outside
- Immutable attributes (id, title, isbn, author) ensure data integrity
- Only status can change via controlled `setStatus()` method

**B. Controlled Access through Getters/Setters**
```java
public class Member implements Borrowable {
    private final List<Book> borrowedBooks;  // Private collection
    
    public List<Book> getBorrowedBooks() {
        return Collections.unmodifiableList(borrowedBooks);  // Return unmodifiable copy
    }
}
```
**Why?**
- Returns an unmodifiable list to prevent external tampering
- Internal borrowedBooks list cannot be directly modified
- Maintains data consistency and integrity

**C. Immutability**
```java
public class PhysicalBook extends Book {
    private final int totalPages;          // Final - cannot change after initialization
    private final String shelfLocation;    // Final - cannot change after initialization
}
```
**Why?**
- Final fields ensure object state cannot be changed after creation
- Prevents accidental data corruption
- Thread-safe by design

**D. Controlled Behavior through Methods**
```java
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
```
**Why?**
- Borrowing logic is encapsulated within the `Member` class
- Validation happens before state changes
- Book status is updated consistently with borrowing action

#### Benefits
✅ Data integrity: Prevents unauthorized access and modification
✅ Maintainability: Internal implementation can change without affecting clients
✅ Validation: Can enforce business rules when data is accessed/modified
✅ Thread-safety: Final fields and controlled access prevent race conditions

---

### 3. **Inheritance**

#### Concept Definition
Inheritance is a mechanism where a new class (subclass) inherits attributes and methods from an existing class (superclass).

#### Implementation in Project

**A. Single Inheritance - Book Hierarchy**
```
Book (Superclass - Abstract)
├── PhysicalBook (Subclass)
├── Ebook (Subclass)
└── AudioBook (Subclass)
```

**PhysicalBook Implementation**
```java
public class PhysicalBook extends Book {
    private final int totalPages;
    private final String shelfLocation;

    public PhysicalBook(String id, String title, String isbn, String author,
                        int totalPages, String shelfLocation) {
        super(id, title, isbn, author);  // Call parent constructor
        this.totalPages = totalPages;
        this.shelfLocation = shelfLocation;
    }

    @Override
    public String getBookType() { return "Physical Book"; }
}
```
**Why?**
- PhysicalBook reuses Book's common attributes (id, title, isbn, author, status)
- Adds physical-specific attributes (totalPages, shelfLocation)
- Implements abstract method `getBookType()` to return its specific type
- Eliminates code duplication

**Ebook Implementation**
```java
public class Ebook extends Book {
    private final String fileFormat;    // PDF, EPUB, etc.
    private final double fileSizeMB;

    @Override
    public String getBookType() { return "E-Book"; }
}
```
**Why?**
- Reuses Book base functionality
- Adds digital-specific attributes (fileFormat, fileSizeMB)
- Different implementation of getBookType()

**AudioBook Implementation**
```java
public class AudioBook extends Book {
    private final String narrator;
    private final double durationHours;

    @Override
    public String getBookType() { return "AudioBook"; }
}
```
**Why?**
- Reuses Book base functionality
- Adds audio-specific attributes (narrator, durationHours)
- Different implementation of getBookType()

**B. Single Inheritance - Member Hierarchy**
```
Member (Superclass)
└── Faculty (Subclass)
```

**Faculty Implementation**
```java
public class Faculty extends Member {
    @Override
    public int getMaxBorrowLimit() { return 10; }  // Faculty can borrow more
}
```
**Why?**
- Faculty inherits all Member functionality (borrowing, member info)
- Overrides `getMaxBorrowLimit()` to provide different limit (10 vs 3)
- Demonstrates behavioral specialization through inheritance

#### Inheritance Benefits
✅ **Code Reuse**: Common functionality (Book attributes) defined once in parent class
✅ **Polymorphism**: Different book types can be treated uniformly as `Book`
✅ **Extensibility**: New book types can be added by extending Book class
✅ **Behavioral Specialization**: Faculty overrides borrow limit behavior while keeping other Member functionality

#### Example: Polymorphism in Action
```java
// Main.java demonstration
List<Book> books = new ArrayList<>();
books.add(new PhysicalBook(...));  // PhysicalBook is-a Book
books.add(new Ebook(...));         // Ebook is-a Book
books.add(new AudioBook(...));     // AudioBook is-a Book

// Polymorphic call - each book returns its specific type
books.forEach(book -> System.out.println(book.getBookType()));
```
Output:
```
Physical Book
E-Book
AudioBook
```

---

### 4. **Polymorphism**

#### Concept Definition
Polymorphism (many forms) allows objects to take multiple forms and enables using objects of different types through the same interface.

#### Implementation in Project

**A. Method Overriding (Runtime Polymorphism)**

**Compile-time reference, Runtime execution:**
```java
// Reference to Book (parent type)
Book book = new PhysicalBook("B001", "Clean Code", "978-0132350884", "Robert Martin", 431, "A1");
System.out.println(book.getBookType());  // Executes PhysicalBook's implementation
// Output: Physical Book

book = new Ebook("B002", "Effective Java", "978-0134685991", "Joshua Bloch", "PDF", 5.2);
System.out.println(book.getBookType());  // Executes Ebook's implementation
// Output: E-Book
```

**Why Runtime Polymorphism?**
- Actual method executed depends on runtime object type, not reference type
- Library can work with generic `Book` references without knowing concrete type
- New book types can be added without changing Library code

**B. toString() Override**
```java
// In Book class
@Override
public String toString() {
    return String.format("[%s] %s by %s | ISBN: %s | Status: %s",
            getBookType(), title, author, isbn, status);
}
```
**Polymorphic Behavior:**
- Book uses `getBookType()` in toString(), which is overridden by each subclass
- Output automatically includes the correct book type:
  ```
  [Physical Book] Clean Code by Robert Martin | ISBN: 978-0132350884 | Status: AVAILABLE
  [E-Book] Effective Java by Joshua Bloch | ISBN: 978-0134685991 | Status: AVAILABLE
  [AudioBook] The Pragmatic Programmer by Dave Thomas | ISBN: 978-0135957059 | Status: AVAILABLE
  ```

**C. Interface-Based Polymorphism**
```java
public class Library implements Searchable {
    @Override
    public List<Book> searchByTitle(String title) { ... }
    
    @Override
    public List<Book> searchByAuthor(String author) { ... }
    
    @Override
    public Book searchByISBN(String isbn) { ... }
}

// Client code
Searchable searcher = new Library("City Library");
List<Book> results = searcher.searchByTitle("Effective Java");
// Can be replaced with another Searchable implementation without client code change
```

**D. Member Polymorphism**
```java
public class Main {
    Member alice = new Member("M001", "Alice", "alice@email.com");
    Faculty prof = new Faculty("F001", "Prof. Bob", "bob@uni.edu");
    
    // Polymorphic call
    alice.borrowBook(b1);   // Uses Member's implementation, limit = 3
    prof.borrowBook(b1);    // Uses Faculty's overridden method, limit = 10
}
```

#### Polymorphism Benefits
✅ **Code Reusability**: Library works with any Book type without specific handling
✅ **Flexibility**: New book types automatically work with existing code
✅ **Maintainability**: Changes to subclass implementations don't affect client code
✅ **Substitutability**: Faculty can be used anywhere Member is expected
✅ **Extensibility**: New implementations can be added following the contract

---

### 5. **Interface-Based Design**

#### Concept Definition
Interfaces define contracts (method signatures) that implementing classes must follow, without dictating how they implement those methods.

#### Implementation in Project

**A. Borrowable Interface**
```java
public interface Borrowable {
    void borrowBook(Book book);
    void returnBook(Book book);
    int getMaxBorrowLimit();
}
```
**Member's Implementation:**
```java
public class Member implements Borrowable {
    @Override
    public void borrowBook(Book book) {
        // Validation: Check availability and limit
        // Update state: Add to borrowedBooks, change status
    }
    
    @Override
    public void returnBook(Book book) {
        // Update state: Remove from borrowedBooks, change status
    }
    
    @Override
    public int getMaxBorrowLimit() { return 3; }
}
```

**Why Borrowable Interface?**
- Defines what it means to be "borrowable"
- Member doesn't have to extend a borrowing class; it implements the behavior
- Future entities (Staff, Guest) could implement Borrowable without inheriting Member
- Separates interface from inheritance hierarchy

**B. Searchable Interface**
```java
public interface Searchable {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    Book searchByISBN(String isbn);
}
```
**Library's Implementation:**
```java
public class Library implements Searchable {
    @Override
    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase()))
                results.add(b);
        }
        return results;
    }
    // ... other search methods
}
```

**Why Searchable Interface?**
- Abstracts search capability from Library's core responsibility
- Could have multiple implementations (LinearSearch, DatabaseSearch, etc.)
- Clients depend on Searchable interface, not Library directly
- Enables mock implementations for testing

#### Interface Benefits
✅ **Loose Coupling**: Clients depend on interfaces, not concrete implementations
✅ **Multiple Inheritance**: Classes can implement multiple interfaces (Java doesn't support multiple class inheritance)
✅ **Flexibility**: Implementation can change without affecting clients
✅ **Testability**: Easy to create mock implementations for testing

---

## SOLID Principles Analysis

### 1. **S - Single Responsibility Principle (SRP)**

#### Definition
A class should have only one reason to change (one responsibility/job).

#### Implementation in Project

**A. Book Class - Responsibility: Represent a Book**
```java
public abstract class Book {
    private final String id;
    private final String title;
    private final String isbn;
    private final String author;
    private BookStatus status;
    
    // Only book-related data and behavior
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return status == BookStatus.AVAILABLE; }
    public abstract String getBookType();
}
```
**Single Responsibility:** Manage book data and status
**Reason to Change:** When book attributes or behavior changes

**B. Member Class - Responsibility: Represent a Member and Handle Borrowing**
```java
public class Member implements Borrowable {
    private final String memberId;
    private final String name;
    private final String email;
    private final List<Book> borrowedBooks;
    
    public void borrowBook(Book book) { ... }
    public void returnBook(Book book) { ... }
    public int getMaxBorrowLimit() { return 3; }
}
```
**Single Responsibility:** 
- Manage member information (id, name, email)
- Handle member's borrowing operations
**Reason to Change:** When member data structure or borrowing logic changes

**C. Library Class - Responsibility: Manage Library Operations**
```java
public class Library implements Searchable {
    private final String name;
    private final List<Book> books;
    private final List<Member> members;
    
    public void addBook(Book book) { ... }
    public void removeBook(String bookId) { ... }
    public List<Book> searchByTitle(String title) { ... }
    public void registerMember(Member member) { ... }
}
```
**Single Responsibility:** 
- Maintain books collection
- Maintain members collection
- Provide search functionality
**Reason to Change:** When library operations or search logic changes

**D. Separate Classes for Different Book Types**
```java
public class PhysicalBook extends Book {
    private final int totalPages;
    private final String shelfLocation;
}

public class Ebook extends Book {
    private final String fileFormat;
    private final double fileSizeMB;
}

public class AudioBook extends Book {
    private final String narrator;
    private final double durationHours;
}
```
**Single Responsibility:** Each book type manages its specific attributes
**Reason to Change:** When specific book type requirements change

**E. Faculty Class - Responsibility: Override Borrow Limit**
```java
public class Faculty extends Member {
    @Override
    public int getMaxBorrowLimit() { return 10; }
}
```
**Single Responsibility:** Faculty-specific borrow behavior
**Reason to Change:** When faculty borrowing rules change

#### SRP Benefits
✅ Each class has clear purpose and responsibility
✅ Changes to one responsibility don't affect others
✅ Classes are easier to test and maintain
✅ Code is more modular and reusable

---

### 2. **O - Open/Closed Principle (OCP)**

#### Definition
Software entities should be **open for extension** (can add new functionality) but **closed for modification** (don't change existing code).

#### Implementation in Project

**A. Abstract Book Class - Foundation for Extension**
```java
public abstract class Book {
    // Existing implementation - CLOSED for modification
    
    public abstract String getBookType();  // Extension point
}
```
**Open for Extension:**
```java
// Add new book types without modifying Book class
public class Magazine extends Book {
    private final String issueNumber;
    @Override
    public String getBookType() { return "Magazine"; }
}

public class Journal extends Book {
    private final String doi;
    @Override
    public String getBookType() { return "Journal"; }
}
```
**Closed for Modification:** The Book class itself doesn't change

**Real Example in Code:**
```java
// Main.java works with new book types without modification
Book b1 = new PhysicalBook(...);
Book b2 = new Ebook(...);
Book b3 = new AudioBook(...);  // Can add new types later
// Magazine, Journal, etc. would work automatically
```

**B. Member Inheritance - Extension Without Modification**
```java
public class Member implements Borrowable {
    // Base implementation - CLOSED for modification
}
```
**Open for Extension:**
```java
public class Faculty extends Member {
    @Override
    public int getMaxBorrowLimit() { return 10; }
}

// Can add more member types
public class Student extends Member {
    @Override
    public int getMaxBorrowLimit() { return 3; }
}

public class Staff extends Member {
    @Override
    public int getMaxBorrowLimit() { return 5; }
}
```

**C. Searchable Interface - Multiple Implementations Without Changing Library**
```java
public interface Searchable {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    Book searchByISBN(String isbn);
}

// Original implementation
public class Library implements Searchable {
    @Override
    public List<Book> searchByTitle(String title) { ... }
}
```
**Open for Extension:**
```java
// New implementation without changing original Library
public class DatabaseLibrary implements Searchable {
    @Override
    public List<Book> searchByTitle(String title) {
        // Database query implementation
    }
}

public class CachedLibrary implements Searchable {
    @Override
    public List<Book> searchByTitle(String title) {
        // Check cache first, then delegate
    }
}
```

#### OCP Benefits
✅ New features can be added without modifying existing code
✅ Reduces risk of breaking existing functionality
✅ Code is more maintainable and stable
✅ Follows extensibility principle through abstraction and inheritance

**Counter-Example (Bad Design):**
```java
// ❌ NOT Open/Closed - Modification required for each book type
public String getBookInfo(Book book) {
    if (book instanceof PhysicalBook) { ... }
    else if (book instanceof Ebook) { ... }
    else if (book instanceof AudioBook) { ... }
    // Must modify method when adding new book type!
}
```

**Good Design (Our Implementation):**
```java
// ✅ Open/Closed - Works with all book types
book.getBookType();  // Polymorphic call, no modification needed
```

---

### 3. **L - Liskov Substitution Principle (LSP)**

#### Definition
Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

#### Implementation in Project

**A. PhysicalBook, Ebook, AudioBook - Substitutable for Book**
```java
// LSP: All book types can be used wherever Book is expected
List<Book> books = new ArrayList<>();
books.add(new PhysicalBook("B001", "Clean Code", "978-0132350884", "Robert Martin", 431, "A1"));
books.add(new Ebook("B002", "Effective Java", "978-0134685991", "Joshua Bloch", "PDF", 5.2));
books.add(new AudioBook("B003", "The Pragmatic Programmer", "978-0135957059", "Dave Thomas", "John Smith", 10.5));

// Process all books uniformly
for (Book book : books) {
    System.out.println(book.toString());  // Works for all types
    System.out.println(book.getBookType()); // Works for all types
    if (book.isAvailable()) {  // Works for all types
        // borrow logic
    }
}
```
**LSP Compliance:**
- All book types behave as expected for Book contract
- No special type checking required
- Can swap implementations without affecting logic

**B. Faculty - Substitutable for Member**
```java
// LSP: Faculty can be used wherever Member is expected
Member member1 = new Member("M001", "Alice", "alice@email.com");
Member member2 = new Faculty("F001", "Prof. Bob", "bob@uni.edu");

// Both work with borrowBook method
Book book = new PhysicalBook(...);
member1.borrowBook(book);  // Alice can borrow 3 books
member2.borrowBook(book);  // Faculty can borrow 10 books
```
**LSP Compliance:**
- Faculty respects Member contract (implements Borrowable)
- Faculty doesn't violate Member expectations
- Faculty behavior is a valid specialization of Member

**C. No Violation of Expected Behavior**
```java
public class Faculty extends Member {
    @Override
    public int getMaxBorrowLimit() { return 10; }  // ✅ Respects contract
    
    // NOT doing this (LSP violation):
    // @Override
    // public void borrowBook(Book book) {
    //     throw new UnsupportedOperationException();  // ❌ Breaks contract!
    // }
}
```

**D. Searchable Interface Implementation**
```java
public class Library implements Searchable {
    @Override
    public List<Book> searchByTitle(String title) {
        // Returns results that match title
    }
    
    @Override
    public Book searchByISBN(String isbn) {
        // Returns single book or null
    }
}

// LSP: Can replace Library with another Searchable
Searchable searcher = new Library("City Library");
// Works as expected for all Searchable operations
```

#### LSP Benefits
✅ Substitutability ensures inheritance is meaningful
✅ No surprises when using subclass instead of superclass
✅ Prevents type-checking code (instanceof checks)
✅ Code is more maintainable and less error-prone

**Counter-Example (LSP Violation):**
```java
// ❌ BAD: Faculty violates Member contract
public class Faculty extends Member {
    @Override
    public void borrowBook(Book book) {
        throw new UnsupportedOperationException("Faculty cannot borrow!");
    }
}

// Now this code breaks:
Member member = new Faculty(...);  // Expected to work
member.borrowBook(book);  // ❌ Throws exception - LSP violated!
```

---

### 4. **I - Interface Segregation Principle (ISP)**

#### Definition
Clients should not be forced to depend on interfaces they do not use. Create specific, small interfaces rather than large, bloated ones.

#### Implementation in Project

**A. Borrowable Interface - Specific to Borrowing**
```java
public interface Borrowable {
    void borrowBook(Book book);
    void returnBook(Book book);
    int getMaxBorrowLimit();
}
```
**ISP Compliance:**
- Interface only contains borrowing-related methods
- Only classes that can borrow implement this interface
- Members don't implement Searchable (don't have to)
- Searchable implementations don't implement Borrowable (don't have to)

**B. Searchable Interface - Specific to Searching**
```java
public interface Searchable {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    Book searchByISBN(String isbn);
}
```
**ISP Compliance:**
- Interface only contains search-related methods
- Library implements Searchable, not Borrowable
- Clean separation of concerns

**C. Client Dependencies are Minimal**
```java
// Client code depends only on needed interface
Searchable searcher = new Library("City Library");
List<Book> results = searcher.searchByTitle("Effective Java");
// Client doesn't care about Member, borrowing, etc.

// Another client
Borrowable borrower = new Member("M001", "Alice", "alice@email.com");
borrower.borrowBook(book);
// Client doesn't care about searching, only borrowing
```

#### ISP Benefits
✅ Interfaces are focused and cohesive
✅ Classes implement only needed methods
✅ No forced dependency on unused functionality
✅ Easier to test and mock interfaces

**Counter-Example (ISP Violation):**
```java
// ❌ BAD: Large, bloated interface
public interface LibraryEntity {
    void borrowBook(Book book);
    void returnBook(Book book);
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    Book searchByISBN(String isbn);
    void registerMember(Member member);
    void addBook(Book book);
    // ... many more methods
}

// Now Member must implement all methods it doesn't use:
public class Member implements LibraryEntity {
    @Override
    public void searchByTitle(String title) { }  // Forced, not needed!
    @Override
    public void addBook(Book book) { }  // Forced, not needed!
}
```

**Our Design (ISP Compliant):**
```java
// ✅ GOOD: Focused interfaces
public interface Borrowable { /* borrowing only */ }
public interface Searchable { /* searching only */ }

public class Member implements Borrowable { }
public class Library implements Searchable { }
```

---

### 5. **D - Dependency Inversion Principle (DIP)**

#### Definition
1. High-level modules should not depend on low-level modules; both should depend on abstractions.
2. Abstractions should not depend on details; details should depend on abstractions.

#### Implementation in Project

**A. Abstractions as Foundation**
```
High-level: Library
            Member
            
Abstractions: Searchable (interface)
              Borrowable (interface)
              Book (abstract class)
              
Low-level: PhysicalBook
           Ebook
           AudioBook
           Faculty
```

**A1. Library Depends on Book Abstraction**
```java
public class Library implements Searchable {
    private final List<Book> books;  // Depends on Book abstraction, not concrete types
    
    public void addBook(Book book) {  // Parameter: Book abstraction
        books.add(book);
    }
    
    public List<Book> getAllBooks() {  // Returns Book abstraction
        return Collections.unmodifiableList(books);
    }
    
    // Search implementation works with any Book
    @Override
    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {  // Uses Book abstraction
            if (b.getTitle().toLowerCase().contains(title.toLowerCase()))
                results.add(b);
        }
        return results;
    }
}
```
**DIP Compliance:**
- Library doesn't know about PhysicalBook, Ebook, AudioBook
- Library only depends on Book abstraction
- Can add new book types without changing Library

**DIP Violation Example (Bad Design):**
```java
// ❌ HIGH-LEVEL MODULE DEPENDS ON LOW-LEVEL MODULES
public class Library {
    private List<PhysicalBook> physicalBooks;
    private List<Ebook> ebooks;
    private List<AudioBook> audiobooks;
    
    public void addPhysicalBook(PhysicalBook book) { ... }
    public void addEbook(Ebook book) { ... }
    public void addAudioBook(AudioBook book) { ... }
    
    // Must update Library for every new book type!
}
```

**B. Member Uses Book Abstraction**
```java
public class Member implements Borrowable {
    private final List<Book> borrowedBooks;  // Depends on Book abstraction
    
    @Override
    public void borrowBook(Book book) {  // Parameter: Book abstraction
        borrowedBooks.add(book);
        book.setStatus(BookStatus.BORROWED);
    }
    
    @Override
    public void returnBook(Book book) {  // Parameter: Book abstraction
        borrowedBooks.remove(book);
        book.setStatus(BookStatus.AVAILABLE);
    }
}
```
**DIP Compliance:**
- Member doesn't know concrete book types
- Works with any Book implementation
- Borrowing logic is independent of book type

**C. Clients Depend on Interfaces, Not Concrete Classes**
```java
public class LibraryApp {
    private static final Library library = new Library("City Library");
    
    // Client code depends on Searchable interface
    private static void searchMenu() {
        Searchable searcher = library;  // Reference abstraction
        List<Book> results = searcher.searchByTitle("Effective Java");
    }
    
    // Client code depends on Borrowable interface
    private static void borrowMenu() {
        Borrowable borrower = new Member("M001", "Alice", "alice@email.com");
        borrower.borrowBook(book);
    }
}
```
**DIP Compliance:**
- Client code doesn't create Library directly (in this simple app)
- References use abstractions (Searchable, Borrowable)
- Could be extended with dependency injection

#### DIP Implementation Benefits
✅ High-level modules are stable and independent
✅ Easy to add new implementations without changing high-level code
✅ Loose coupling between modules
✅ Easy to test with mock implementations
✅ Promotes better code organization

**DIP Applied Example in Testing (Not in Current Code, but Possible):**
```java
// Test with Mock Searchable
class MockLibrary implements Searchable {
    @Override
    public List<Book> searchByTitle(String title) {
        return Arrays.asList(new PhysicalBook(...));
    }
}

// Client doesn't care if it's real Library or MockLibrary
Searchable searcher = new MockLibrary();
List<Book> results = searcher.searchByTitle("Test");
```

---

## Class Hierarchy & Design

### Class Diagram
```
┌─────────────────────────────────────────────────────────────────┐
│                       INHERITANCE HIERARCHY                      │
└─────────────────────────────────────────────────────────────────┘

                    ┌──────────────┐
                    │ Book (Abstract)
                    │ - id: String│
                    │ - title: String
                    │ - isbn: String
                    │ - author: String
                    │ - status: BookStatus
                    │ # getBookType(): String (abstract)
                    └──────────────┘
                         ▲
            ┌────────────┼────────────┐
            │            │            │
    ┌───────────────┐ ┌────────────┐ ┌─────────────────┐
    │PhysicalBook   │ │   Ebook    │ │   AudioBook    │
    │- totalPages   │ │- fileFormat│ │- narrator      │
    │- shelfLocation│ │- fileSizeMB│ │- durationHours │
    └───────────────┘ └────────────┘ └─────────────────┘


                    ┌──────────────┐
                    │   Member     │
                    │- memberId    │
                    │- name        │
                    │- email       │
                    │- borrowedBooks
                    └──────────────┘
                         ▲
                         │
                    ┌─────────────┐
                    │   Faculty   │
                    │(overrides   │
                    │borrow limit)│
                    └─────────────┘
```

### Interface Implementation Diagram
```
┌─────────────────────────────────────────────────────────────────┐
│                       INTERFACE IMPLEMENTATION                    │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────┐              ┌─────────────────────┐
│   Borrowable     │              │    Searchable      │
│ - borrowBook()   │              │ - searchByTitle()  │
│ - returnBook()   │              │ - searchByAuthor() │
│ - getMaxBorrow() │              │ - searchByISBN()   │
└──────────────────┘              └─────────────────────┘
        ▲                                   ▲
        │                                   │
        │ implements                        │ implements
        │                                   │
    ┌───────────────┐                 ┌────────────┐
    │    Member     │                 │  Library   │
    │    Faculty    │                 └────────────┘
    └───────────────┘

┌──────────────────┐
│   BookStatus     │
│   (enum)         │
│ - AVAILABLE      │
│ - BORROWED       │
│ - RESERVED       │
└──────────────────┘
```

### Composition Relationships
```
Library COMPOSITION
├── has-many → List<Book>
│              └── PhysicalBook
│              └── Ebook
│              └── AudioBook
│
└── has-many → List<Member>
               ├── Member
               └── Faculty

Member COMPOSITION
└── has-many → List<Book> (borrowed)
               └── Any Book type
```

---

## Design Patterns Implemented

### 1. **Strategy Pattern** (Implicit - Book Types)
**Concept:** Define family of algorithms (book handling), encapsulate each, and make them interchangeable.

**Implementation:**
```java
// Different book types have different behaviors
Book physicalBook = new PhysicalBook(...);  // Physical book strategy
Book ebook = new Ebook(...);                // E-book strategy
Book audiobook = new AudioBook(...);        // AudioBook strategy

// All can be treated uniformly
books.add(physicalBook);
books.add(ebook);
books.add(audiobook);

// Each implements behavior differently
for (Book book : books) {
    System.out.println(book.getBookType());  // Calls appropriate strategy
}
```

### 2. **Template Method Pattern** (Implicit - Book Abstract Class)
**Concept:** Define skeleton of algorithm in superclass, let subclasses override specific steps.

**Implementation:**
```java
public abstract class Book {
    // Template: Book data structure
    private final String id;
    private final String title;
    private final String isbn;
    private final String author;
    
    // Template method: uses abstract method for variation point
    @Override
    public String toString() {
        return String.format("[%s] %s by %s | ISBN: %s | Status: %s",
                getBookType(), title, author, isbn, status);
    }
    
    // Extension point: each subclass implements differently
    public abstract String getBookType();
}
```

**Template in Action:**
- `toString()` is the template method
- `getBookType()` is the hook/extension point
- Each subclass implements `getBookType()` differently

### 3. **Separation of Concerns Pattern**
**Concept:** Divide system into focused modules, each handling one aspect.

**Implementation:**
- **Book Management:** Book, PhysicalBook, Ebook, AudioBook
- **Member Management:** Member, Faculty
- **Library Operations:** Library
- **Searching:** Searchable interface
- **Borrowing:** Borrowable interface

Each module has clear responsibility.

---

## Summary of Design Benefits

### Code Quality
✅ **Modularity:** Each class has single responsibility
✅ **Reusability:** Book hierarchy, Member hierarchy can be extended
✅ **Maintainability:** Changes isolated to relevant classes
✅ **Testability:** Small, focused classes are easier to test

### Extensibility
✅ Add new book types (Magazine, Journal) without modifying existing code
✅ Add new member types (Student, Staff) without modifying existing code
✅ Add new search implementations without changing Library
✅ Add new borrowing rules without changing Member

### Design Quality
✅ **Loose Coupling:** Depend on abstractions, not concrete classes
✅ **High Cohesion:** Related functionality grouped together
✅ **Clear Contracts:** Interfaces define expected behavior
✅ **Polymorphism:** Different types handled uniformly

### SOLID Principles Compliance
✅ **S:** Each class has single responsibility
✅ **O:** Open for extension (new book types, member types), closed for modification
✅ **L:** Subclasses (PhysicalBook, Faculty) substitutable for superclasses
✅ **I:** Focused interfaces (Borrowable, Searchable)
✅ **D:** Depend on abstractions (Book, Member, interfaces)

---

## Conclusion

This Library Management System is an excellent learning project that demonstrates:

1. **Fundamental OOP Concepts:**
   - Abstraction (abstract classes, interfaces, enums)
   - Encapsulation (private fields, controlled access)
   - Inheritance (Book, Member hierarchies)
   - Polymorphism (method overriding, interface implementation)

2. **SOLID Principles:**
   - Single Responsibility (focused classes)
   - Open/Closed (extensible without modification)
   - Liskov Substitution (proper subclassing)
   - Interface Segregation (focused interfaces)
   - Dependency Inversion (depend on abstractions)

3. **Real-World Design Patterns:**
   - Strategy Pattern (different book types)
   - Template Method Pattern (Book abstraction)
   - Separation of Concerns (modular design)

The architecture provides a solid foundation for extension and maintenance, making it an ideal teaching tool for understanding professional software design principles.
