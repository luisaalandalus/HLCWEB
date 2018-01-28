<?php

	/* este script recibe la información de la página web adduser.html a través del protocolo POST 
	mediante los datos alumno y passw */
	$usuario = $_POST['alumno'];
	$passw = $_POST['passw'];

	// incluye el archivo funciones_bd.php
	require_once 'funciones_bd.php';

	/* crea un objeto con el que se podrán llamar a todas las funciones 
	   que están dentro de funciones_bd.php */
	$db = new funciones_BD();

	// comprobar si el alumno y contraseña ya existen en la base de datos
	if($db->isuserexist($usuario,$passw)){

		echo(" Este alumno ya existe ingrese otro diferente!");
	}else{
		// el alumno no existe, se va a intentar insertar un alumno y contraseña nuevos
		if($db->adduser($usuario,$passw))
		{	
			echo(" El alumno fue agregado a la Base de Datos correctamente.");			
		}else{
			echo(" ha ocurrido un error.");
		}		

	}
?>



