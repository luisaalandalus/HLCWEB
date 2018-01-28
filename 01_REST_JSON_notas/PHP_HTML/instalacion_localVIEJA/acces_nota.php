
<?php

	/* Acceso a las notas del alumno */

	$alumno = $_POST['alumno'];

	require_once 'funciones_bd.php';
	$db = new funciones_BD();

	$resultado[] = array("nota"=>$db->ver_nota($alumno));

	echo json_encode($resultado);

?>
