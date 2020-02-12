<?php

	//getting the dboperation class
	require_once 'DbOperation.php';

	//function validating all the paramters are available
	//we will pass the required parameters to this function
	function areTheseParametersAvailable($params){
		//assuming all parameters are available
		$available = true;
		$missingparams = "";

		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false;
				$missingparams = $missingparams . ", " . $param;
			}
		}

		//if parameters are missing
		if(!$available){
			$response = array();
			$response['error'] = true;
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' are missing';

			//displaying error
			echo json_encode($response);

			//stopping further execution
			die();
		}
	}

	//an array to display response
	$response = array();

	//if it is an api call
	//that means a get parameter named api call is set in the URL
	//and with this parameter we are concluding that it is an api call
	if(isset($_GET['apicall'])){

		switch($_GET['apicall']){

			//the CREATE operation
			//if the api call value is 'createuser'
			//we will create a record in the database
			case 'createuser':
				//first check the parameters required for this request are available or not
				areTheseParametersAvailable(array('email','firstName','lastName','password'));

				//creating a new dboperation object
				$db = new DbOperation();

				//creating a new record in the database
				$result = $db->createUser(
					$_POST['email'],
					$_POST['firstName'],
					$_POST['lastName'],
					$_POST['password']
				);


				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false;
					//in message we have a success message
					$response['message'] = 'User added successfully';
				}else{

					//if record is not added that means there is an error
					$response['error'] = true;
					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}

			break;

			//the READ operation
			//if the call is getusers
			case 'getusers':
				$db = new DbOperation();
				$response['error'] = false;
				$response['message'] = 'Request successfully completed';
				$response['users'] = $db->getUsers();
			break;

			case 'getuser':

				if(isset($_GET['email'], $_GET['password'])){
					$db = new DbOperation();
					if($db->getUser($_GET['email'],$_GET['password'])){
						$response['error'] = false;
						$response['message']= 'Request successfully completed';
						$response['userid'] = $db->getUserID($_GET['email']);
					}else{
						$response['error'] = true;
						$response['message'] = 'No user found with these details';
					}
				}else{
					$response['error'] = true;
					$response['message'] = 'Nothing to fetch, provide an email please';
				}
			break;




			//the UPDATE operation
			case 'updateuser':
				areTheseParametersAvailable(array('userID','email','firstName','lastName','password'));
				$db = new DbOperation();
				$result = $db->updateUser(
					$_POST['userID'],
					$_POST['email'],
					$_POST['firstName'],
					$_POST['lastName'],
					$_POST['password']
				);

				if($result){
					$response['error'] = false;
					$response['message'] = 'User updated successfully';
					$response['users'] = $db->getUsers();
				}else{
					$response['error'] = true;
					$response['message'] = 'Some error occurred please try again';
				}
			break;

			//the delete operation
			case 'deleteuser':

				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['userID'])){
					$db = new DbOperation();
					if($db->deleteUser($_GET['userID'])){
						$response['error'] = false;
						$response['message'] = 'User deleted successfully';
						$response['users'] = $db->getUsers();
					}else{
						$response['error'] = true;
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true;
					$response['message'] = 'Nothing to delete, provide an id please';
				}
			break;

			case 'getliveshows':

				$db = new DbOperation();
				$response = $db->getLiveShows();

			break;


			case 'getshowinfo':
				if(isset($_GET['showName'])){
					$db = new DbOperation();
					if($db->getShowInfo($_GET['showName']))
						$response = $db->getShowInfo($_GET['showName']);
					 else
						$response['error'] = true;
				}
			break;

			case 'createbooking':
				areTheseParametersAvailable(array('instanceID', 'userID', 'numberOfTickets', 'date', 'showName'));
				$db = new DbOperation();

				$result = $db->createBooking($_POST['instanceID'],$_POST['userID'],$_POST['numberOfTickets'],$_POST['date'], $_POST['showName']);

				if($result){
					$response['error'] = false;
					$response['message'] = 'Booking added';
					//$response['bookingid'] = $db->createBooking($_POST['instanceID'],$_POST['userID'],$_POST['numberOfTickets'],$_POST['date']);
					$response['bookingid'] = $result;
				}else{
					$response['error'] = true;
					$response['message'] = 'Some error occurred please try again';
				}

			break;

			case 'createticket':
				areTheseParametersAvailable(array('bookingID', 'price'));
				$db = new DbOperation();

				$result = $db->createTicket($_POST['bookingID'], $_POST['price']);

				if($result){
					$response['error'] = false;
					$response['message'] = 'Ticket added';
				}else{
					$response['error'] = true;
					$response['message'] = "Some error occurred - check input data and try again";
				}
			break;

			case 'getfuturebookings':
				if(isset($_GET['userID'])){
					$db = new DbOperation();
					if ($response = $db->getFutureBookings($_GET['userID'])){
						$response = $db->getFutureBookings($_GET['userID']);
						//$response['message'] = "Success";
						//$response['error'] = "false";
					}else
						$response['message'] = "Empty";
				}else{
					$response['error'] = true;
					$response['message'] = "Some error";
				}
			break;


			case 'getshowname':
				if(isset($_GET['showInstanceID'])){
					$db = new DbOperation();
					//if ($response = $db->getShowName($_GET['showInstanceID'])){
						$response['error'] = false;
						$response['showName'] = $db->getShowName($_GET['showInstanceID']);
					//}else
					//	$response['message'] = "Invalid showInstanceID";

				}


			break;

		}
	}else{
		//if it is not api call
		//pushing appropriate values to response array
		$response['error'] = true;
		$response['message'] = 'Invalid API Call';
	}
	//displaying the response in json structure
	echo json_encode($response);
?>
