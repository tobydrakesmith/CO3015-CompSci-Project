<?php

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


        $showName = $_GET['q'];

        $stmt = $con->prepare("SELECT showDescription, runningTime, classification FROM shows WHERE showName = ?");
        $stmt->bind_param("s", $showName);
        $stmt->execute();
        $stmt->bind_result($desc, $runningTime, $classification);
        $stmt->fetch();
	$show = array();
	$show['description'] = $desc;
	$show['runningTime'] = $runningTime;
	$show['classification'] = $classification;
        $show = json_encode($show);
	echo $show;

?>

