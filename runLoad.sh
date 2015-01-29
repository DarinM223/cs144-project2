#!/bin/bash

# Bash script to create the tables and load the data

# Run drop.sql batch file to drop existing tables
# Drop tables only if they exist
mysql CS144 < drop.sql

# Run create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
ant
ant run-all

# If the Java code does not handle duplicate removal, do this now
# TODO: handle duplicate removal

# Run the load.sql batch file to load the data
mysql CS144 < load.sql

# remove all temporary files
rm Bid.dat
rm Bidder.dat
rm Item.dat
rm ItemCategory.dat
rm Seller.dat