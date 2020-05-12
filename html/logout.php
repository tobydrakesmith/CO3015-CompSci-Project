<?php
	session_start();
	session_unset();
	// destroy the session
	session_destroy();
	header('Refresh: 1; URL = login.php');
?>
