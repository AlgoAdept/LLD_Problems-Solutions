import pytest

from models.auction import Auction
from models.auction_item import AuctionItem
from models.bid import Bid
from models.user import User
from services.auction_system import AuctionSystem


@pytest.fixture
def seller():
    return User("Alice", "alice@example.com")


@pytest.fixture
def bidder():
    return User("Bob", "bob@example.com")


@pytest.fixture
def auction_item():
    return AuctionItem("Antique Vase", 100, "A rare porcelain vase.")


@pytest.fixture
def auction(seller, auction_item):
    return Auction(auction_item, 100, seller)


@pytest.fixture
def auction_system():
    return AuctionSystem()
