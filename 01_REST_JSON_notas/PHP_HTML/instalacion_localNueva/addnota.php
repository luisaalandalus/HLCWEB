<?php

	// recoge el nombre del alumno y su nota mediante POST
	$alumno = $_POST['alumno'];
	$nota = $_POST['nota'];

	// incluye el archivo funciones_bd.php con la clase funciones_BD
	require_once 'funciones_bd.php';
	// llama al constructor de la clase para intentar conectarse a la BD
	$db = new funciones_BD();

	// comprobar si el alumno  existe
	if($db->isuserexist($alumno)==false){

		echo(" Este alumno no existe ingrese otro diferente!");
	}else{

		if($db->addnota($alumno,$nota))
		{	
			echo(" Las notas del alumno fueron agregadas a la Base de Datos correctamente.");			
		}else{
			echo(" ha ocurrido un error.");
		}		

	}
?>



