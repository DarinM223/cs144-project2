/* SQL statements to create the tables necessary for the application */

CREATE TABLE Bidder (
  UserID INTEGER,
  Rating INTEGER,
  Location VARCHAR(80),
  Country VARCHAR(80),

  PRIMARY KEY (UserID),
);

CREATE TABLE Seller (
  UserID INTEGER,
  Rating INTEGER,

  PRIMARY KEY (UserID)
);

CREATE TABLE ItemDescription (
  ItemID INTEGER,
  Name VARCHAR(80),
  Buy_Price FLOAT,
  First_Bid,
  Number_of_Bids INTEGER,
  Location VARCHAR(80),
  Latitude,
  Longitude,
  Country VARCHAR(80),
  Started,
  Ends,
  Seller INTEGER,
  Description VARCHAR(80),

  PRIMARY KEY (ItemID)
  FOREIGN KEY Seller REFERENCES Seller(UserID)
);

CREATE TABLE Bids (
  ItemID INTEGER,
  UserID INTEGER,
  Time INTEGER, /* WTF is time */
  Amount INTEGER,

  FOREIGN KEY (ItemID) REFERENCES ItemDescription(ItemID),
  FOREIGN KEY (UserID) REFERENCES Bidder(UserID)
);

CREATE TABLE ItemCategory (
  ItemID INTEGER,
  Category VARCHAR(80),

  FOREIGN KEY (ItemID) REFERENCES ItemDescription(ItemID)
);
