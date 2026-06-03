class AuctionException(Exception):
    "Base exception for auction domain errors."


class AuctionClosedError(AuctionException):
    "Raised when an operation is attempted on a closed auction."


class InvalidBidError(AuctionException):
    "Raised when a bid does not exceed the current price."


class AuctionNotFoundError(AuctionException):
    "Raised when a requested auction cannot be found."


class UserNotFoundError(AuctionException):
    "Raised when a requested user cannot be found."
