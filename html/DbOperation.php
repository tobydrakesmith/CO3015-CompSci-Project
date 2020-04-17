<?php

	class DbOperation{

		private $con;

		function __construct(){
			//Getting the DbConnect.php file
			require_once dirname(__FILE__) . '/DbConnect.php';

			//Creating a DbConnect object to connect to the database
			$db = new DbConnect();

			//Initializing our connection link of this class
			//by calling the method connect of DbConnect class
			$this->con = $db->connect();
		}

		/*
		* The create operation
		* When this method is called a new record is created in the database
		*/
		function createUser($email, $firstName, $lastName, $password){
			$stmt = $this->con->prepare("INSERT INTO user (email, firstName, lastName, password) VALUES (?, ?, ?, ?)");
			$stmt->bind_param("ssss", $email, $firstName, $lastName, $password);

			if($stmt->execute())
				return true;
			return false;
		}

		/*
		* The read operation
		* When this method is called it is returning all the existing record of the database
		*/

		function getUserDetails($email){
			$stmt = $this->con->prepare("SELECT userID, firstName, lastName FROM user WHERE email = ? ");
			$stmt->bind_param("s", $email);
			$stmt->execute();
			$stmt->bind_result($userID, $firstName, $lastName);
			$stmt->fetch();

			$user = array();
			$user['userID'] = $userID;
			$user['firstName'] = $firstName;
			$user['lastName'] = $lastName;

			return $user;

		}


		function getUsers(){
			$stmt = $this->con->prepare("SELECT userID, email, firstName, lastName, password FROM user");
			$stmt->execute();
			$stmt->bind_result($userID, $email, $firstName, $lastName, $password);

			$users = array();

			while($stmt->fetch()){
				$user  = array();
				$user['userID'] = $userID;
				$user['email'] = $email;
				$user['firstName'] = $firstName;
				$user['lastName'] = $lastName;
				$user['password'] = $password;

				array_push($users, $user);
			}

			return $users;
		}

		function getUser($email, $password){
			$stmt = $this->con->prepare("SELECT email, password FROM user WHERE email = ? AND password = ? ");
			$stmt->bind_param("ss", $email, $password);
			$stmt->execute();
			$stmt->bind_result($email, $password);
			if($stmt->fetch())
				return true;
			return false;
		}

		function checkPassword($userID, $password){
			$stmt = $this->con->prepare("SELECT password FROM user WHERE userID = ?");
			$stmt->bind_param("i", $userID);
			$stmt->execute();
			$stmt->bind_result($dbPass);
			$stmt->Fetch();
			return $dbPass == $password;
		}

		/*
		* The update operation
		* When this method is called the record with the given id is updated with the new given values
		*/
		function updateUser($userID, $email, $firstName, $lastName, $password){
			$stmt = $this->con->prepare("UPDATE user SET email = ?, firstName = ?, lastName = ?, password = ? WHERE userID = ?");
			$stmt->bind_param("ssssi", $email, $firstName, $lastName, $password, $userID);
			if($stmt->execute())
				return true;
			return false;
		}
		/*
		* The delete operation
		* When this method is called record is deleted for the given id
		*/
		function deleteUser($userID){
			$stmt = $this->con->prepare("DELETE FROM user WHERE userID = ? ");
			$stmt->bind_param("i", $userID);
			if($stmt->execute())
				return true;

			return false;
		}

		function getLiveShows(){
			$stmt = $this->con->prepare("SELECT * FROM showInstance WHERE startDate <= CURDATE() AND endDate > CURDATE()");
			$stmt->execute();
			$stmt->bind_result($id, $showName, $venueName, $startDate, $endDate, $rating, $bandAPrices, $bandBPrices, $bandCPrices, $bandDPrices, $bandANum, $bandBNum, $bandCNum, $bandDNum, $monMat, $monEve, $tueMat, $tueEve, $wedMat, $wedEve, $thuMat, $thuEve, $friMat, $friEve, $satMat, $satEve, $sunMat, $sunEve, $matTime, $eveTime);

                        $liveshows = array();

                        while($stmt->fetch()){
                                $liveshow  = array();
				$liveshow['id'] = $id;
                                $liveshow['showName'] = $showName;
                                $liveshow['venueName'] = $venueName;
                                $liveshow['startDate'] = $startDate;
                                $liveshow['endDate'] = $endDate;
				$liveshow['rating']= $rating;
				$liveshow['bandAPrice'] = $bandAPrices;
				$liveshow['bandBPrice'] = $bandBPrices;
				$liveshow['bandCPrice'] = $bandCPrices;
				$liveshow['bandDPrice'] = $bandDPrices;
				$liveshow['bandANumberOfTickets'] = $bandANum;
				$liveshow['bandBNumberOfTickets'] = $bandBNum;
				$liveshow['bandCNumberOfTickets'] = $bandCNum;
				$liveshow['bandDNumberOfTickets'] = $bandDNum;
				$liveshow['monMat'] = $monMat;
				$liveshow['monEve'] = $monEve;
				$liveshow['tueMat'] = $tueMat;
                                $liveshow['tueEve'] = $tueEve;
				$liveshow['wedMat'] = $wedMat;
                                $liveshow['wedEve'] = $wedEve;
                                $liveshow['thuMat'] = $thuMat;
                                $liveshow['thuEve'] = $thuEve;
                                $liveshow['friMat'] = $friMat;
                                $liveshow['friEve'] = $friEve;
                                $liveshow['satMat'] = $satMat;
                                $liveshow['satEve'] = $satEve;
                                $liveshow['sunMat'] = $sunMat;
                                $liveshow['sunEve'] = $sunEve;
				$liveshow['matTime'] = $matTime;
				$liveshow['eveTime'] = $eveTime;
				array_push($liveshows, $liveshow);
                        }

                        return $liveshows;


		}

		function getVenuePostcode($venueName){
			$stmt = $this->con->prepare("SELECT postcode FROM venue WHERE venueName = ?");
			$stmt->bind_param("s", $venueName);
			$stmt->execute();
			$stmt->bind_result($postcode);
			$stmt->fetch();
			return $postcode;
		}

		function getShowInfo($showName){

			if($stmt = $this->con->prepare("SELECT showDescription, runningTime FROM shows WHERE showName = ? ")){
				$stmt->bind_param("s", $showName);
				$stmt->execute();
				$stmt->bind_result($showdesc, $runningtime);

				while($stmt->fetch()){}
				$show = array();
				$show['showDesc'] = $showdesc;
				$show['runningTime'] = $runningtime;
				$show['rating'] = $this->getReviews($showName);
				return $show;
			}
			return false;
		}

		function createBooking($instanceID, $userID, $numberOfTickets, $date, $showTime, $showName, $tempID){
			$this->deleteTemp($tempID);
			$stmt = $this->con->prepare("INSERT INTO booking(showInstanceID, userID, numberOfTickets, bookingDate, showTime, showName) VALUES (?, ?, ?, ?, ?, ?)");
                       	$stmt->bind_param("iiisss", $instanceID, $userID, $numberOfTickets, $date, $showTime, $showName);
			if($stmt->execute())
				return mysqli_insert_id($this->con);
			else
				return $this->con->error;

		}

		function createTicket($bookingID, $price, $priceBand){
			$stmt = $this->con->prepare("INSERT INTO ticket(bookingID, price, priceBand) VALUES (?, ?, ?)");
			$stmt->bind_param("iis", $bookingID, $price, $priceBand);
			if($stmt->execute()) return true;
			else return false;
		}

		function getFutureBookings($userID){
			$stmt = $this->con->prepare("SELECT bookingID, showInstanceID, numberOfTickets, bookingDate, showTime, showName FROM booking WHERE userID= ? AND bookingDate >= CURDATE() ");
			$stmt->bind_param("i", $userID);
			$stmt->execute();
			$stmt->bind_result($bookingID, $showInstanceID, $numberOfTickets, $bookingDate, $showTime, $showName);

			$bookings = array();
			while($stmt->fetch()){
				$booking = array();
				$booking['bookingID'] = $bookingID;
				$booking['showInstanceID'] = $showInstanceID;
				$booking['numberOfTickets'] = $numberOfTickets;
				$booking['bookingDate'] = $bookingDate;
				$booking['showTime'] = $showTime;
				$booking['showName'] = $showName;

				array_push($bookings, $booking);
			}

			return $bookings;
		}

		function getPastBookings($userID){
	  		$stmt = $this->con->prepare("SELECT bookingID, showInstanceID, numberOfTickets, bookingDate, showTime, showName FROM booking WHERE userID= ? AND bookingDate < CURDATE() ");
	                $stmt->bind_param("i", $userID);
        	        $stmt->execute();
                        $stmt->bind_result($bookingID ,$showInstanceID, $numberOfTickets, $bookingDate, $showTime, $showName);

                        $bookings = array();
                        while($stmt->fetch()){
                                $booking = array();
				$booking['bookingID'] = $bookingID;
                                $booking['showInstanceID'] = $showInstanceID;
                                $booking['numberOfTickets'] = $numberOfTickets;
                                $booking['bookingDate'] = $bookingDate;
				$booking['showTime'] = $showTime;
                                $booking['showName'] = $showName;

                                array_push($bookings, $booking);
                        }

                        return $bookings;
		}

		function createReview($bookingID, $userID, $showName, $showInstanceID, $rating, $review){
			$stmt = $this->con->prepare("INSERT INTO review(bookingID, userID, showName, showInstanceID, rating, review) VALUES(?, ?, ?, ?, ?, ?)");
			$stmt->bind_param("iisiis", $bookingID, $userID, $showName, $showInstanceID, $rating, $review);
			if($stmt->execute()){
				$stmt = $this->con->prepare("UPDATE booking SET reviewLeft = 1 WHERE bookingID = ?");
				$stmt->bind_param("i", $bookingID);
				$stmt->execute();
				return true;
			}else
				return $this->con->error;
		}

		function getTickets($bookingID){
			$stmt = $this->con->prepare("SELECT ticketID, price, priceBand FROM ticket WHERE bookingID = ?");
			$stmt->bind_param("i", $bookingID);
			$stmt->execute();
			$stmt->bind_result($ticketID, $price, $priceBand);

			$tickets = array();
			while($stmt->fetch()){
				$ticket = array();
				$ticket['ticketID'] = $ticketID;
				$ticket['price'] = $price;
				$ticket['priceBand'] = $priceBand;
				array_push($tickets, $ticket);
			}

			return $tickets;
		}

		function updatePassword($password, $email){
			$stmt = $this->con->prepare("SELECT * FROM user WHERE email = ?");
			$stmt->bind_param("s", $email);
			$stmt->execute();
			while($stmt->fetch()){}
			if($stmt->num_rows == 0) return false;
			$stmt = $this->con->prepare("UPDATE user SET password = ? WHERE email = ?");
			$stmt->bind_param("ss", $password, $email);
			if ($stmt->execute()) return true;
			return false;
		}

		function updatePasswordLoggedOn($userID, $password){
			$stmt = $this->con->prepare("UPDATE user SET password = ? WHERE userID = ?");
			$stmt->bind_param("si", $password, $userID);
			return $stmt->execute();
		}

		function checkReview($bookingID){
			$stmt = $this->con->prepare("SELECT reviewLeft FROM booking WHERE bookingID = ?");
			$stmt->bind_param("i", $bookingID);
			$stmt->execute();
			$stmt->bind_result($reviewLeft);
			$stmt->fetch();

			if($reviewLeft==0) return false;
			else return true;
		}

		function getVenueInfo($venueName){
			$stmt = $this->con->prepare("SELECT venueDesc, postcode FROM venue WHERE venueName = ?");
			$stmt->bind_param("s", $venueName);
			$stmt->execute();
			$stmt->bind_result($venueDescription, $postcode);
			$stmt->fetch();
			$venue = array();
			$venue['venueDescription'] = $venueDescription;
			$venue['postcode'] = $postcode;
			$venue['name'] = $venueName;

			return $venue;
		}

		function getReviews($showName){
			$stmt = $this->con->prepare("SELECT rating FROM review WHERE showName = ?");
			$stmt->bind_param("s", $showName);
			$stmt->execute();
			$stmt->bind_result($rating);
			$sum = 0;
			$reviews = array();
			while($stmt->fetch()){
				$review = array();
				$review['rating'] = $rating;
				array_push($reviews, $review);
			}
			if ($stmt->num_rows==0) return false;
			return $reviews;
		}

		function getSales($showInstanceID, $date, $time){
			$stmt = $this->con->prepare("SELECT bookingID FROM booking WHERE showInstanceID = ? AND bookingDate = ? AND showTime = ?");
			$stmt->bind_param("iss", $showInstanceID, $date, $time);
			$stmt->execute();
			$stmt->bind_result($bookingID);
			$bookingIDs = array();
			while($stmt->fetch()){
				$booking = array();
				$booking = $bookingID;
				array_push($bookingIDs, $booking);
			}

			return $bookingIDs;
		}

		function getOpenSales($showInstanceID, $date, $time){
			$stmt = $this->con->prepare("SELECT priceBand, numberOfTickets FROM basketSales WHERE showInstanceID = ? AND showDate = ? AND showTime = ?");
			$stmt->bind_param("iss", $showInstanceID, $date, $time);
			$stmt->execute();
			$stmt->bind_result($priceBand, $numberTix);
			$priceBands = array();
			while($stmt->fetch()){
				$pb = array();
				$pb['priceBand'] = $priceBand;
				array_push($priceBands, $pb);
			}
			return $priceBands;
		}

		function checkPriceBand($showInstanceID, $date, $time){

			$priceBands = array();

			$stmt = $this->con->prepare("SELECT priceBand, numberOfTickets FROM basketSales WHERE showInstanceID = ? AND showDate = ? AND showTime = ?");
                        $stmt->bind_param("iss", $showInstanceID, $date, $time);
                        $stmt->execute();
                        $stmt->bind_result($priceBand, $numberTix);

			while($stmt->fetch()){
				$pb = array();
				for ($i = 0; $i<$numberTix; $i++){
					$pb['priceBand'] = $priceBand;
					array_push($priceBands, $pb);
				}
			}
			$bookings = array();
			if ($bookings = $this->getSales($showInstanceID, $date, $time)){
	 			$ids = join(',', $bookings);

				$stmt = $this->con->prepare("SELECT priceBand FROM ticket WHERE bookingID IN ($ids)");
				$stmt->execute();
				$stmt->bind_result($priceBand);

				while($stmt->fetch()){
					$result = array();
					$result['priceBand'] = $priceBand;
					array_push($priceBands, $result);
				}
			}
			return $priceBands;
		}

		function getReviewsDetailed($showName){
			$stmt = $this->con->prepare("SELECT userID, bookingID, rating, review, date FROM review WHERE showName = ?");
			$stmt->bind_param("s", $showName);
			$stmt->execute();
			$stmt->bind_result($userID, $bookingID, $rating, $reviewText, $date);
			$reviews = array();
			while($stmt->fetch()){
				$review = array();
				$review['userID'] = $userID;
				$review['bookingID'] = $bookingID;
				$review['rating'] = $rating;
				$review['reviewText'] = $reviewText;
				$review['date'] = $date;
				array_push($reviews, $review);
			}

			return $reviews;

		}

		function addBasketSales($showInstanceID, $priceBand, $date, $time, $userID, $numTix){
			$stmt = $this->con->prepare("INSERT INTO basketSales(showInstanceID, priceBand, showDate, showTime, userID, numberOfTickets) VALUES(?, ?, ?, ?, ?, ?)");
			$stmt->bind_param("isssii", $showInstanceID, $priceBand, $date, $time, $userID, $numTix);
			if ($stmt->execute())
				return mysqli_insert_id($this->con);
			else
				return false;

		}

		function deleteTemp($tempID){
			$stmt = $this->con->prepare("DELETE FROM basketSales WHERE idBasketSales = ?");
			$stmt->bind_param("i", $tempID);
			if ($stmt->execute())
				return true;
			else
				return false;
		}

		function checkScan($ticketID){
			$stmt = $this->con->prepare("SELECT scanned FROM ticket WHERE ticketID = ?");
			$stmt->bind_param("i", $ticketID);
			$stmt->execute();
			$stmt->bind_result($scanned);
			$stmt->fetch();
			if ($scanned === 1) return true;
			else return false;
		}

		function scan($ticketID){
			$stmt = $this->con->prepare("UPDATE ticket SET scanned = 1 WHERE ticketID = ?");
			$stmt->bind_param("i", $ticketID);
			if ($stmt->execute()) return true;
			else return false;
		}

		function getVenueInfoForBooking($showInstanceID){

			$stmt = $this->con->prepare("SELECT venueName FROM showInstance WHERE showInstanceID = ?");
			$stmt->bind_param("i", $showInstanceID);
			$stmt->execute();
			$stmt->bind_result($venueName);
			while($stmt->fetch()){}
			return $this->getVenueInfo($venueName);
		}

		function getShowRunningTime($showName){
			$stmt = $this->con->prepare("SELECT runningTime FROM shows WHERE showName = ?");
			$stmt->bind_param("s", $showName);
			$stmt->execute();
			$stmt->bind_result($runningTime);
			$stmt->Fetch();
			$runtime = array();
			$runtime['runningTime'] = $runningTime;
			return $runtime;
		}

	}

?>


