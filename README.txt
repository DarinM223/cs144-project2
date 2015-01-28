Name: Darin Minamoto
UID:

Name: Kevin Tong
UID: 704161137

---------------------------------------------------------------------------------------

Part B:

1.
Bidder(UserID [KEY], Rating, Location, Country)

Seller(UserID [KEY], Rating)

Bids(ItemID [KEY], UserID [KEY], Time [KEY], Amount)

ItemDescription(ItemID [KEY], Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Latitude, Longitude, Country, Started, Ends, Seller [FOREIGN KEY], Description)

ItemCategory(ItemID [KEY], Category)

2.
All functional dependencies specify the key(s) for each relation:
Bidder: UserID
Seller: UserID
Bids: ItemID, UserID, Time (all three fields together)
ItemDescription: ItemID
ItemCatagory: ItemID

MVD
ItemID -> Category

3. Yes.

4. Yes.