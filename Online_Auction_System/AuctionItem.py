class AuctionItem:
    def __init__(self, title: str, base_price: int, description: str):
        self.title = title
        self.base_price = base_price
        self.description = description

    def __str__(self):
        return f"AuctionItem(title={self.title}, base_price={self.base_price})"
