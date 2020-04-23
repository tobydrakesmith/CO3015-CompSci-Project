<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);



	$startDate = str_replace('/','-',$_GET['startDate']);
	$endDate = str_replace('/','-',$_GET['endDate']);
	$venueName = $_GET['venueName'];

	$stmt = $con->prepare("SELECT * FROM showInstance WHERE venueName = ? AND ((startDate <= ? AND endDate >= ?) OR (startDate <= ? AND endDate >= ?) OR (startDate > ? AND endDate < ?))");
	$stmt->bind_param("sssssss", $venueName, $startDate, $startDate, $endDate, $endDate, $startDate, $endDate);
	$stmt->execute();
	while($stmt->fetch()){}
	if ($stmt->num_rows > 0) echo '<p style="color:red">Warning: these dates clash with an existing show at this venue</p>';


?>
