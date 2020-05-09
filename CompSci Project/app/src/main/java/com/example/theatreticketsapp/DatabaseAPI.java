package com.example.theatreticketsapp;

public class DatabaseAPI {

    private static final String ROOT_URL =
            //"http://192.168.0.28/Api.php?apicall="; //linux home
            //"http://143.210.204.216/Api.php?apicall="; //linux uni
           //"http://192.168.43.193/Api.php?apicall="; // phone hotspot
            //"http://192.168.0.33/Api.php?apicall="; //home home
            "http://81.101.72.233:80/Api.php?apicall="; //public access



    public static final String URL_CREATE_USER =
            ROOT_URL + "createuser";

    public static final String URL_GET_USER =
            ROOT_URL + "getuser&email=";

    public static final String URL_GET_LIVE_SHOWS =
            ROOT_URL + "getliveshows";

    public static final String URL_GET_SHOW_INFO =
            ROOT_URL + "getshowinfo&showName=";

    public static final String URL_CREATE_BOOKING =
            ROOT_URL + "createbooking";

    public static final String URL_CREATE_TICKET =
            ROOT_URL + "createticket";

    public static final String URL_GET_BOOKINGS =
            ROOT_URL + "getbookings&userID=";

    public static final String URL_CREATE_REVIEW =
            ROOT_URL + "createreview";

    static final String URL_GET_TICKETS =
            ROOT_URL + "gettickets&bookingID=";


    static final String URL_GET_VENUE_INFO =
            ROOT_URL + "getvenueinfo&venueName=";

    static final String URL_GET_REVIEWS =
            ROOT_URL + "getreviews&showName=";

    static final String URL_GET_SALES =
            ROOT_URL + "getsales&showInstanceID=";

    static final String URL_GET_REVIEWS_DETAILED =
            ROOT_URL + "getreviewsdetailed&showName=";

    static final String URL_CREATE_BASKET_BOOKING =
            ROOT_URL + "addbasketbooking&showInstanceID=";

    static final String URL_DELETE_BASKET_BOOKING =
            ROOT_URL + "deletebasketbooking&tempID=";

    static final String URL_GET_VENUE_INFO_BOOKING =
            ROOT_URL + "venueinfobooking&showInstanceID=";

    static final String URL_GET_RUNNING_TIME =
            ROOT_URL + "getrunningtime&showName=";

    static final String URL_CHECK_PASSWORD =
            ROOT_URL + "checkpassword&userID=";

    static final String URL_RESET_PASSWORD_LOGGED_ON =
            ROOT_URL + "updatepasswordloggedon&userID=";

    static final String URL_SEND_RESET_PASSWORD_EMAIL =
            ROOT_URL + "sendresetpasswordemail&email=";

    static final String URL_SEND_BOOKING_EMAIL =
            ROOT_URL + "sendbookingconfirmation";

    static final String URL_GET_USER_REVIEWS =
            ROOT_URL + "getuserreviews&userID=";

    static final String URL_EDIT_USER_REVIEW =
            ROOT_URL + "editusereview";

}
