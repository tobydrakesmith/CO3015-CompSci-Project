<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false){

                die("ERROR: Could not connect. " .$this->connect_error);
        }

	$venuename = $con->real_escape_string($_REQUEST['venue_name']);

        if ($_POST['edit']){
		$newname = $con->real_escape_string($_REQUEST['venueName']);
	        $venuedesc = $con->real_escape_string($_REQUEST['venue_description']);
	        $numseats = $con->real_escape_string($_REQUEST['venue_noseats']);
	        $postcode = $con->real_escape_string($_REQUEST['venue_postcode']);
	        $city = $con->real_escape_string($_REQUEST['venue_city']);
	        $region = $_REQUEST['venue_region'];

	        $stmt = $con->prepare("UPDATE venue SET venueName = ?, venueDesc = ?, capacity = ?, postcode = ?, city = ?, region = ?	WHERE venueName = ?");
		$stmt->bind_param("ssissss", $newname, $venuedesc, $numseats, $postcode, $city, $region, $venuename);

	        if ($stmt->execute()){
	                echo "Venue successfully updated";
	                header('Refresh: 2; URL = homepage.php');
	        }else{
	                echo "Error:  " .$stmt->error;
	                echo "<br><br><a href='/homepage.php'>Home</a>";
	        }


	} else if ($_POST['delete']){

		$stmt = $con->prepare("DELETE FROM venue WHERE venueName = ?");
		$stmt->bind_param("s", $venuename);
                if ($stmt->execute()){
                        echo "Venue successfully deleted";
                        header('Refresh: 2; URL = homepage.php');
                }else{
                        echo "Error:  " .$stmt->error;
                        echo "<br><br><a href='/homepage.php'>Home</a>";
                }



	}

        $con->close();


?>

