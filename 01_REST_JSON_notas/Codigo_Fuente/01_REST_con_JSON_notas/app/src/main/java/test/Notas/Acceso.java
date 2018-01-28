package test.Notas;
/* Código adaptado por Ramón José Martínez Cuevas 
  del original de SEBASTIAN CIPOLAT 
  => https://github.com/Androideity/Login-en-Android-usando-PHP-y-MySQL*/

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import test.Notas.clases.Httppostaux;

// funciona también para emuladores
// puede funcionar en dispositivos reales para un servidor local,
// cuando el dispositivo (móvil o tablet) esté conectado a la misma red que el ordenador local
// en caso de un servidor en Internet

public class Acceso extends Activity {
    /**
     * Called when the activity is first created.
     */

    // atributos
    EditText alumno;
    EditText contraseña;
    Button login;
    TextView registrar;
    Httppostaux post; // para recibir los datos enviados por el formulario
    int nota = -1; // nota del alumno => -1 => no le han puesto nota todavía

    // IMPORTANTE PONER IP REAL DEL SERVIDOR DE BASE DE DATOS (no vale la de localhost o 127.0.0.1)
    // IP DE NUESTRO PC o IP O URL donde esté alojado el servidor en Internet
    // Ten en cuenta que la dir. IP de tu ordenador puede cambiar
    String URL_SERVIDOR="ramonjmc.pe.hu"; // hostinger
    //private final static String URL_SERVIDOR ="192.168.43.19"; // LA IP CAMBIARÁ DE UNA VEZ A OTRA COMPROBAR CON ifconfig


    // URL donde está el script acces.php
    // => permitirá conectarse a la BD y consultar si el alumno y contraseña son válidos
    String URL_connect = "http://" + URL_SERVIDOR + "/acceso/acces.php";//ruta en donde están nuestros archivos
    // URL donde está el script acces_nota.php => para obtener la nota de un alumno
    String URL_notas = "http://" + URL_SERVIDOR + "/acceso/acces_nota.php";//ruta en donde están nuestros archivos

    private ProgressDialog pDialog; // barra de progreso (mostrada mientras se conecta a la BD)

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // cargar layout => Res/layout/main
        setContentView(R.layout.main);

        // crear un objeto Httppost que permite mandar los valores que necesita el script PHP
        // mediante POST (como si se mandarán a través de un formulario en HTML)
        post = new Httppostaux();

        // acceder a los diferentes elementos del layout => cuadros de texto, botón y etiqueta
        alumno = (EditText) findViewById(R.id.edalumno);
        contraseña = (EditText) findViewById(R.id.edpassword);
        login = (Button) findViewById(R.id.Blogin);
        registrar = (TextView) findViewById(R.id.link_to_register);

        //Definir la acción a realizar con el botón del login
        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Cogemos los datos de los EditText
                String usuario = alumno.getText().toString();
                String passw = contraseña.getText().toString();

                //verificamos si estan en blanco
                if (checklogindata(usuario, passw) == true) {
                    //si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parámetros
                    new asynclogin().execute(usuario, passw);
                } else {
                    //si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
                    //err_login();
                    tostada("Error, nombre o contraseña en blanco");
                }
            }
        });

        //Definir la acción a realizar con la etiqueta registrar
        registrar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Abre el navegador al formulario adduser.html => añadir alumnos (nombre y contraseña)
                String url = "http://" + URL_SERVIDOR + "/acceso/adduser.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    } // fin onCreate

    // vibra y muestra una tostada
    public void err_login() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        tostada("Error:Nombre de alumno o password incorrectos");
    }

    // muestra una tostada
    public void tostada(String mensaje) {
        Toast toast1 = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
        toast1.show();
    }

    /* E: alumno => nombre del alumno
          password => contraseña
       S: Intenta conectarse con el servidor a través del script de PHP contenido en URL_connect
          mediante un objeto Httppost que permite mandar los valores a dicho script
          devuelve true => si se ha podido conectar al servidor
                           y verificar que el usuario y contraseña son correctos
                   false => en caso contrario
     */
    public boolean loginstatus(String alumno, String password) {
        int logstatus = -1;
        boolean login = false; // indica si el logueo es correcto (true) o incorrecto (false)

    	/* Creamos un ArrayList del tipo nombre valor (NameValuePair)
    	 * para agregar los datos (alumno y contraseña) enviados mediante  POST a nuestro servidor PHP
    	 * para realizar la validación */
        ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();

        // añadimos al ArrayList el nombre del alumno y su contraseña
        postparameters2send.add(new BasicNameValuePair("alumno", alumno));
        postparameters2send.add(new BasicNameValuePair("passw", password));

        //realizamos una peticion y como respuesta obtienes un array JSON => jdata
        JSONArray jdata = post.getserverdata(postparameters2send, URL_connect);

		/*como estamos trabajando de manera local el ida y vuelta será casi inmediato
		 * para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
		 * observar el progressdialog, la podemos eliminar si queremos
		 */
        //SystemClock.sleep(950);

        //si la consulta JSON no es null y tiene algún dato
        if (jdata != null && jdata.length() > 0) {

            //creamos un objeto JSON
            JSONObject json_data;

            try {
                json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el único
                logstatus = json_data.getInt("logstatus");//accedemos al valor
                Log.e("loginstatus", "logstatus= " + logstatus);//muestro por log que obtuvimos
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                //tostada("JSON Exception"); => no se puede poner en primer plano
                Log.e("JSON Exception", "JSON error !!!!");
                e.printStackTrace();
            }

            //validamos el valor obtenido
            if (logstatus == 0) {// [{"logstatus":"0"}]
                Log.e("loginstatus ", "inválido");
                // logueo incorrecto
                login = false;
            } else {// [{"logstatus":"1"}]
                Log.e("loginstatus ", "válido");
                // logueo correcto
                login = true;
            }

        } else {    //json obtenido invalido verificar parte WEB.
            Log.e("JSON LOGIN ", "ERROR !!!");
            login = false;
        }

        return login;

    } // fin loginstatus()


    /* E: alumno => nombre del alumno
       S: Intenta conectarse con el servidor a través del script de PHP contenido en URL_notas
          mediante un objeto Httppost que permite mandar los valores a dicho script
          devuelve el valor de la nota del alumno, si dicho alumno no tiene nota, nota vale -1
     */
    public int consulta_nota(String alumno) {

        Httppostaux post2 = new Httppostaux();

    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/
        ArrayList<NameValuePair> postsend = new ArrayList<NameValuePair>();

        // añadimos al ArrayList el nombre del alumno
        postsend.add(new BasicNameValuePair("alumno", alumno));

        //realizamos una petición y como respuesta obtenemos un array JSON
        JSONArray jdata = post2.getserverdata(postsend, URL_notas);

        //si lo que obtuvimos no es null
        if (jdata != null && jdata.length() > 0) {

            JSONObject json_data;

            //creamos un objeto JSON
            try {
                json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el único
                nota = json_data.getInt("nota");//accedemos al valor de la nota
                Log.e("valor de la nota", "nota= " + nota);//muestro por log lo que obtuvimos
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //validamos el valor obtenido
            if (nota == -1) {// [{"nota":"-1"}] => cadena JSON
                // logueo incorrecto
                Log.e("valor de la nota ", "incorrecto = " + nota);
            } else {
                // logueo correcto
                Log.e("valor de la nota ", "correcto = " + nota);
            }

        } else {    //json obtenido inválido verificar parte WEB.
            Log.e("JSON NOTA INVÁLIDO ", "ERROR");
            nota = -1;
        }

        return nota;

    } // fin consulta_nota()


    // validamos si no hay ningún campo vacío
    // Entrada: alumno => nombre del alumno
    //          password => contraseña
    // Salida:  true => si no hay ningún campo vacío
    //          false => si alumno y contraseña tienen información
    public boolean checklogindata(String alumno, String password) {

        boolean resul = false;

        // comprobar si hay algún campo vacío
        if (alumno.equals("") || password.equals("")) {
            Log.e("Acceso ui", "hay algún campo sin información");
            resul = false;
        } else
            // no hay campos vacíos
            resul = true;

        return resul;
    }

    /*		CLASE ASYNCTASK
    *
    * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
    * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir
    * si la conexion es lenta o el servidor tarda en responder la aplicacion será inestable.
    * ademas observariamos el mensaje de que la app no responde.
    */
    class asynclogin extends AsyncTask<String, String, String> {

        String alumno, pass;

        /* Proceso Invocado en la Interfaz de Usuario (IU) antes de ejecutar la tarea en segundo plano.
           En este caso, muestra una barra de progreso
         */
        protected void onPreExecute() {
            //para el progress dialog => barra de progreso
            //tostada("Autenticando....");

            pDialog = new ProgressDialog(Acceso.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // Tarea a realizar en segundo plano (con otro hilo que no está en el Interfaz de Usuario)
        // por lo tanto esta tarea no puede interaccionar con el usuario
        protected String doInBackground(String... params) {
            // este método lo hemos llamado antes con => new asynclogin().execute(usuario,passw);
            // obtenemos alumno y password de los parámetros
            alumno = params[0];
            pass = params[1];

            String resultado = "err";

            // enviamos y recibimos y analizamos los datos en segundo plano.
            // comprobar si el nombre y usuario son correctos
            if (loginstatus(alumno, pass) == true) {
                //login válido
                // hay que consultar la nota del alumno
                consulta_nota(alumno);

                resultado = "ok";
            } else {
                //login inválido
                resultado = "err";
            }

            return resultado;

        } // fin doInBackground()

        /* Una vez terminado doInBackground según lo que halla ocurrido
           intentamos mostrar la nota o un error de acceso */
        protected void onPostExecute(String result) {

            pDialog.dismiss();//ocultamos progess dialog
            Log.e("onPostExecute=", "" + result);

            // comparar el valor enviado mediante el return por doInBackground
            if (result.equals("ok")) {
                // login válido => hay que lanzar actividad que muestra las notas
                Intent i = new Intent(Acceso.this, Muestra_notas.class);
                // mandar el nombre del alumno
                i.putExtra("alumno", alumno);

                // mandar la nota
                i.putExtra("nota", nota);

                startActivity(i);
            } else {
                // login inválido => mostrar error de acceso en una tostada
                err_login();
                tostada("Error de acceso...");
            }
        } // fin onPostExecute()

    } // fin clase asynclogin

}