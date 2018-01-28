
<?php

	/*LOGIN*/

	$alumno = $_POST['alumno'];
	$passw = $_POST['passw'];


	require_once 'funciones_bd.php';
	$db = new funciones_BD();

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
