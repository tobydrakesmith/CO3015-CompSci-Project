<?php
        session_start();
        if($_SESSION['valid']);
        else die('nah m8');

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

<html>
<head>
<title>Edit show instance</title>
<script>
        function showInstances(){
		var showName = document.getElementById("show_name").value;
		var venueName = document.getElementById("venue_name").value;
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                        if (this.readyState == 4 && this.status == 200){
				var show = JSON.parse(this.responseText);

				var startDate = show.startDate;
				document.getElementById("start_date").value = startDate;
				var endDate = show.endDate;
                                document.getElementById("end_date").value = endDate;

				var bandAPrices = show.bandAPrices;
				var bandBPrices = show.bandBPrices;
				var bandCPrices = show.bandCPrices;
				var bandDPrices = show.bandDPrices;

				document.getElementById("banda_prices").value = bandAPrices;
				document.getElementById("bandb_prices").value = bandBPrices;
				document.getElementById("bandc_prices").value = bandCPrices;
				document.getElementById("bandd_prices").value = bandDPrices;

				var bandANumSeats = show.bandANumTickets;
				var bandBNumSeats = show.bandBNumTickets;
				var bandCNumSeats = show.bandCNumTickets;
				var bandDNumSeats = show.bandDNumTickets;

				document.getElementById("banda_numseats").value = bandANumSeats;
				document.getElementById("bandb_numseats").value = bandBNumSeats;
				document.getElementById("bandc_numseats").value = bandCNumSeats;
				document.getElementById("bandd_numseats").value = bandDNumSeats;

				var mondayMat = show.mondayMat;
				var mondayEve = show.mondayEve;

				var tuesdayMat = show.tuesdayMat;
				var tuesdayEve = show.tuesdayEve;

				var wednesdayMat = show.wednesdayMat;
				var wednesdayEve = show.wednesdayEve;

				var thursdayMat = show.thursdayMat;
				var thursdayEve = show.thursdayEve;

				var fridayMat = show.fridayMat;
				var fridayEve = show.fridayEve;

				var saturdayMat = show.saturdayMat;
				var saturdayEve = show.saturdayEve;

				var sundayMat = show.sundayMat;
				var sundayEve = show.sundayEve;

				document.getElementById("mondaymat").checked = mondayMat == 1;
                                document.getElementById("mondayeve").checked = mondayEve == 1;

                                document.getElementById("tuesdaymat").checked = tuesdayMat == 1;
                                document.getElementById("tuesdayeve").checked = tuesdayEve == 1;

                                document.getElementById("wednesdaymat").checked = wednesdayMat == 1;
                                document.getElementById("wednesdayeve").checked = wednesdayEve == 1;

                                document.getElementById("thursdaymat").checked = thursdayMat == 1;
                                document.getElementById("thursdayeve").checked = thursdayEve == 1;

                                document.getElementById("fridaymat").checked = fridayMat == 1;
                                document.getElementById("fridayeve").checked = fridayEve == 1;

                                document.getElementById("saturdaymat").checked = saturdayMat == 1;
                                document.getElementById("saturdayeve").checked = saturdayEve == 1;

                                document.getElementById("sundaymat").checked = sundayMat == 1;
                                document.getElementById("sundayeve").checked = sundayEve == 1;

				var matTime = show.matTime;
				var eveTime = show.eveTime;


				document.getElementById("mat_start").value = matTime;
				document.getElementById("eve_start").value = eveTime;
	             }
                };

                xmlhttp.open("GET", "getShowInstanceDetails.php?s="+showName+"&v="+venueName, true);
                xmlhttp.send();

        }
</script>
</head>
<body>
<form name="form" action="editShowInstanceSubmit.php" class="alt" method="POST">

        <p>
                <label for="show_name">Show name:</label>
                <select name="show_name" id="show_name" onchange="showInstances()">
                <option value="">Select a show</option>
                <?php
                        populateShowList();
                ?>
                </select>
        </p>

        <p>
                <label for="venue_name">Venue name:</label>
                <select name="venue_name" id="venue_name" onchange="showInstances()">
                <option value="">Select a venue</option>
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
                <input type="number" value = 0 name="banda_numseats" id="banda_numseats">
        </p>

        <p>
                <label for="bandb_prices">Price of seats PBB:</label>
                <input type="text" name="bandb_prices" id="bandb_prices">

                <label for="bandb_numSeats">Number of seats PBB:</label>
                <input type="number" value=0 name="bandb_numseats" id="bandb_numseats">

        </p>



        <p>
                <label for="bandc_prices">Price of seats PBC:</label>
                <input type="text" name="bandc_prices" id="bandc_prices">

                <label for="bandc_numSeats">Number of seats PBC:</label>
                <input type="number" value=0 name="bandc_numseats" id="bandc_numseats">
        </p>

        <p>
                <label for="bandd_prices">Price of seats PBD:</label>
                <input type="text" name="bandd_prices" id="bandd_prices">

                <label for="bandd_numSeats">Number of seats PBD:</label>
                <input type="number" value=0 name="bandd_numseats" id="bandd_numseats">
        </p>

        Monday
        &nbsp;&nbsp;&nbsp;&nbsp;Tuesday
        &nbsp;&nbsp;&nbsp;&nbsp;Wednesday
        &nbsp;&nbsp;&nbsp;&nbsp;Thursday
        &nbsp;&nbsp;&nbsp;&nbsp;Friday
        &nbsp;&nbsp;&nbsp;&nbsp;Saturday
        &nbsp;&nbsp;&nbsp;&nbsp;Sunday


        <br>

        <input type="checkbox" id="mondaymat" name="mondaymat">Matinee
        <input type="checkbox" id="tuesdaymat" name="tuesdaymat">Matinee
        <input type="checkbox" id="wednesdaymat" name="wednesdaymat">Matinee
        <input type="checkbox" id="thursdaymat" name="thursdaymat">Matinee
        <input type="checkbox" id="fridaymat" name="fridaymat">Matinee
        <input type="checkbox" id="saturdaymat" name="saturdaymat">Matinee
        <input type="checkbox" id="sundaymat" name="sundaymat">Matinee

        <br>

        <input type="checkbox" id="mondayeve" name="mondayeve">Evening
        <input type="checkbox" id="tuesdayeve" name="tuesdayeve">Evening
        <input type="checkbox" id="wednesdayeve" name="wednesdayeve">Evening
        <input type="checkbox" id="thursdayeve" name="thursdayeve">Evening
        <input type="checkbox" id="fridayeve" name="fridayeve">Evening
        <input type="checkbox" id="saturdayeve" name="saturdayeve">Evening
        <input type="checkbox" id="sundayeve" name="sundayeve">Evening

        <br>

        <label for="mat_start">Matinee start time:</label>

        <input type="time" id="mat_start" name="mat_start" min="11:00" max="16:00" required>

        <label for="eve_start">Evening start time:</label>
        <input type="time" id="eve_start" name="eve_start" min="18:00" max="22:00" required>

	<br> <br>

	<button>Submit</button>
	<br>
</form>
<a href="homepage.php"><button>Home</button></a>
</body>
</html>

