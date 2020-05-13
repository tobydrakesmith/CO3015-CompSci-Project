<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);


        if($con === false){
                die("ERROR: Could not connect. " . $this->connect_error);
        }

	$showname = $con->real_escape_string($_REQUEST['show_name']);

	if ($_POST['edit']){

		$newname = $con->real_escape_string($_REQUEST['showName']);
	        $showdesc = $con->real_escape_string($_REQUEST['show_description']);
	        $runningtime = $con->real_escape_string($_REQUEST['running_time']);
	        $playmusical = $_REQUEST['classification'];

	        $stmt = $con->prepare("UPDATE shows SET showName = ?, showDescription = ?, runningTime = ?, classification = ? WHERE showName = ?");
		$stmt->bind_param("ssiss", $newname, $showdesc, $runningtime, $playmusical, $showname);
	        if ($stmt->execute()){
	                echo "Show successfully updated";
	                header('Refresh: 2; URL = homepage.php');
	        }else{
	                echo "Error:  " .$stmt->error;
	                echo "<br><br><a href='/homepage.php'>Home</a>";
	        }
	} else if ($_POST['delete']){

		$stmt = $con->prepare("DELETE FROM shows WHERE showName = ?");
		$stmt->bind_param("s", $showname);
                if ($stmt->execute()){
                        echo "Show successfully deleted";
                        header('Refresh: 2; URL = homepage.php');
                }else{
                        echo "Error:  " .$stmt->error;
                        echo "<br><br><a href='/homepage.php'>Home</a>";
                }


	}


        $con->close();
?>


