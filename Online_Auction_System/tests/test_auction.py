import pytest

from models.bid import Bid
from models.user import User
from exceptions import AuctionClosedError, InvalidBidError


def test_initial_auction_state(auction, auction_item, seller):
    assert auction.item == auction_item
    assert auction.starting_price == 100
    assert auction.current_price == 100
    assert auction.highest_bidder is None
    assert auction.seller == seller
    assert auction.closed is False


def test_place_bid_updates_current_price_and_highest_bidder(auction, bidder):
    bid = Bid(bidder, 150)

    auction.place_bid(bid)

    assert auction.current_price == 150
    assert auction.highest_bidder == bidder


def test_place_bid_raises_invalid_bid_error_for_lower_amount(auction, bidder):
    bid = Bid(bidder, 90)

    with pytest.raises(InvalidBidError):
        auction.place_bid(bid)


def test_place_bid_raises_invalid_bid_error_for_equal_amount(auction, bidder):
    bid = Bid(bidder, 100)

    with pytest.raises(InvalidBidError):
        auction.place_bid(bid)


def test_close_auction_marks_closed(auction):
    result = auction.close_auction()

    assert auction.closed is True
    assert result == "Auction closed"


def test_place_bid_raises_auction_closed_error_when_closed(auction, bidder):
    auction.close_auction()
    bid = Bid(bidder, 150)

    with pytest.raises(AuctionClosedError):
        auction.place_bid(bid)


def test_get_winner_returns_none_when_no_bids(auction):
    assert auction.get_winner() is None
    assert auction.get_winning_bid() is None


def test_get_winner_and_winning_bid_after_bid(auction, bidder):
    bid = Bid(bidder, 150)
    auction.place_bid(bid)

    assert auction.get_winner() == bidder
    assert auction.get_winning_bid() == 150
