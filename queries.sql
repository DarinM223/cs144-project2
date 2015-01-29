/* SQL queries to test the relational database */

/* TODO: add queries */
SELECT COUNT(*)
FROM (
	SELECT UserID
	FROM Bidder
	UNION
	SELECT UserID
	FROM Seller
) Q1;

SELECT COUNT(*)
FROM Item i
WHERE BINARY i.Location = 'New York';

SELECT COUNT(*)
FROM (
  SELECT ItemID FROM ItemCategory
  GROUP BY ItemID
  HAVING COUNT(*) = 4
) Q2;

SELECT i.ItemID as ITEMID
FROM Item i, Bid b
WHERE Ends > "2001-12-20 00:00:00" AND i.ItemID = b.ItemID
ORDER BY Currently DESC
LIMIT 1;

SELECT COUNT(*)
FROM (
	SELECT UserID
	FROM Seller
	WHERE Rating > 1000
) as Query5;

SELECT COUNT(*)
FROM (
	SELECT b.UserID
	FROM Bidder b, Seller s
	WHERE b.UserID = s.UserID
) as Query6;

SELECT COUNT(*) AS "COUNT(DISTINCT CATEGORY)"
FROM (
	SELECT COUNT(*) AS "DISTINCT CATEGORY"
	FROM Bid b, ItemCategory c 
	WHERE Amount > 100 AND b.ItemID = c.ItemID 
	GROUP BY Category
) as Query7;
