package fortin.restaurant.digital.menu.activities;


import fortin.restaurant.digital.menu.model.DishOrderPojo;

import java.util.List;


public class DataManager {
    public static final String TAG = "Demo";
    public static final String MAIN = "http://swiftomatics.in/FortinRestaurant/webservice/";  // Change your URL here.
  public static final String URL = "http://swiftomatics.in/FortinRestaurant/upload/";

    public static List<DishOrderPojo> dishorederlist;
    public static String cusineid = "";
    public static String currency = "₹ ";
  //  public static List<CustAddCartPojo> custlist;
    public static String currentorderid = "0";
    public static String tableid = "0";
    public static String tablename = "";
}