from models.auction import Auction
from models.auction_item import AuctionItem
from models.user import User


class AuctionSystem:
    def __init__(self):
        self.users = []
        self.auctions = []

    def register_user(self, name: str, email: str):
        user = User(name, email)
        self.users.append(user)
        return user

    def create_auction(self, item_title: str, base_price: int, description: str, seller: User):
        item = AuctionItem(item_title, base_price, description)
        auction = Auction(item, base_price, seller)
        self.auctions.append(auction)
        return auction

    def close_auction(self, auction: Auction):
        return auction.close_auction()

    def get_active_auctions(self):
        return [auction for auction in self.auctions if not auction.closed]

    def list_auctions(self):
        return self.auctions

    def find_user(self, name: str):
        for user in self.users:
            if user.name.lower() == name.lower():
                return user
        return None
