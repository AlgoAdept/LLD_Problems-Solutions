import User

class Bid:
    def __init__(self, bidder: User, amount: int):
        self.bidder = bidder
        self.amount = amount

    def __str__(self):
        return f"Bid(bidder={self.bidder.name}, amount={self.amount})"
