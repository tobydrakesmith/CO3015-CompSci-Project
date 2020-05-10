package com.configfiles.scannerapp;

public class DatabaseAPI {

    private static final String ROOT_URL =
            //"http://192.168.0.28/Api.php?apicall="; //linux home
            //"http://143.210.204.216/Api.php?apicall="; //linux uni
            //"http://192.168.43.193/Api.php?apicall="; // phone hotspot
            //"http://192.168.0.33/Api.php?apicall="; //home home
            "http://81.101.72.233:80/Api.php?apicall="; //public access

    public static final String URL_SCAN_TICKET =
            ROOT_URL + "checkscanned&ticketID=";

    public static final String URL_GET_VENUES =
            ROOT_URL + "getvenues";

    public static final String URL_GET_INSTANCES =
            ROOT_URL + "getshowinstance&venueName=";
}
