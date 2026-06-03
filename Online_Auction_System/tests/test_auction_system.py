from models.user import User
from models.auction_item import AuctionItem
from models.auction import Auction


def test_register_user_adds_user(auction_system):
    user = auction_system.register_user("Alice", "alice@example.com")

    assert user in auction_system.users
    assert user.name == "Alice"
    assert user.email == "alice@example.com"


def test_create_auction_adds_auction(auction_system, seller):
    auction = auction_system.create_auction("Antique Vase", 100, "A rare porcelain vase.", seller)

    assert auction in auction_system.auctions
    assert isinstance(auction.item, AuctionItem)
    assert auction.item.title == "Antique Vase"
    assert auction.seller == seller


def test_get_active_auctions_returns_open_auctions(auction_system, seller):
    auction = auction_system.create_auction("Antique Vase", 100, "A rare porcelain vase.", seller)
    auction_system.create_auction("Old Clock", 50, "A vintage clock.", seller)

    assert auction in auction_system.get_active_auctions()
    assert len(auction_system.get_active_auctions()) == 2


def test_close_auction_closes_auction(auction_system, seller):
    auction = auction_system.create_auction("Antique Vase", 100, "A rare porcelain vase.", seller)
    auction_system.close_auction(auction)

    assert auction.closed is True
    assert auction not in auction_system.get_active_auctions()


def test_find_user_by_name_returns_user_if_exists(auction_system):
    auction_system.register_user("Alice", "alice@example.com")

    found = auction_system.find_user("Alice")

    assert found is not None
    assert found.name == "Alice"


def test_find_user_by_name_returns_none_if_missing(auction_system):
    assert auction_system.find_user("Nonexistent") is None
