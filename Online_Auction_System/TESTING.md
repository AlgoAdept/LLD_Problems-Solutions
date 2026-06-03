# Testing Guide for Online Auction System

## Purpose
This document describes how to write and run tests for the Online Auction System using production-level Python testing practices.

## Recommended framework
- Use `pytest` for unit tests.
- It is simple, powerful, and widely used in production Python projects.

## Project test structure
Place tests in a dedicated folder at the repository root:

```
Online_Auction_System/
├── models/
├── services/
├── exceptions/
├── tests/
│   ├── test_user.py
│   ├── test_auction_item.py
│   ├── test_bid.py
│   ├── test_auction.py
│   ├── test_auction_system.py
│   └── conftest.py
├── main.py
├── README.md
└── TESTING.md
```

## Test modules and their purpose
- `tests/test_user.py`: verifies user creation and string representation.
- `tests/test_auction_item.py`: verifies item properties and display formatting.
- `tests/test_bid.py`: verifies bid association with a bidder and amount.
- `tests/test_auction.py`: verifies core auction behavior, bid acceptance, closing, and winner selection.
- `tests/test_auction_system.py`: verifies service-level behavior for registering users, creating auctions, and querying auction state.
- `tests/conftest.py`: contains shared fixtures and reusable test setup.

## Test cases and their significance
### `tests/test_user.py`
- `test_user_properties`
  - Verifies the `User` object stores `name` and `email` correctly.
  - Significance: ensures user identity data is reliable.
- `test_user_str`
  - Verifies the string representation of `User`.
  - Significance: supports logging, debugging, and display consistency.

### `tests/test_auction_item.py`
- `test_auction_item_properties`
  - Verifies item title, base price, and description are stored correctly.
  - Significance: ensures auction items carry accurate domain data.
- `test_auction_item_str`
  - Verifies the item's string output.
  - Significance: ensures readable object display for debugging and logs.

### `tests/test_bid.py`
- `test_bid_properties`
  - Verifies bid stores bidder and amount correctly.
  - Significance: ensures bids are constructed correctly before auction logic uses them.
- `test_bid_str`
  - Verifies the string representation of `Bid`.
  - Significance: supports readable output and debugging when bids are printed.

### `tests/test_auction.py`
- `test_initial_auction_state`
  - Verifies default auction fields, including current price, seller, and closed state.
  - Significance: ensures auction objects initialize correctly.
- `test_place_bid_updates_current_price_and_highest_bidder`
  - Verifies a valid bid updates the auction price and highest bidder.
  - Significance: tests the core auction pricing behavior.
- `test_place_bid_raises_invalid_bid_error_for_lower_amount`
  - Verifies a lower bid is rejected with `InvalidBidError`.
  - Significance: enforces the business rule that bids must increase.
- `test_place_bid_raises_invalid_bid_error_for_equal_amount`
  - Verifies an equal bid is rejected as invalid.
  - Significance: protects against non-incrementing bids.
- `test_close_auction_marks_closed`
  - Verifies closing an auction updates the `closed` flag.
  - Significance: ensures auctions can be finalized.
- `test_place_bid_raises_auction_closed_error_when_closed`
  - Verifies bids on closed auctions raise `AuctionClosedError`.
  - Significance: enforces auction lifecycle rules.
- `test_get_winner_returns_none_when_no_bids`
  - Verifies `get_winner()` returns `None` when there are no bids.
  - Significance: ensures correct winner handling for empty auctions.
- `test_get_winner_and_winning_bid_after_bid`
  - Verifies winner and winning bid after a valid bid.
  - Significance: confirms auction result calculation.

### `tests/test_auction_system.py`
- `test_register_user_adds_user`
  - Verifies registering a user appends it to the system.
  - Significance: ensures the user registry works.
- `test_create_auction_adds_auction`
  - Verifies creating an auction stores it with the correct item and seller.
  - Significance: ensures new auctions are created properly.
- `test_get_active_auctions_returns_open_auctions`
  - Verifies active auctions are returned correctly.
  - Significance: ensures auction filtering works.
- `test_close_auction_closes_auction`
  - Verifies closing an auction removes it from active auctions.
  - Significance: checks service-level closing behavior.
- `test_find_user_by_name_returns_user_if_exists`
  - Verifies existing users are found by name.
  - Significance: ensures user lookup functions correctly.
- `test_find_user_by_name_returns_none_if_missing`
  - Verifies lookup returns `None` for unknown users.
  - Significance: ensures the service gracefully handles missing users.

## Best practices
1. Keep tests small and focused.
2. Test one behavior per function.
3. Use descriptive names:
   - `test_place_bid_updates_current_price`
   - `test_place_bid_raises_invalid_bid_error`
   - `test_create_auction_adds_to_auction_list`
4. Use fixtures to reduce duplication.
5. Assert behavior, not internal implementation.
6. Cover both happy paths and error cases.
7. Keep tests isolated from each other.
8. Use custom exception assertions for domain-specific errors.

## Example test patterns
### Arrange / Act / Assert
```python
from models.auction import Auction
from models.auction_item import AuctionItem
from models.user import User
from models.bid import Bid
from exceptions import InvalidBidError


def test_place_bid_updates_current_price():
    seller = User("Alice", "alice@example.com")
    item = AuctionItem("Painting", 100, "Nice art")
    auction = Auction(item, 100, seller)
    bidder = User("Bob", "bob@example.com")
    bid = Bid(bidder, 150)

    auction.place_bid(bid)

    assert auction.current_price == 150
    assert auction.highest_bidder == bidder
```

### Error assertion
```python
import pytest
from exceptions import InvalidBidError


def test_place_bid_raises_invalid_bid_error():
    seller = User("Alice", "alice@example.com")
    item = AuctionItem("Painting", 100, "Nice art")
    auction = Auction(item, 100, seller)
    bidder = User("Bob", "bob@example.com")
    bid = Bid(bidder, 90)

    with pytest.raises(InvalidBidError):
        auction.place_bid(bid)
```

## Run tests
1. Install dependencies, e.g. `pip install pytest`
2. Run all tests:
   ```bash
   pytest
   ```
3. Run a specific file:
   ```bash
   pytest tests/test_auction.py
   ```

## Optional improvements
- Add `pytest-cov` for code coverage reporting.
- Use `tests/conftest.py` for shared fixtures.
- Integrate tests into CI so they run automatically on every commit.

## Notes
- Do not include `__pycache__/` or generated files in source control.
- Keep production code and test code separate.
- Use tests as living documentation for expected behavior.
