/* Load the data generated from the parser into MySQL */

LOAD DATA LOCAL INFILE 'Bidder.dat' INTO TABLE Bidder;
LOAD DATA LOCAL INFILE 'Seller.dat' INTO TABLE Seller;
LOAD DATA LOCAL INFILE 'Bid.dat' INTO TABLE Bid;
LOAD DATA LOCAL INFILE 'ItemDescription.dat' INTO TABLE ItemDescription;
LOAD DATA LOCAL INFILE 'ItemCategory.dat' INTO TABLE ItemCategory;
