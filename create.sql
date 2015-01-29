/* SQL statements to create the tables necessary for the application */

CREATE TABLE Bidder (
  UserID VARCHAR(80),
  Rating INTEGER,
  Location VARCHAR(80),
  Country VARCHAR(80),

  PRIMARY KEY (UserID),
);

CREATE TABLE Seller (
  UserID VARCHAR(80),
  Rating INTEGER,

  PRIMARY KEY (UserID)
);

CREATE TABLE ItemDescription (
  ItemID INTEGER,
  Name VARCHAR(80),
  Buy_Price DECIMAL(10, 2),
  First_Bid DECIMAL(10, 2),
  Number_of_Bids INTEGER,
  Location VARCHAR(80),
  Latitude DECIMAL(9, 6),
  Longitude DECIMAL(9, 6),
  Currently DECIMAL(10, 2),
  Country VARCHAR(80),
  Started DATETIME,
  Ends DATETIME,
  Seller VARCHAR(80),
  Description VARCHAR(4000),

  PRIMARY KEY (ItemID),
  FOREIGN KEY Seller REFERENCES Seller(UserID)
);

CREATE TABLE Bids (
  ItemID INTEGER,
  UserID VARCHAR(80),
  Time DATETIME,
  Amount DECIMAL(10, 2),

  FOREIGN KEY (ItemID) REFERENCES ItemDescription(ItemID),
  FOREIGN KEY (UserID) REFERENCES Bidder(UserID)
);

CREATE TABLE ItemCategory (
  ItemID INTEGER,
  Category VARCHAR(80),

  FOREIGN KEY (ItemID) REFERENCES ItemDescription(ItemID)
);
