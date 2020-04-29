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

	$query = "INSERT INTO venue(venueName, capacity, venueDesc, postcode, city, region) VALUES ('$venuename', '$numseats', '$venuedesc', '$postcode', '$city', '$region')";


	if($con->query($query))
		echo "Success";
	else
		echo "ERROR " . $con->error;




	$con->close();




?>
