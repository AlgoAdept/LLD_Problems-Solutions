from models.auction_item import AuctionItem


def test_auction_item_properties():
    item = AuctionItem("Antique Vase", 100, "A rare porcelain vase.")

    assert item.title == "Antique Vase"
    assert item.base_price == 100
    assert item.description == "A rare porcelain vase."


def test_auction_item_str():
    item = AuctionItem("Antique Vase", 100, "A rare porcelain vase.")

    assert str(item) == "AuctionItem(title=Antique Vase, base_price=100)"
