<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


	$venueName = $_GET['venueName'];

	$stmt = $con->prepare("SELECT region FROM venue WHERE venueName = ?");
	$stmt->bind_param("s", $venueName);
	$stmt->execute();
	$stmt->bind_result($region);
	$stmt->fetch();

	switch($region){

		case 'Scotland':

			echo 'newShowScotland';
		break;

		case 'Northern Ireland':

			echo 'newShowNorthernIreland';

		break;

		case 'Wales':

			echo 'newShowWales';

		break;

		case 'North East':

			echo 'newShowNorthEast';

		break;

		case 'North West':
			echo 'newShowNorthWest';
		break;

		case 'Yorkshire':
			echo 'newShowYorkshire';
		break;

		case 'West Midlands':
			echo 'newShowWestMidlands';

		break;

		case 'East Midlands':
			echo 'newShowEastMidlands';
		break;

		case 'South West':
			echo 'newShowSouthWest';
		break;

		case 'South East':
			echo 'newShowSouthEast';
		break;

		case 'East of England':
			echo 'newShowEastOfEngland';
		break;

		case 'Greater London':
			echo 'newShowLondon';
		break;


	}


?>

