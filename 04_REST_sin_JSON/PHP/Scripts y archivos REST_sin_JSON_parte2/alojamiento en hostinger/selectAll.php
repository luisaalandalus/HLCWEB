<?php
	// REST SIN JSON

	// http://ramonjmc.zz.mu/rest_sin_json

	// Hostinger usa una versión de php más avanzada => Extensión MySQL mejorada
	// han quedado algunas funciones obsoletas o deprecated => mysql_connect => obsoleta
	// http://php.net/manual/es/book.mysqli.php
 
	$hostname ="mysql.hostinger.es";  //nuestro servidor
	$database ="u777888_alum"; //Nombre de nuestra base de datos
	$username ="u777888_usu2";       //Nombre de usuario de nuestra base de datos (yo utilizo el valor por defecto)
	$password ="abC7Y567Z";   //Contraseña de nuestra base de datos	

	// intentar conectar al servidor con el usuario y contraseña anteriores
	$mysqli = new mysqli($hostname,$username,$password, $database);

	/* Advertencia
	La propiedad mysqli->connect_error solo funciona correctamente en las versiones 5.2.9 y 5.3.0 de PHP. Usa la 			
	función mysqli_connect_error() si es necesaria la compatibilidad con versiones anteriores de PHP.*/
	if ($mysqli->connect_error) {
		die('Connect Error (' . $mysqli->connect_errno . ') '
			    . $mysqli->connect_error);
	}	 	

	// realizar una consulta que selecciona todas las matriculas de los alumnos
	// ordenadas por la matricula
	$query_search = "select * from matriculas order by matricula";
	
	// lanzar la consulta
	$query_exec = $mysqli->query($query_search);
	if (!$query_exec) {
		echo "Falló la consulta de la tabla matriculas: (" . $mysqli->errno . ") " . $mysqli->error;
	}
	else {

		// mostrar por pantalla el resultado de la consulta
		while($row = $query_exec->fetch_array(MYSQLI_ASSOC)){
			echo $row['nombre']." <br>".$row['matricula']." <br>".$row['telefono']." <br>".$row['email']."<br>/";
		}

		// la consulta anterior está separado cada alumno por una barra => /
		// y cada dato del alumno por un <br>
	}

?>
