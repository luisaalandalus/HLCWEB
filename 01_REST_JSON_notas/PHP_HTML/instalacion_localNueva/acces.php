
<?php

	/*LOGIN*/

	/* este script recibe la información de la página web login.html a través del protocolo POST mediante los datos alumno y passw */
	$alumno = $_POST['alumno'];
	$passw = $_POST['passw'];

	// incluye el archivo funciones_bd.php 
	require_once 'funciones_bd.php';

	/* crea un objeto con el que se podrán llamar a todas las funciones 
	   que están dentro de funciones_bd.php */
	$db = new funciones_BD();

	// comprobar que el usuario y contraseña sean correctos
	if($db->login($alumno,$passw)){
		/* acceso incorrecto */
		$resultado[]=array("logstatus"=>"0");
	}else{
		/* acceso correcto */
		$resultado[]=array("logstatus"=>"1");
	}

	// codifica con json el array $resultado
	echo json_encode($resultado);

?>
