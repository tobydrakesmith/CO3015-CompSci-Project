<?php
        session_start();
	if(!$_SESSION['valid']) die('Access denied. Please log on');

        function populateVenueList(){
                include_once dirname(__FILE__) . '/constants.php';
                $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME) or die ('Cannot connect to db');

                $result = $con->query("SELECT venueName FROM venue ");

                while ($row = $result->fetch_assoc()) {
                        $venueName = $row['venueName'];
                        echo '<option value="'.$venueName.'">'.$venueName.'</option>';
                }
        }

?>

<html>
<head>
<title>View Sales</title>
<script>
        function getShowInstances(venueName){

                var select = document.getElementById("show_instances");
                var l = select.options.length - 1;

                for(var i = l; i>0; i--){
                        select.remove(i);
		}
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                        if (this.readyState == 4 && this.status == 200){
                                var shows = JSON.parse(this.responseText);
                                for (x in shows){
                                        var show = shows[x];
                                        var el = document.createElement("option");
                                        el.textContent = show.name + " " + show.startDate + " - " + show.endDate;
                                        el.value = show.id;
                                        select.appendChild(el);
                                }
                        }
                };

                xmlhttp.open("GET", "getShowInstances.php?q="+venueName, true);
                xmlhttp.send();


        }

	function getSales(){

		var id = document.getElementById("show_instances").value;
		var from = document.getElementById("start_date").value;
		var to = document.getElementById("end_date").value;

                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                        if (this.readyState == 4 && this.status == 200){
				document.getElementById("sales").innerHTML = "Total sales for this period: £"+this.responseText;
                        }
                };

                xmlhttp.open("GET", "getSales.php?i="+id+"&f="+from+"&t="+to, true);
                xmlhttp.send();

	}

</script>
</head>
<body>


        <p>
                <label for="venue_name">Venue name:</label>
                <select name="venue_name" id="venue_name" onchange="getShowInstances(this.value)">
                <option value="">Select a venue</option>
                <?php
                        populateVenueList();
                ?>
                </select>
        </p>

        <p>
                <select name="show_instances" id="show_instances">
                        <option>Select a show instance</option>
                </select>
        </p>


        <p>
                <label for="start_date">From:</label>
                <input type="date" name="start_date" id="start_date">
        </p>

        <p>
                <label for="end_date">To:</label>
                <input type="date" name="end_date" id="end_date">
        </p>

	<button onclick="getSales()">Get sales</button>

	<br>
	<p>
	<div id="sales"></div>
	</p>

	<br>
	<br>
	<a href="homepage.php"><button>Home</button></a>
</body>
</html>

