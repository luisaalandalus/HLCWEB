<?php
	/*
		author: Belal Khan 
		website: http://www.simplifiedcoding.net
		
		My Database is androiddb 
		you need to change the database name rest the things are default if you are using wamp or xampp server
		You may need to change the host user name or password if you have changed the defaults in your server
	*/
	
	define('HOST','mysql.2freehosting.com'); //nuestro servidor de BD
	define('USER','u777888_user'); //Nombre de usuario de nuestra base de datos
	define('PASS','vcdfTr45'); //Contraseña de nuestra base de datos
	define('DB','u777888_and'); //Nombre de nuestra base de datos
	
	$con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');


?>
