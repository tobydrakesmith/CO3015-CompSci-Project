<?php

	include_once dirname(__FILE__) . '/constants.php';

	$con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

	if($con === false){

		die("ERROR: Could not connect. " .$this->connect_error);
	}

	$venuename = $con->real_escape_string($_REQUEST['venue_name']);
	$venuedesc = $con->real_escape_string($_REQUEST['venue_description']);
	$numseats = $con->real_escape_string($_REQUEST['venue_noseats']);


	$query = "INSERT INTO venue(venueName, numOfSeats, venueDesc) VALUES ('$venuename', '$numseats', '$venuedesc')";

	if($con->query($query) === true){
		echo "Records inserted";
	}else{
		echo "ERROR " . $con->error;
	}



	$con->close();




?>
