from models.user import User


def test_user_properties():
    user = User("Alice", "alice@example.com")

    assert user.name == "Alice"
    assert user.email == "alice@example.com"


def test_user_str():
    user = User("Alice", "alice@example.com")

    assert str(user) == "User(name=Alice, email=alice@example.com)"
