<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


        $venueName = $_GET['q'];

        $stmt = $con->prepare("SELECT capacity, venueDesc, postcode, city, region FROM venue WHERE venueName = ?");
        $stmt->bind_param("s", $venueName);
        $stmt->execute();
        $stmt->bind_result($capacity, $description, $postcode, $city, $region);
        $stmt->fetch();
        $venue = array();
        $venue['description'] = $description;
        $venue['region'] = $region;
        $venue['city'] = $city;
	$venue['postcode'] = $postcode;
	$venue['capacity'] = $capacity;
        $venue = json_encode($venue);
        echo $venue;

?>






