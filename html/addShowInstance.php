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
</head>

<body>
<form action="addShowInstanceSubmit.php" class="alt" method="POST">

	<p>
		<label for="show_name">Show name:</label>
		<select name="show_name">
		<?php
			populateShowList();
		?>
		</select>
	</p>

	<p>
                <label for="venue_name">Venue name:</label>
                <select name="venue_name">
		<?php
			populateVenueList();
		?>
		</select>
        </p>

	<p>
                <label for="start_date">Start date:</label>
                <input type="date" name="start_date" id="start_date">
        </p>


	<p>
                <label for="end_date">End date:</label>
                <input type="date" name="end_date" id="end_date">
        </p>

	<p>
                <label for="banda_prices">Price of seats PBA:</label>
                <input type="text" name="banda_prices" id="banda_prices">

		<label for="banda_numSeats">Number of seats PBA:</label>
                <input type="text" name="banda_numseats" id="banda_numseats">

	</p>

	<p>
                <label for="bandb_prices">Price of seats PBB:</label>
                <input type="text" name="bandb_prices" id="bandb_prices">

		<label for="bandb_numSeats">Number of seats PBB:</label>
                <input type="text" name="bandb_numseats" id="bandb_numseats">

	</p>



	<p>
                <label for="bandc_prices">Price of seats PBC:</label>
                <input type="text" name="bandc_prices" id="bandc_prices">

		<label for="bandc_numSeats">Number of seats PBC:</label>
                <input type="text" name="bandc_numseats" id="bandc_numseats">
	</p>

	<p>
        	<label for="bandd_prices">Price of seats PBD:</label>
                <input type="text" name="bandd_prices" id="bandd_prices">

		<label for="bandd_numSeats">Number of seats PBD:</label>
		<input type="text" name="bandd_numseats" id="bandd_numseats">
        </p>



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
	<input type="checkbox" name="sundaymat" id="sundaymat">Matinee
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

	<input type="time" id="mat_start" name="mat_start" min="11:00" max="17:00" required>

	<label for="eve_start">Evening start time:</label>
	<input type="time" id="eve_start" name="eve_start" min="18:00" max="22:00" required>


	<br>
	<input type="submit">


</form>
<br>
<br>
<a href="homepage.html"><button>Home</button></a>
</body>
</html>
