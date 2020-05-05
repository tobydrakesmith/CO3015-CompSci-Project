<?php

	function populateVenueList(){
	        include_once dirname(__FILE__) . '/constants.php';
	        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME) or die ('Cannot connect to db');

		$result = $con->query("SELECT venueName FROM venue ");

                while ($row = $result->fetch_assoc()) {
                        $venueName = $row['venueName'];
                        echo '<option value="'.$venueName.'">'.$venueName.'</option>';
                }
	}


	function populateShowList(){

	        include_once dirname(__FILE__) . '/constants.php';

	        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME) or die ('Cannot connect to db');

		$result = $con->query("SELECT showName FROM shows");

		while ($row = $result->fetch_assoc()) {
			$showName = $row['showName'];
			echo '<option value="'.$showName.'">'.$showName.'</option>';
		}
	}

?>
<head>
<title>Add show instance</title>
<script>

var venueCapacity;

function showVenueInfo(venue) {
	dateChange();
	numberTicketsChange();
	xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200){
			document.getElementById("venue_capacity").innerHTML = "<b>Venue capacity:</b> "  + this.responseText;
			venueCapacity = this.responseText;
			numberTicketsChange();
		}
	};

	xmlhttp.open("GET", "getVenueInfo.php?q="+venue, true);
	xmlhttp.send();

	getVenueRegion(venue);
}

function dateChange(name) {
	var venueName = document.forms["form"]["venue_name"].value;
	var startDate = document.forms["form"]["start_date"].value;
	var endDate = document.forms["form"]["end_date"].value;

	if (startDate && endDate){
		xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200){
				document.getElementById("start_date_check").innerHTML = this.responseText;
			}
		};

		xmlhttp.open("GET", "getStartDateCheck.php?startDate="+startDate+"&endDate="+endDate+"&venueName="+venueName, true);
		xmlhttp.send();
	}

	if (name == "start_date"){
		document.forms["form"]["end_date"].setAttribute("min", startDate);
	}else{
		document.forms["form"]["start_date"].setAttribute("max", endDate);
	}

}

function numberTicketsChange(){

	var count = 0;

	var a = parseInt(document.forms["form"]["banda_numseats"].value);
	var b = parseInt(document.forms["form"]["bandb_numseats"].value);
	var c = parseInt(document.forms["form"]["bandc_numseats"].value);
	var d = parseInt(document.forms["form"]["bandd_numseats"].value);

	count = a + b + c + d;

	document.getElementById("number_tickets").innerHTML = "<b>Current count: </b>" + count;

	if (count > venueCapacity){
		document.getElementById("number_tickets").innerHTML = '<p style="color:red"><b> ERROR: You have selected more tickets than the capacity of the venue. Current count: </b>' + count +'</p>';
		document.forms["form"]["submit"].disabled = true;
	}else {
		document.forms["form"]["submit"].disabled = false;
	}
}

function showNameChange(showName){

	var showName = document.forms["form"]["show_name"].value;


	xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200){
			document.getElementById("running_time").innerHTML = '<b>Running time: </b>' + this.responseText;
		}
	};

	xmlhttp.open("GET", "getRunningTime.php?showName="+showName, true);
	xmlhttp.send();

	getShowType(showName);
}

function getShowType(showName){

        xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200){
			document.forms["form"]["show_type"].value = this.responseText;
			alert(this.responseText);
                }
        };

        xmlhttp.open("GET", "getShowType.php?showName="+showName, true);
        xmlhttp.send();

}

function getVenueRegion(venueName){
        xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200){
			document.forms["form"]["venue_region"].value = this.responseText;
			alert(this.responseText);
                }
        };

        xmlhttp.open("GET", "getVenueRegion.php?venueName="+venueName, true);
        xmlhttp.send();

}

</script>
</head>

<body>
<form name="form" action="addShowInstanceSubmit.php" class="alt" method="POST">

	<p>
		<label for="show_name">Show name:</label>
		<select name="show_name" onchange="showNameChange(this.value)">
		<option value="">Select a show</option>
		<?php
			populateShowList();
		?>
		</select>
	</p>

	<p>
                <label for="venue_name">Venue name:</label>
                <select name="venue_name" onchange="showVenueInfo(this.value)">
		<option value="">Select a venue</option>
		<?php
			populateVenueList();
		?>
		</select>
        </p>

	<p>
                <label for="start_date">Start date:</label>
                <input type="date" onchange="dateChange(this.name)" name="start_date" id="start_date">
        </p>
	<div id="start_date_check"></div>

	<p>
                <label for="end_date">End date:</label>
                <input type="date" onchange="dateChange(this.name)" name="end_date" id="end_date">
        </p>


	<p>
                <label for="banda_prices">Price of seats PBA:</label>
                <input type="text" name="banda_prices" id="banda_prices">

		<label for="banda_numSeats">Number of seats PBA:</label>
		<input type="number" value = 0 onchange="numberTicketsChange()" name="banda_numseats" id="banda_numseats">
	</p>

	<p>
                <label for="bandb_prices">Price of seats PBB:</label>
                <input type="text" name="bandb_prices" id="bandb_prices">

		<label for="bandb_numSeats">Number of seats PBB:</label>
                <input type="number" value=0 onchange="numberTicketsChange()" name="bandb_numseats" id="bandb_numseats">

	</p>



	<p>
                <label for="bandc_prices">Price of seats PBC:</label>
                <input type="text" name="bandc_prices" id="bandc_prices">

		<label for="bandc_numSeats">Number of seats PBC:</label>
                <input type="number" value=0 onchange="numberTicketsChange()" name="bandc_numseats" id="bandc_numseats">
	</p>

	<p>
        	<label for="bandd_prices">Price of seats PBD:</label>
                <input type="text" name="bandd_prices" id="bandd_prices">

		<label for="bandd_numSeats">Number of seats PBD:</label>
		<input type="number" value=0 onchange="numberTicketsChange()" name="bandd_numseats" id="bandd_numseats">
        </p>


        <div id="venue_capacity"></div>
	<div id="number_tickets"></div>


	Monday
	&nbsp;&nbsp;&nbsp;&nbsp;Tuesday
	&nbsp;&nbsp;&nbsp;&nbsp;Wednesday
	&nbsp;&nbsp;&nbsp;&nbsp;Thursday
	&nbsp;&nbsp;&nbsp;&nbsp;Friday
	&nbsp;&nbsp;&nbsp;&nbsp;Saturday
	&nbsp;&nbsp;&nbsp;&nbsp;Sunday


	<br>

	<input type="checkbox" name="mondaymat">Matinee
	<input type="checkbox" name="tuesdaymat">Matinee
	<input type="checkbox" name="wednesdaymat">Matinee
	<input type="checkbox" name="thursdaymat">Matinee
	<input type="checkbox" name="fridaymat">Matinee
	<input type="checkbox" name="saturdaymat">Matinee
	<input type="checkbox" name="sundaymat">Matinee
	<br>

	<input type="checkbox" name="mondayeve">Evening
	<input type="checkbox" name="tuesdayeve">Evening
	<input type="checkbox" name="wednesdayeve">Evening
	<input type="checkbox" name="thursdayeve">Evening
	<input type="checkbox" name="fridayeve">Evening
	<input type="checkbox" name="saturdayeve">Evening
	<input type="checkbox" name="sundayeve">Evening

	<br>

	<label for="mat_start">Matinee start time:</label>

	<input type="time" id="mat_start" name="mat_start" min="11:00" max="16:00" required>

	<label for="eve_start">Evening start time:</label>
	<input type="time" id="eve_start" name="eve_start" min="18:00" max="22:00" required>

	<div id="running_time"></div>

	<br>


	<input type="text" name="venue_region" id="venue_region" readonly>
	<input type="text" name="show_type" id="show_type" readonly>
	<br>
	<br>

	<input type="submit" id="submit">



</form>
<br>
<a href="homepage.html"><button>Home</button></a>
</body>
</html>
