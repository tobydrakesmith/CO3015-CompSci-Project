<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


	$venueName = $_GET['venueName'];

	$stmt = $con->prepare("SELECT * FROM venue WHERE venueName = ?");
	$stmt->bind_param("s", $venueName);
	$stmt->execute();
	if ($stmt->fetch()) echo 'Venue name already exists';
	else echo 'ok';

?>
