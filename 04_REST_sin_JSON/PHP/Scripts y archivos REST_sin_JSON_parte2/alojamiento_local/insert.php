<?PHP
	$hostname_localhost ="localhost";  //nuestro servidor
	$database_localhost ="alumnos"; //Nombre de nuestra base de datos
	$username_localhost ="root";       //Nombre de usuario de nuestra base de datos (yo utilizo el valor por defecto)
	$password_localhost ="123456";   //Contraseña de nuestra base de datos

	// Conexión a nuestro servidor mysql
	$localhost = mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
	or
	trigger_error(mysql_error(),E_USER_ERROR); //mensaje de error si no se puede conectar

	//selección de la base de datos con la que se desea trabajar	
	mysql_select_db($database_localhost, $localhost);

	// variables que almacenan los valores que enviamos por nuestra app por POST o a través de un formulario
	// observar que se llaman igual en nuestra app y aqui
	$alumno=$_POST['alumno'];
	$matricula=$_POST['matricula'];
	$telefono=$_POST['telefono'];
	$email=$_POST['email'];

	// definir la sentencia sql para insertar en la BD un alumno en la tabla matriculas
	$query_search = "insert into matriculas(nombre,matricula,telefono,email) values ('".$alumno."','".$matricula."','".$telefono."','".$email."')";
	//Ejecutar la sentencia sql anterior
	$query_exec = mysql_query($query_search) 
	or 
	die(mysql_error()); // mensaje de error si no se puede realizar la sentencia
?>
