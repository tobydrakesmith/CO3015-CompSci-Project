<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


	$showName = $_GET['showName'];

	$stmt = $con->prepare("SELECT runningTime FROM shows WHERE showName = ?");
	$stmt->bind_param("s", $showName);
	$stmt->execute();
	$stmt->bind_result($runningTime);
	if($stmt->fetch())
		echo $runningTime . " minutes";


?>
