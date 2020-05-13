<?php
        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);

	$id = $_REQUEST['show_instances'];
	if ($_POST['edit']){
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

		$stmt = $con->prepare("UPDATE showInstance SET startDate = ?, endDate = ?, bandAPrices = ?, bandBPrices = ?, bandCPrices = ?, bandDPrices = ?, bandANumTickets= ?, bandBNumTickets= ?,bandCNumTickets= ?, bandDNumTickets= ?, mondayMat= ?, mondayEve= ?, tuesdayMat= ?, tuesdayEve= ?, wednesdayMat= ?, wednesdayEve= ?, thursdayMat= ?, thursdayEve= ?, fridayMat= ?, fridayEve= ?, saturdayMat= ?, saturdayEve= ?, sundayMat= ?, sundayEve = ?, matineeTime = ?, eveningTime = ? WHERE showInstanceID = ?");
		$stmt->bind_param("ssiiiiiiiiiiiiiiiiiiiiiissi", $startdate, $enddate, $bandaprices, $bandbprices, $bandcprices, $banddprices, $bandanumseats, $bandbnumseats, $bandcnumseats, $banddnumseats, $mondaymat, $mondayeve, $tuesdaymat, $tuesdayeve, $wednesdaymat, $wednesdayeve, $thursdaymat, $thursdayeve, $fridaymat, $fridayeve, $saturdaymat, $saturdayeve, $sundaymat, $sundayeve, $matineestart, $eveningstart, $id);
		if ($stmt->execute()){
	                echo "Instance successfully updated";
	                header('Refresh: 2; URL = homepage.php');
	        }else{
	                echo "Error:  " .$stmt->error;
	                echo "<br><br><a href='/homepage.php'>Home</a>";
	        }
	} else if ($_POST['delete']){

                $stmt = $con->prepare("DELETE FROM showInstance WHERE showInstanceID = ?");
                $stmt->bind_param("i", $id);
                if ($stmt->execute()){
                        echo "Show instance successfully deleted";
                        header('Refresh: 2; URL = homepage.php');
                }else{
                        echo "Error:  " .$stmt->error;
                        echo "<br><br><a href='/homepage.php'>Home</a>";
                }


        }



?>
