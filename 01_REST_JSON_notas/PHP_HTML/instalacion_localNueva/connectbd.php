<?php
 
class DB_Connect {
 
    // constructor
    function __construct() {
 
    }
 
    // destructor
    function __destruct() {
        // $this->close();
    }
 
    // Conectar a la BD
    public function connect() {
		// incluye el archivo config.php que define los datos de la conexión a la BD
        require_once 'config.php';

        // conectando a mysql
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);

        // Seleccionando la base de datos
        mysql_select_db(DB_DATABASE);
 
        // devuelve el manejador de la BD a la que se ha conectado
        return $con;
    }
 
    // Cerrar la conexión a la BD
    public function close() {
        mysql_close();
    }
 
}
 
?>
