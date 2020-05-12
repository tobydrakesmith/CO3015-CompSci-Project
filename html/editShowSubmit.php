<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);


        if($con === false){
                die("ERROR: Could not connect. " . $this->connect_error);
        }


        $showname = $con->real_escape_string($_REQUEST['show_name']);
        $showdesc = $con->real_escape_string($_REQUEST['show_description']);
        $runningtime = $con->real_escape_string($_REQUEST['running_time']);
        $playmusical = $_REQUEST['classification'];

        $stmt = $con->prepare("UPDATE shows SET showDescription = ?, runningTime = ?, classification = ? WHERE showName = ?");
	$stmt->bind_param("siss", $showdesc, $runningtime, $playmusical, $showname);
	if ($stmt->execute()) echo "Success";
	else echo $con->error;


        $con->close();
?>

