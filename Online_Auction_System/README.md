# Online Auction System

## Project Overview

This Python project implements a simple console-based online auction system. It models the key domain concepts of an auction platform using object-oriented design and separates responsibilities across classes.

The system supports:
- user registration
- auction creation
- placing bids
- closing auctions
- viewing auction status
- declaring a winner

## Files

- `main.py` - Console menu and user interaction loop
- `AuctionSystem.py` - Auction system manager, stores users and auctions
- `Auction.py` - Auction business logic and bid acceptance
- `AuctionItem.py` - Item details for an auction
- `Bid.py` - Bid representation with bidder and amount
- `User.py` - User representation with name and email
- `requirements.txt` - Project requirements and design notes
- `output.txt` - (Optional) output or sample run data

## Class Descriptions

### `User`

Responsibility:
- Encapsulates user information
- Represents both buyers and sellers

Key members:
- `name`: user name
- `email`: user email

Why this class exists:
- Keeps user data together and reusable across auctions and bids.
- Avoids passing raw name/email strings throughout the system.

### `AuctionItem`

Responsibility:
- Stores metadata about an item being auctioned

Key members:
- `title`: item title
- `base_price`: initial price for the auction
- `description`: item description

Why this class exists:
- Separates item data from auction logic.
- Makes it easy to extend item details later without changing auction behavior.

### `Bid`

Responsibility:
- Represents a bid placed by a user

Key members:
- `bidder`: instance of `User`
- `amount`: bid value

Why this class exists:
- Encapsulates the concept of a single bid.
- Keeps bid data distinct from auction and user responsibilities.

### `Auction`

Responsibility:
- Contains main auction behavior
- Tracks current price, highest bidder, seller, and status

Key members and methods:
- `item`: associated `AuctionItem`
- `starting_price`: auction start price
- `current_price`: current highest bid or starting price
- `highest_bidder`: current winning `User`
- `seller`: `User` who created the auction
- `closed`: auction open/closed state
- `place_bid(bid)`: accepts a bid only if it is higher than current price and auction is open
- `close_auction()`: closes the auction
- `get_winner()`: returns the highest bidder
- `get_winning_bid()`: returns the winning price if a bid exists

Why this class exists:
- Encapsulates auction-specific rules and state.
- Enables clean validation of bids and closure logic.
- Keeps auction behavior isolated from user and system management.

### `AuctionSystem`

Responsibility:
- Manages collections of users and auctions
- Provides higher-level operations such as registering users and creating auctions

Key members and methods:
- `users`: list of `User` objects
- `auctions`: list of `Auction` objects
- `register_user(name, email)`: creates and stores a user
- `create_auction(item_title, base_price, description, seller)`: creates an auction and item
- `close_auction(auction)`: delegates closing to the auction
- `get_active_auctions()`: returns auctions that are still open
- `list_auctions()`: returns all auctions
- `find_user(name)`: finds a registered user by name

Why this class exists:
- Separates system-level orchestration from auction business logic.
- Prevents `main.py` from directly managing auction internals.
- Makes the core application easier to maintain and extend.

## `main.py` Behavior

`main.py` provides a command-line menu for interacting with the auction system.

Main flow:
1. Register users
2. Create auctions by selecting a registered seller
3. Place bids by selecting a bidder and an active auction
4. Close auctions and display the winner
5. List auctions with status and top bidder
6. Exit the application

Helper functions:
- `choose_user(system)`: selects a registered user
- `choose_auction(system)`: selects an active auction
- `print_auction_details(auction)`: displays auction creation data

## OOP Concepts Used

### Encapsulation

Each class keeps its own state and behavior. For example:
- `Auction` controls bidding and closure logic.
- `User` stores only user-specific fields.
- `Bid` wraps the bidder and amount in a single object.

This avoids spreading auction rules and data across unrelated files.

### Abstraction

Classes model high-level auction concepts without exposing raw implementation details.
- `AuctionSystem` offers simple operations like `create_auction` and `register_user`.
- Consumers of the system do not need to know how auctions are stored internally.

### Composition

The system composes objects together:
- `Auction` contains an `AuctionItem` and a `seller` user.
- `Bid` contains a `User` as the bidder.
- `AuctionSystem` contains lists of `User` and `Auction` objects.

This models the real-world relationships cleanly.

### Single Responsibility Principle (SRP)

Each class has one clear responsibility:
- `User` only holds user data.
- `AuctionItem` only holds item data.
- `Bid` only holds bid data.
- `Auction` only manages auction state and bid acceptance.
- `AuctionSystem` only manages registration and auction lifecycle.

SRP improves maintainability and reduces coupling.

## SOLID Principles

### Single Responsibility Principle (SRP)

Clearly applied: each class has a single reason to change.
- If user fields change, only `User.py` is affected.
- If auction rules change, only `Auction.py` is affected.
- If system behavior changes, only `AuctionSystem.py` is affected.

### Open/Closed Principle (OCP)

The design is open for extension and closed for modification in concept.
- New auction types or validation rules can be added by extending `Auction` or adding decorators.
- Existing classes do not need modification for simple extensions.

Example future extension:
- Add `ReverseAuction` or `DutchAuction` subclasses of `Auction`.

### Liskov Substitution Principle (LSP)

The design supports substitution in principle.
- If a subclass like `PremiumUser(User)` is added, it can be used anywhere `User` is expected.
- `Auction` and `Bid` interact with `User` via the shared contract of user identity.

### Interface Segregation Principle (ISP)

Not directly used because Python is duck-typed and the project does not define explicit interfaces.
- However, the class boundaries already keep interfaces small and specific.
- If buyer-only or seller-only behavior were required, the system could be split into smaller role-specific classes.

### Dependency Inversion Principle (DIP)

The project mostly uses dependency passing rather than hardcoding logic.
- `Auction` receives a `Bid`, not raw bid values.
- `AuctionSystem` accepts a `seller` user when creating auctions.

This makes the code more flexible and easier to test.

## Design Notes

Business rules enforced by code:
- Bid must be greater than the current highest bid.
- Closed auctions reject bids.
- The highest bidder becomes the winner.
- `AuctionSystem` centralizes registration and auction lifecycle management.

Potential improvements:
- Prevent sellers from bidding on their own auctions.
- Add validation for duplicate user registration.
- Add persistence or file-based storage.
- Implement bid history and timestamp tracking.
- Support more auction types and user roles.

## Running the Project

From the project root, run:

```bash
python main.py
```

Then follow the menu prompts to register users, create auctions, place bids, and close auctions.
