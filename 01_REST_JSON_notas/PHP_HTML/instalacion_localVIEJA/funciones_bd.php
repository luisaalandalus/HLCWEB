<?php

class funciones_BD {

    private $db;

    // constructor

    function __construct() {
        require_once 'connectbd.php';
        // connecting to database

        $this->db = new DB_Connect();
        $this->db->connect();

    }

    // destructor
    function __destruct() {

    }

    /**
     * agregar nuevo alumno
     */
    public function adduser($alumno, $password) {

    	$result = mysql_query("INSERT INTO acceso(nombre,password) VALUES('$alumno', '$password')");
        // check for successful store

        if ($result) {

            return true;

        } else {

            return false;
        }

    }


	/**
     * agregar nueva nota
     */
    public function addnota($alumno, $nota) {

    	$result = mysql_query("INSERT INTO calificaciones(nombre,nota) VALUES('$alumno', '$nota')");
        // check for successful store

        if ($result) {

            return true;

        } else {

            return false;
        }

    }




     /**
     * Verificar si ya existe el alumno
     */

    public function isuserexist($alumno) {

        $result = mysql_query("SELECT nombre from acceso WHERE nombre = '$alumno'");

        $num_rows = mysql_num_rows($result); //numero de filas retornadas

        if ($num_rows > 0) {

            // el alumno existe

            return true;
        } else {
            // no existe
            return false;
        }
    }


	public function login($alumno,$passw){

		$result=mysql_query("SELECT COUNT(*) FROM acceso WHERE nombre='$alumno' AND password='$passw' ");
		$count = mysql_fetch_row($result);

		/*como el alumno debe ser único cuenta el numero de ocurrencias con esos datos*/


		if ($count[0]==0){
			return true;
		}else{
			return false;
		}
	}


	public function ver_nota($alumno){

		// consultar la nota de una alumno => puede que no tenga la nota asignada
		$result=mysql_query("SELECT COUNT(*) FROM calificaciones WHERE nombre='$alumno'");
		$count = mysql_fetch_row($result);


		if ($count[0]==0){
			// error el alumno no tiene notas => se devuelve la nota -1
			$nota = -1;
		}else{
			$nota1 = mysql_query("SELECT nota FROM calificaciones WHERE nombre='$alumno'");
			$fila = mysql_fetch_row($nota1);
			$nota = $fila[0];
		}

		return $nota;
	}

}

?>
