<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


        $venueName = $_GET['q'];

	$stmt = $con->prepare("SELECT showInstanceID, showName, startDate, endDate FROM showInstance WHERE venueName = ? AND endDate >= NOW()");
	$stmt->bind_param("s", $venueName);
	$stmt->execute();
	$stmt->bind_result($showInstanceID, $showName, $startDate, $endDate);
	$shows = array();
	while($stmt->fetch()){
		$show = array();
		$show['id'] = $showInstanceID;
		$show['name'] = $showName;
		$show['startDate'] = $startDate;
		$show['endDate'] = $endDate;
		array_push($shows, $show);
	}

	echo json_encode($shows);


?>
