<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


	$showName = $_GET['showName'];

	$stmt = $con->prepare("SELECT * FROM shows WHERE showName = ?");
	$stmt->bind_param("s", $showName);
	$stmt->execute();

	if ($stmt->fetch())
		echo 'This show name is already in use - you can add a new instance in the add show instance page, if this is a new show please change the name';
	else
		echo 'Show name OK';


?>
