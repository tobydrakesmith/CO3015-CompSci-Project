<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


	$showID = $_GET['i'];
	$from = $_GET['f'];
	$to = $_GET['t'];

	$stmt = $con->prepare("SELECT bookingID FROM booking WHERE bookingDate >= ? AND bookingDate <= ? AND showInstanceID = ?");
	$stmt->bind_param("ssi", $from, $to, $showID);
	$stmt->bind_result($bookingID);
	$stmt->execute();
	$ids = array();
	while($stmt->fetch()){
		$id = array();
		$id = $bookingID;
		array_push($ids, $id);
	}
	if ($stmt->num_rows == 0) die('No sales in this period');

	$bookingids = join(',', $ids);
        $stmt = $con->prepare("SELECT price FROM ticket WHERE bookingID IN ($bookingids)");
        $stmt->execute();
        $stmt->bind_result($price);
	$total = 0;
	while($stmt->fetch())
		$total += $price;

	echo $total;





?>
