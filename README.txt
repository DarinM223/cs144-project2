Name: Darin Minamoto
UID: 704140102

Name: Kevin Tong
UID: 704161137

---------------------------------------------------------------------------------------

Part B:

1.
Bidder(UserID [KEY], Rating, Location, Country)

Seller(UserID [KEY], Rating)

Bids(ItemID [KEY], UserID [KEY], Time [KEY], Amount)

ItemDescription(ItemID [KEY], Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Latitude, Longitude, Country, Started, Ends, Seller [FOREIGN KEY], Description)

ItemCategory(ItemID [KEY, FOREIGN KEY], Category [KEY])

2.
All functional dependencies specify the key(s) for each relation:
Bidder: UserID
Seller: UserID
Bids: ItemID, UserID, Time (all three fields together)
ItemDescription: ItemID
ItemCatagory: ItemID, Category (both fields together)

3.
All relations except for ItemCategory are in BCNF. ItemCategory is not in BCNF because each item can have multiple categories and each category can have multiple items.

4.
All relations are in 4NF.