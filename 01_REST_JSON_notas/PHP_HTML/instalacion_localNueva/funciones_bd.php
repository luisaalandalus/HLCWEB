<?php
 
class funciones_BD {
 
    private $db;
 
    // constructor
    function __construct() {
		/* incluye el archivo connectbd.php que define la clase DB_Connect 
           para conectarse a la BD */
        require_once 'connectbd.php';

        // Crea un objeto db de la clase DB_Connect
        $this->db = new DB_Connect();
		// llama a la función connect() para intentar conectarse a la BD
        $this->db->connect();

    }
 
    // destructor
    function __destruct() {
 
    }
 
    /**
     * agregar nuevo alumno
     * devuelve true si la inserción es correcta
     * devuelve false si la inserción es incorrecta
     */
     
    public function adduser($alumno, $password) {

		// Ejecuta la sentencia MySQL para insertar en la tabla acceso un alumno y su contraseña    	
		$result = mysql_query("INSERT INTO acceso(nombre,password) VALUES('$alumno', '$password')");

        // Comprobar si la inserción ha sido correcta
        if ($result) {

            return true;

        } else {

            return false;
        }

    }


	/**
     * agregar nueva nota al alumno
	 * devuelve true si la inserción es correcta
     * devuelve false si la inserción es incorrecta
     */
    public function addnota($alumno, $nota) {

		// Ejecuta la sentencia MySQL para insertar en la tabla calificaciones un alumno y su nota
    	$result = mysql_query("INSERT INTO calificaciones(nombre,nota) VALUES('$alumno', '$nota')");

        // Comprobar si la inserción ha sido correcta
        if ($result) {
			// inserción correcta
            return true;
        } else {
			// inserción incorrecta
            return false;
        }

    } 
 
	 /**
	 * Verificar si ya existe el alumno
	 */
    public function isuserexist($alumno) {

		// Ejecuta la sentencia MySQL de consulta de un alumno en la tabla acceso
        $result = mysql_query("SELECT nombre from acceso WHERE nombre = '$alumno'");

        $num_rows = mysql_num_rows($result); //número de filas retornadas

        if ($num_rows > 0) {
            // el alumno existe 
            return true;
        } else {
            // no existe
            return false;
        }
    }
 
   	/* comprueba si el login es correcto o no
       true => si es incorrecto
       false => si es correcto */
	public function login($alumno,$passw){

		// Ejecuta la sentencia MySQL para contar el número de alumnos con el nombre y contraseña escogidos		
		$result=mysql_query("SELECT COUNT(*) FROM acceso WHERE nombre='$alumno' AND password='$passw' "); 
		$count = mysql_fetch_row($result); //número de ocurrencias retornadas

		/*como el alumno debe ser único cuenta el numero de ocurrencias con esos datos*/
		if ($count[0]==0){
			return true; // login incorrecto
		}else{
			return false; // login correcto
		}
	}


	// devuelve la nota del alumno
	// -1 => si el alumno no tiene la nota puesta o no existe
	public function ver_nota($alumno){
		
		// consultar la nota de una alumno => puede que no tenga la nota asignada
		$result=mysql_query("SELECT COUNT(*) FROM calificaciones WHERE nombre='$alumno'"); 
		$count = mysql_fetch_row($result);	

		if ($count[0]==0){
			// error el alumno no tiene notas => se devuelve la nota -1
			$nota = -1;
		}else{
			// hay al menos un alumno con notas, se selecciona dicho alumno
			$nota1 = mysql_query("SELECT nota FROM calificaciones WHERE nombre='$alumno'");
			$fila = mysql_fetch_row($nota1);
			$nota = $fila[0];  // escoger la nota del alumno
		}

		return $nota;
	}
  
}
 
?>
