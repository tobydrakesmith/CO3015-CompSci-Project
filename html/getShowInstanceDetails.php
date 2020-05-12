<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


        $venueName = $_GET['v'];
	$showName = $_GET['s'];

	$stmt = $con->prepare("SELECT startDate, endDate, bandAPrices, bandBPrices, bandCPrices, bandDPrices, bandANumTickets, bandBNumTickets, bandCNumTickets, bandDNumTickets, mondayMat, mondayEve, tuesdayMat, tuesdayEve, wednesdayMat, wednesdayEve, thursdayMat, thursdayEve, fridayMat, fridayEve, saturdayMat, saturdayEve, sundayMat, sundayEve, matineeTime, eveningTime FROM showInstance WHERE venueName = ? AND showName = ? AND endDate >= NOW()");
	$stmt->bind_param("ss", $venueName, $showName);
	$stmt->execute();
	$stmt->bind_result($startDate, $endDate, $bandAPrices, $bandBPrices, $bandCPrices, $bandDPrices, $bandANumTickets, $bandBNumTickets, $bandCNumTickets, $bandDNumTickets, $mondayMat, $mondayEve, $tuesdayMat, $tuesdayEve, $wednesdayMat, $wednesdayEve, $thursdayMat, $thursdayEve, $fridayMat, $fridayEve, $saturdayMat, $saturdayEve, $sundayMat, $sundayEve, $matineeTime, $eveningTime);
	$stmt->fetch();
	$show = array();
	$show['startDate'] = $startDate;
	$show['endDate'] = $endDate;
	$show['bandAPrices'] = $bandAPrices;
	$show['bandBPrices'] = $bandBPrices;
	$show['bandCPrices'] = $bandCPrices;
	$show['bandDPrices'] = $bandDPrices;
	$show['bandANumTickets'] = $bandANumTickets;
	$show['bandBNumTickets'] = $bandBNumTickets;
	$show['bandCNumTickets'] = $bandCNumTickets;
	$show['bandDNumTickets'] = $bandDNumTickets;
	$show['mondayMat'] = $mondayMat;
	$show['mondayEve'] = $mondayEve;
	$show['tuesdayMat'] = $tuesdayMat;
	$show['tuesdayEve'] = $tuesdayEve;
	$show['wednesdayMat'] = $wednesdayMat;
	$show['wednesdayEve'] = $wednesdayEve;
	$show['thursdayMat'] = $thursdayMat;
	$show['thursdayEve'] = $thursdayEve;
	$show['fridayMat'] = $fridayMat;
	$show['fridayEve'] = $fridayEve;
	$show['saturdayMat'] = $saturdayMat;
	$show['saturdayEve'] = $saturdayEve;
	$show['sundayMat'] = $sundayMat;
	$show['sundayEve'] = $sundayEve;
	$show['matTime'] = $matineeTime;
	$show['eveTime'] = $eveningTime;

	$show = json_encode($show);
	echo $show;

?>