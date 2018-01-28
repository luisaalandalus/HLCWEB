<?php
	$hostname_localhost ="localhost"; // ubicación del servidor
	$database_localhost ="alumnos";   // nombre de la BD
	$username_localhost ="root";      // nombre del usuario
	$password_localhost ="123456";  // contraseña

	// intentar conectar al servidor con el usuario y contraseña anteriores	
	$localhost = mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
	or	
	trigger_error(mysql_error(),E_USER_ERROR); // en caso de error en la conexión
	
 	// intentar seleccionar la BD
	mysql_select_db($database_localhost, $localhost);

	// realizar una consulta que selecciona todas las matriculas de los alumnos
	// ordenadas por la matricula
	$query_search = "select * from matriculas order by matricula";
	
	// lanzar la consulta
	$query_exec = mysql_query($query_search) 
	or 
	die(mysql_error()); // en caso de que haya un error en la consulta

	// mostrar por pantalla el resultado de la consulta
	while($row = mysql_fetch_array($query_exec)){
		echo $row['nombre']." <br>".$row['matricula']." <br>".$row['telefono']." <br>".$row['email']."<br>/";
	}

	// la consulta anterior está separado cada alumno por una barra => /
	// y cada dato del alumno por un <br>
?>
