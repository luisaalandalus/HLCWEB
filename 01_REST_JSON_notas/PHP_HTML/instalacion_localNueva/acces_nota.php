
<?php

	/* Acceso a las notas del alumno */

	// recibe mediante POST el nombre del alumno
	$alumno = $_POST['alumno'];

	// incluir el archivo 'funciones_bd.php' que contiene la clase funciones_BD
	require_once 'funciones_bd.php';

	// llama al constructor de la clase funciones_DB de esta forma se intenta conectar a la BD
	$db = new funciones_BD();

	// guarda el resultado de la consulta (nota del alumno) en el array resultado[]
	$resultado[] = array("nota"=>$db->ver_nota($alumno));

	// muestra el array resultado codificado mediante JSON
	echo json_encode($resultado);

?>
