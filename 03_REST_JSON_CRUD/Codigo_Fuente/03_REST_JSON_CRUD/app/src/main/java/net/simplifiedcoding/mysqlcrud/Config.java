package net.simplifiedcoding.mysqlcrud;

/**
 * Created by Belal on 10/24/2015.
 */
public class Config {

    // Host o URL donde están alojados los scripts PHP
    public static final String HOST = "192.168.109.28";
    // public static final String HOST = "www.ramonjmc.honor.es"; // página de http://cpanel.2freehosting.com/

    // URL de nuestros scripts PHP
    public static final String URL_ADD = "http://"+HOST+"/CRUD/addEmp.php";
    public static final String URL_GET_ALL = "http://"+HOST+"/CRUD/getAllEmp.php";
    public static final String URL_GET_EMP = "http://"+HOST+"/CRUD/getEmp.php?id=";
    public static final String URL_UPDATE_EMP = "http://"+HOST+"/CRUD/updateEmp.php";
    public static final String URL_DELETE_EMP = "http://"+HOST+"/CRUD/deleteEmp.php?id=";

    //LLaves o Keys que serán usados para acceder a los campos de las tablas de la BD con los scripts PHP
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAME = "name";
    public static final String KEY_EMP_DESG = "desg";
    public static final String KEY_EMP_SAL = "salary";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_DESG = "desg";
    public static final String TAG_SAL = "salary";

    //id del empleado para lanzar intent
    public static final String EMP_ID = "emp_id";
}
