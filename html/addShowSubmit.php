<?php


	include_once dirname(__FILE__) . '/constants.php';

	$con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);


	if($con === false){
		die("ERROR: Could not connect. " . $this->connect_error);
	}


	$showname = $con->real_escape_string($_REQUEST['show_name']);
	$showdesc = $con->real_escape_string($_REQUEST['show_description']);
	$runningtime = $con->real_escape_string($_REQUEST['running_time']);
	$playmusical = $_REQUEST['classification'];

	$query = "INSERT INTO shows(showName, showDescription, runningTime, classification) VALUES ('$showname', '$showdesc', $runningtime, '$playmusical')";

	if($con->query($query)){
		echo "Records inserted";
                header('Refresh: 2; URL = homepage.php');
	}else{
		echo "ERROR: " . $con->error;
                echo "<br><br><a href='/homepage.php'>Home</a>";

	}

	$con->close();
?>
