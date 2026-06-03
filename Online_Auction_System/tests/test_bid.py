from models.bid import Bid
from models.user import User


def test_bid_properties():
    bidder = User("Bob", "bob@example.com")
    bid = Bid(bidder, 150)

    assert bid.bidder == bidder
    assert bid.amount == 150


def test_bid_str():
    bidder = User("Bob", "bob@example.com")
    bid = Bid(bidder, 150)

    assert str(bid) == "Bid(bidder=Bob, amount=150)"
