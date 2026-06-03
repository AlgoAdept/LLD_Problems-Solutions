from models.auction_item import AuctionItem
from models.bid import Bid
from models.user import User
from exceptions.exceptions import AuctionClosedError, InvalidBidError


class Auction:
    def __init__(self, item: AuctionItem, starting_price: int, seller: User):
        self.item = item
        self.starting_price = starting_price
        self.current_price = starting_price
        self.highest_bidder = None
        self.seller = seller
        self.closed = False

    def __str__(self):
        return (
            f"Auction(item={self.item.title}, current_price={self.current_price}, "
            f"highest_bidder={self.highest_bidder})"
        )

    def place_bid(self, bid: Bid):
        if self.closed:
            raise AuctionClosedError("Cannot place a bid on a closed auction.")
        if bid.amount <= self.current_price:
            raise InvalidBidError(
                f"Bid amount must be greater than current price ({self.current_price})."
            )
        self.current_price = bid.amount
        self.highest_bidder = bid.bidder
        return True

    def close_auction(self):
        self.closed = True
        return "Auction closed"

    def get_winner(self):
        return self.highest_bidder

    def get_winning_bid(self):
        return self.current_price if self.highest_bidder else None
