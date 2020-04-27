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

	$query = "INSERT INTO venue(venueName, capacity, venueDesc, postcode) VALUES ('$venuename', '$numseats', '$venuedesc', '$postcode')";

	if($con->query($query) === true){
//		$command = escapeshellcmd('export GOOGLE_APPLICATION_CREDENTIALS="/home/tjd17/Documents/theatre-tickets-app-firebase-adminsdk-nm5iv-fb5e36781c.json" ; python firebase.py');
//		$command = escapeshellcmd('python firebase.py');
//		$output = shell_exec($command);
		echo "success";
	}else{
		echo "ERROR " . $con->error;
	}



	$con->close();




?>
