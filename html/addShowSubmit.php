<?php


	include_once dirname(__FILE__) . '/constants.php';

	$con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);


	if($con === false){
		die("ERROR: Could not connect. " . $this->connect_error);
	}


	$showname = $con->real_escape_string($_REQUEST['show_name']);
	$showdesc = $con->real_escape_string($_REQUEST['show_description']);
	$runningtime = $con->real_escape_string($_REQUEST['running_time']);

	$query = "INSERT INTO shows(showName, showDescription, runningTime) VALUES ('$showname', '$showdesc', $runningtime)";

	if($con->query($query) === true){
		echo "Records inserted";
	}else{
		echo "ERROR: " . $con->error;
	}


	$con->close();
?>
