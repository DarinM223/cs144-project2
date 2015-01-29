/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };

    //hashmaps and arraylist to store
    static Map<String, Bidder> bidderMap = new HashMap<String, Bidder>();
    static Map<String, Seller> sellerMap = new HashMap<String, Seller>();
    static ArrayList<Bid> bidList = new ArrayList<Bid>();
    static ArrayList<Item> itemList = new ArrayList<Item>();
    static ArrayList<ItemCategory> itemCategoryList = new ArrayList<ItemCategory>();

    //class which represents entry in Bidder.csv
    public static class Bidder {
        String b_userID;
        String b_rating;
        String b_location;
        String b_country;

        public Bidder(String userID, String rating, String location, String country) {
            b_userID = userID;
            b_rating = rating;
            b_location = location;
            b_country = country;
        }
    }

    //class which represents entry in Seller.csv
    public static class Seller {
        String s_userID;
        String s_rating;

        public Seller(String userID, String rating) {
            s_userID = userID;
            s_rating = rating;
        }
    }

    //class which represents entry in Bid.csv
    public static class Bid {
        String bd_itemID;
        String bd_userID;
        String bd_time;
        String bd_amount;

        public Bid(String itemID, String userID, String time, String amount) {
            bd_itemID = itemID;
            bd_userID = userID;
            bd_time = time;
            bd_amount = amount;
        }
    }

    //class which represents entry in Item.csv
    public static class Item {
        String i_itemID;
        String i_name;
        String i_currently;
        String i_buy_price;
        String i_first_bid;
        String i_number_of_bids;
        String i_location;
        String i_latitude;
        String i_longitude;
        String i_country;
        String i_started;
        String i_ends;
        String i_seller;
        String i_description;

        public Item(String itemID, String name, String currently, String buy_price,
                            String first_bid, String number_of_bids, String location, String latitude,
                            String longitude, String country, String started, String ends,
                            String seller, String description) {
            i_itemID = itemID;
            i_name = name;
            i_currently = currently;
            i_buy_price = buy_price;
            i_first_bid = first_bid;
            i_number_of_bids = number_of_bids;
            i_location = location;
            i_latitude = latitude;
            i_longitude = longitude;
            i_country = country;
            i_started = started;
            i_ends = ends;
            i_seller = seller;
            i_description = description;
        }
    }

    //class which represents entry in ItemCategory.csv
    public static class ItemCategory {
        String ic_itemID;
        String ic_category;

        public ItemCategory(String itemID, String category) {
            ic_itemID = itemID;
            ic_category = category;
        }
    }

    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    //converts xml date format to sql date format
    static String convertDateFormat(String date) {
        try {
                SimpleDateFormat old_format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
                SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                date = new_format.format(old_format.parse(date));
        }
        catch(ParseException e) {
            System.out.println("Parse error!");
        }
        
        return date;
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        //put all items of the file into an array
        Element root = doc.getDocumentElement();
        Element[] items = getElementsByTagNameNR(root, "Item");

        //iterate through all items and put into respective structures
        for (Element e : items) {
            //get item info for e
            String itemID = e.getAttribute("ItemID");
            String name = getElementTextByTagNameNR(e, "Name");
            
            //convert currently, buy_price, first_bid to money-string
            String currently = strip(getElementTextByTagNameNR(e, "Currently"));
            String buy_price = strip(getElementTextByTagNameNR(e, "Buy_Price"));
            String first_bid = strip(getElementTextByTagNameNR(e, "First_Bid"));

            String number_of_bids = getElementTextByTagNameNR(e, "Number_of_Bids"); 
            Element location_e = getElementByTagNameNR(e, "Location");
            String location = getElementText(location_e);
            String latitude = location_e.getAttribute("Latitude");
            String longitude = location_e.getAttribute("Longitude");
            String country = getElementTextByTagNameNR(e, "Country");

            //convert started and ends
            String started = convertDateFormat(getElementTextByTagNameNR(e, "Started"));
            String ends = convertDateFormat(getElementTextByTagNameNR(e, "Ends"));

            //get seller userID and rating
            Element seller = getElementByTagNameNR(e, "Seller");
            String s_userID = seller.getAttribute("UserID");
            String s_rating = seller.getAttribute("Rating");

            //check if seller is already in sellerMap and add seller if not
            if (!sellerMap.containsKey(s_userID)) {
                Seller s = new Seller(s_userID, s_rating);
                sellerMap.put(s_userID, s);
            }

            //truncate description to 4000 characters if its longer than 4000
            String description = getElementTextByTagNameNR(e, "Description");
            if (description.length() > 4000) {
                description = description.substring(0, 4000);
            }

            //get bids for the item if any
            Element bids_root = getElementByTagNameNR(e, "Bids");
            Element[] bids = getElementsByTagNameNR(bids_root, "Bid");
            for (Element b : bids) {
                //get bidder info
                Element bidder = getElementByTagNameNR(b, "Bidder");
                String b_userID = bidder.getAttribute("UserID");
                String b_rating = bidder.getAttribute("Rating");
                String b_location = getElementTextByTagNameNR(bidder, "Location");
                String b_country = getElementTextByTagNameNR(bidder, "Country");

                //check if bidder is already in bidderMap and add bidder if not
                if (!bidderMap.containsKey(b_userID)) {
                    Bidder b_bidder = new Bidder(b_userID, b_rating, b_location, b_country);
                    bidderMap.put(b_userID, b_bidder);
                }

                //get remaining bid info
                String time = convertDateFormat(getElementTextByTagNameNR(b, "Time"));
                String amount = strip(getElementTextByTagNameNR(b, "Amount"));

                //add bid to bidList
                Bid bid = new Bid(itemID, b_userID, time, amount);
                bidList.add(bid);
            }

            //get all categories and put into itemCategoryList
            Element[] categories = getElementsByTagNameNR(e, "Category");
            for (Element c : categories) {
                //get category
                String category = getElementText(c);

                //create ItemCategory object and add to itemCategoryList
                ItemCategory ic = new ItemCategory(itemID, category);
                itemCategoryList.add(ic);
            }

            //add item to itemList
            Item i = new Item(itemID, name, currently, buy_price, first_bid,
                            number_of_bids, location, latitude, longitude,
                            country, started, ends, s_userID, description);
            itemList.add(i);
        }
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }

        //write bidderMap to Bidder.csv
        try {
            FileWriter bidder_fw = new FileWriter("Bidder.csv");
            BufferedWriter bidder_bw = new BufferedWriter(bidder_fw);

            //iterate through bidderMap
            for (Map.Entry<String, Bidder> e : bidderMap.entrySet()) {
                //get bidder
                Bidder bidder = e.getValue();

                //construct record string
                String output = bidder.b_userID + columnSeparator
                                + bidder.b_rating + columnSeparator
                                + bidder.b_location + columnSeparator
                                + bidder.b_country + "\n";

                //write record to file
                bidder_bw.write(output);
            }

            bidder_bw.close();
            bidder_fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //write sellerMap to Seller.csv
        try {
            FileWriter seller_fw = new FileWriter("Seller.csv");
            BufferedWriter seller_bw = new BufferedWriter(seller_fw);

            //iterate through sellerMap
            for (Map.Entry<String, Seller> e : sellerMap.entrySet()) {
                //get seller
                Seller seller = e.getValue();

                //construct record string
                String output = seller.s_userID + columnSeparator
                                + seller.s_rating + "\n";

                //write record to file
                seller_bw.write(output);
            }

            seller_bw.close();
            seller_fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //write bidList to Bid.csv
        try {
            FileWriter bid_fw = new FileWriter("Bid.csv");
            BufferedWriter bid_bw = new BufferedWriter(bid_fw);

            //iterate through bidList
            for (Bid bid : bidList) {
                //construct record string
                String output = bid.bd_itemID + columnSeparator
                                + bid.bd_userID + columnSeparator
                                + bid.bd_time + columnSeparator
                                + bid.bd_amount + "\n";

                //write record to file
                bid_bw.write(output);
            }

            bid_bw.close();
            bid_fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //write itemList to Item.csv
        try {
            FileWriter item_fw = new FileWriter("Item.csv");
            BufferedWriter item_bw = new BufferedWriter(item_fw);            

            //iterate through itemList
            for (Item item : itemList) {
                //construct record string
                String output = item.i_itemID + columnSeparator
                                + item.i_name + columnSeparator
                                + item.i_currently + columnSeparator
                                + item.i_buy_price + columnSeparator
                                + item.i_first_bid + columnSeparator
                                + item.i_number_of_bids + columnSeparator
                                + item.i_location + columnSeparator
                                + item.i_latitude + columnSeparator
                                + item.i_longitude + columnSeparator
                                + item.i_country + columnSeparator
                                + item.i_started + columnSeparator
                                + item.i_ends + columnSeparator
                                + item.i_seller + columnSeparator
                                + item.i_description + "\n";

                //write record to file
                item_bw.write(output);
            }

            item_bw.close();
            item_fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //write itemCategoryList to ItemCategory.csv
        try {
            FileWriter itemCategory_fw = new FileWriter("ItemCategory.csv");
            BufferedWriter itemCategory_bw = new BufferedWriter(itemCategory_fw);    

            //iterate through itemCategoryList
            for (ItemCategory itemCategory : itemCategoryList) {
                //construct record string
                String output = itemCategory.ic_itemID + columnSeparator
                                + itemCategory.ic_category + "\n";

                //write record to file
                itemCategory_bw.write(output);
            }

            itemCategory_bw.close();
            itemCategory_fw.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
