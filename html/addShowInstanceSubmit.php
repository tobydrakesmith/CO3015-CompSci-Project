<?php

	include_once dirname(__FILE__) . '/constants.php';

	$con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

	if($con === false)
		die("ERROR: could not connect. " . $this->connect_error);


	$showname = $_REQUEST['show_name'];
	$venuename = $_REQUEST['venue_name'];
	$bandaprices = $con->real_escape_string($_REQUEST['banda_prices']);
	$bandanumseats = $con->real_escape_string($_REQUEST['banda_numseats']);
	$bandbprices = $con->real_escape_string($_REQUEST['bandb_prices']);
	$bandbnumseats = $con->real_escape_string($_REQUEST['bandb_numseats']);
	$bandcprices = $con->real_escape_string($_REQUEST['bandc_prices']);
	$bandcnumseats = $con->real_escape_string($_REQUEST['bandc_numseats']);
	$banddprices = $con->real_escape_string($_REQUEST['bandd_prices']);
	$banddnumseats = $con->real_escape_string($_REQUEST['bandd_numseats']);

	$startdate = str_replace('/','-',$_REQUEST['start_date']);
	$enddate = str_replace('/','-',$_REQUEST['end_date']);

	/*
	$stmt = $con->prepare("SELECT * FROM showInstance WHERE venueName = ? AND endDate > ?");
	$stmt->bind_param("ss", $venuename, $startdate);
	$stmt->execute();
	while($stmt->fetch()){}
	//if ($stmt->num_rows > 0) die("show already at venue");
	if($stmt->num_rows > 0){
		$message = "wrong answer";
		echo
		"<script type='text/javascript'>
			if(confirm('$message')){
				var ok = true;
			}else{
				var ok = false;
			}
		</script>";
	}

	$ok = $_GET['ok'];
	*/
	if($_REQUEST['mondaymat'] === on)
		$mondaymat = 1;
	else
		$mondaymat = 0;

        if($_REQUEST['mondayeve'] === on)
                $mondayeve = 1;
        else
                $mondayeve = 0;

        if($_REQUEST['tuesdaymat'] === on)
                $tuesdaymat = 1;
        else
                $tuesdaymat = 0;

        if($_REQUEST['tuesdayeve'] === on)
                $tuesdayeve = 1;
        else
                $tuesdayeve = 0;

        if($_REQUEST['wednesdaymat'] === on)
                $wednesdaymat = 1;
        else
                $wednesdaymat = 0;

        if($_REQUEST['wednesdayeve'] === on)
                $wednesdayeve = 1;
        else
                $wednesdayeve = 0;

        if($_REQUEST['thursdaymat'] === on)
                $thursdaymat = 1;
        else
                $thursdaymat = 0;

        if($_REQUEST['thursdayeve'] === on)
                $thursdayeve = 1;
        else
                $thursdayeve = 0;

        if($_REQUEST['fridaymat'] === on)
                $fridaymat = 1;
        else
                $fridaymat = 0;

        if($_REQUEST['fridayeve'] === on)
                $fridayeve = 1;
        else
                $fridayeve = 0;

        if($_REQUEST['saturdaymat'] === on)
                $saturdaymat = 1;
        else
                $saturdaymat = 0;

        if($_REQUEST['saturdayeve'] === on)
                $saturdayeve = 1;
        else
                $saturdayeve = 0;

        if($_REQUEST['sundaymat'] === on)
                $sundaymat = 1;
        else
                $sundaymat = 0;

        if($_REQUEST['sundayeve'] === on)
                $sundayeve = 1;
        else
                $sundayeve = 0;


	$matineestart = $_REQUEST['mat_start'];
	$eveningstart = $_REQUEST['eve_start'];

	$query = "INSERT INTO showInstance(showName, venueName, startDate, endDate, bandAPrices, bandBPrices, bandCPrices, bandDPrices, bandANumTickets, bandBNumTickets, bandCNumTickets, bandDNumTickets, mondayMat, mondayEve, tuesdayMat, tuesdayEve, wednesdayMat, wednesdayEve, thursdayMat, thursdayEve, fridayMat, fridayEve, saturdayMat, saturdayEve, sundayMat, sundayEve, matineeTime, eveningTime) VALUES('$showname', '$venuename', '$startdate', '$enddate', '$bandaprices', '$bandbprices', '$bandcprices', '$banddprices', '$bandanumseats', '$bandbnumseats', '$bandcnumseats', '$banddnumseats', '$mondaymat', '$mondayeve', '$tuesdaymat', '$tuesdayeve', '$wednesdaymat', '$wednesdayeve', '$thursdaymat', '$thursdayeve', '$fridaymat', '$fridayeve', '$saturdaymat', '$saturdayeve', '$sundaymat', '$sundayeve', '$matineestart', '$eveningstart')";

	if($con->query($query))
		echo "Success";
	else
		echo "Error:  " .$con->error;


$con->close();
?>
