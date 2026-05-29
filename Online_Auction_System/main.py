import Auction
from AuctionSystem import AuctionSystem
from Bid import Bid

MENU = (
    "\nAuction System Menu:\n"
    "1. Register user\n"
    "2. Create auction\n"
    "3. Place bid\n"
    "4. Close auction\n"
    "5. Show auctions\n"
    "6. Exit\n"
)


def choose_user(system: AuctionSystem):
    if not system.users:
        print("No users registered yet.")
        return None
    print("Registered users:")
    for index, user in enumerate(system.users, start=1):
        print(f"{index}. {user.name} ({user.email})")
    choice = input("Enter user number: ").strip()
    if not choice.isdigit():
        print("Invalid selection.")
        return None
    index = int(choice) - 1
    if 0 <= index < len(system.users):
        return system.users[index]
    print("Invalid user number.")
    return None


def choose_auction(system: AuctionSystem):
    active_auctions = system.get_active_auctions()
    if not active_auctions:
        print("No active auctions available.")
        return None
    print("Active auctions:")
    for index, auction in enumerate(active_auctions, start=1):
        print(
            f"{index}. {auction.item.title} - current price ₹{auction.current_price} "
            f"(seller: {auction.seller.name})"
        )
    choice = input("Enter auction number: ").strip()
    if not choice.isdigit():
        print("Invalid selection.")
        return None
    index = int(choice) - 1
    if 0 <= index < len(active_auctions):
        return active_auctions[index]
    print("Invalid auction number.")
    return None


def print_auction_details(auction: Auction):
    print(f"Auction created: {auction.item.title}")
    print(f"Starting price: ₹{auction.starting_price}")
    print(f"Seller: {auction.seller.name}")


if __name__ == "__main__":
    system = AuctionSystem()
    print("Welcome to the simple auction system.")

    while True:
        print(MENU)
        choice = input("Choose an option: ").strip()

        if choice == "1":
            name = input("Enter user name: ").strip()
            email = input("Enter user email: ").strip()
            if name and email:
                user = system.register_user(name, email)
                print(f"User registered: {user.name}")
            else:
                print("Name and email are required.")

        elif choice == "2":
            seller = choose_user(system)
            if seller is None:
                continue
            title = input("Enter item title: ").strip()
            price_text = input("Enter starting price: ").strip()
            description = input("Enter item description: ").strip()
            if not price_text.isdigit():
                print("Please enter a numeric starting price.")
                continue
            auction = system.create_auction(title, int(price_text), description, seller)
            print_auction_details(auction)

        elif choice == "3":
            bidder = choose_user(system)
            if bidder is None:
                continue
            auction = choose_auction(system)
            if auction is None:
                continue
            bid_text = input("Enter bid amount: ").strip()
            if not bid_text.isdigit():
                print("Please enter a numeric bid amount.")
                continue
            bid = Bid(bidder=bidder, amount=int(bid_text))
            if auction.place_bid(bid):
                print(f"{bidder.name} bid ₹{bid.amount}")
            else:
                print(f"Bid must be higher than current price ₹{auction.current_price}.")

        elif choice == "4":
            auction = choose_auction(system)
            if auction is None:
                continue
            print(system.close_auction(auction))
            winner = auction.get_winner()
            if winner:
                print(f"Winner: {winner.name}")
                print(f"Winning Bid: ₹{auction.get_winning_bid()}")
            else:
                print("Winner: None")

        elif choice == "5":
            if not system.auctions:
                print("No auctions have been created yet.")
                continue
            print("All auctions:")
            for index, auction in enumerate(system.auctions, start=1):
                status = "Closed" if auction.closed else "Open"
                highest = auction.highest_bidder.name if auction.highest_bidder else "No bids"
                print(
                    f"{index}. {auction.item.title} - current price ₹{auction.current_price} "
                    f"(status: {status}, highest bidder: {highest})"
                )

        elif choice == "6":
            print("Exiting auction system.")
            break

        else:
            print("Invalid option, please choose from the menu.")
