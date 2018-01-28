<?php

$usuario = $_POST['alumno'];
$passw = $_POST['nota'];

require_once 'funciones_bd.php';
$db = new funciones_BD();

	if($db->isuserexist($usuario,$passw)==false){

		echo(" Este alumno no existe ingrese otro diferente!");
	}else{

		if($db->addnota($usuario,$passw))
		{	
			echo(" Las notas del alumno fueron agregadas a la Base de Datos correctamente.");			
		}else{
			echo(" ha ocurrido un error.");
		}		

	}
?>



