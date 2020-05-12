<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false){

                die("ERROR: Could not connect. " .$this->connect_error);
        }

        $venuename = $con->real_escape_string($_REQUEST['venue_name']);
        $venuedesc = $con->real_escape_string($_REQUEST['venue_description']);
        $numseats = $con->real_escape_string($_REQUEST['venue_noseats']);
        $postcode = $con->real_escape_string($_REQUEST['venue_postcode']);
        $city = $con->real_escape_string($_REQUEST['venue_city']);
        $region = $_REQUEST['venue_region'];

        $stmt = $con->prepare("UPDATE venue SET venueDesc = ?, capacity = ?, postcode = ?, city = ?, region = ?	WHERE venueName = ?");
	$stmt->bind_param("sissss", $venuedesc, $numseats, $postcode, $city, $region, $venuename);

        if ($stmt->execute()) echo "Success";
        else echo $con->error;

        $con->close();


?>

