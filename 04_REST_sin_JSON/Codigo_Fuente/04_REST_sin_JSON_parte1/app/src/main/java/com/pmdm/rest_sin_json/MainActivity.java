package com.pmdm.rest_sin_json;

// código adaptado por Ramón José Martínez Cuevas del código original de la página web:
// http://picarcodigo.blogspot.com.es/2014/05/webservice-conexion-base-de-datos-mysql.html

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


// recuerda que debes añadir el permiso de internet al manifiesto para poder acceder a
// los scripts de php y mandar información por POST mediante HTTP
// <uses-permission android:name="android.permission.INTERNET" />

// OJO!! Este programa solo permite Insertar alumnos en la BD, pero no mostrarlos
// en la aplicación REST_sin_JSON_parte2 si se podrán mostrar los alumnos

public class MainActivity extends Activity {

    // atributos
    private EditText matricula;
    private EditText alumno;
    private EditText telefono;
    private EditText email;
    private Button insertar;

    private ProgressDialog pDialog; // barra de progreso (mostrada mientras se conecta a la BD)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // establacer que la orientación del dispositivo sea siempre vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);

        // acceder a los cuadros de texto
        matricula=(EditText)findViewById(R.id.dni);
        alumno=(EditText)findViewById(R.id.nombre);
        telefono=(EditText)findViewById(R.id.telefono);
        email=(EditText)findViewById(R.id.email);

        // acceder al botón insertar
        insertar=(Button)findViewById(R.id.insertar);
        //Definir la acción del botón Insertar => Insertamos los datos del alumno
        insertar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //controlar que la información no esté en blanco
                if(!matricula.getText().toString().trim().equalsIgnoreCase("")||
                        !alumno.getText().toString().trim().equalsIgnoreCase("")||
                        !telefono.getText().toString().trim().equalsIgnoreCase("")||
                        !email.getText().toString().trim().equalsIgnoreCase(""))

                    // intentar insertar los datos del alumno con el servicio web
                    new WebService(MainActivity.this).execute();

                else
                    Toast.makeText(MainActivity.this, "Hay información por rellenar", Toast.LENGTH_LONG).show();
            }

        });

    }

    //Inserta los datos de las Personas en el servidor.
    private boolean insertar(){
        boolean resul = false;

        // interfaz para un cliente HTTP
        HttpClient httpclient;
        // define una lista de parámetros ("clave" "valor") que serán enviados por POST al script php
        List<NameValuePair> parametros_POST;
        // define un objeto para realizar una solicitud POST a través de HTTP
        HttpPost httppost;

        httpclient=new DefaultHttpClient();
        // creamos el objeto httpost para realizar una solicitud POST al script insert.php
        // OJO pon la dirección IP de tu servidor
        httppost= new HttpPost("http://192.168.109.28/rest_sin_json/insert.php"); // Url del Servidor

        /*como estamos trabajando de manera local el ida y vuelta será casi inmediato
		 * para darle un poco de realismo decimos que el proceso se pare por unos segundos para poder
		 * observar el progressdialog, la podemos eliminar si queremos
		 */
        //SystemClock.sleep(950); // dormir el proceso actual 950 milisegundos

        //Añadimos nuestros datos que vamos a enviar por POST al script insert.php
        parametros_POST = new ArrayList<NameValuePair>(4);
        parametros_POST.add(new BasicNameValuePair("alumno", alumno.getText().toString().trim()));
        parametros_POST.add(new BasicNameValuePair("matricula",matricula.getText().toString().trim()));
        parametros_POST.add(new BasicNameValuePair("telefono",telefono.getText().toString().trim()));
        parametros_POST.add(new BasicNameValuePair("email",email.getText().toString().trim()));

        try {
            // establece la entidad => como una lista de pares URL codificada.
            // Este suele ser útil al enviar una solicitud HTTP POST
            httppost.setEntity(new UrlEncodedFormEntity(parametros_POST));
            // intentamos ejecutar la solicitud HTTP POST
            httpclient.execute(httppost);
            resul = true;
        } catch (UnsupportedEncodingException e) {
            // La codificación de caracteres no es compatible
            resul = false;
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // Señala un error en el protocolo HTTP
            resul = false;
            e.printStackTrace();
        } catch (IOException e) {
            // Error de Entrada / Salida
            resul = false;
            e.printStackTrace();
        }

        // devuelve el resultado de la inserción
        return resul;

    } // fin insertar()

    // muestra una tostada
    public void tostada (String mensaje) {
        Toast toast1 = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
        toast1.show();
    }

    /*		CLASE ASYNCTASK
	*
	* usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
	* podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir
	* si la conexion es lenta o el servidor tarda en responder la aplicacion será inestable.
	* ademas observariamos el mensaje de que la app no responde.
	*/
    class WebService extends AsyncTask<String,String,String> {

        private Activity context;

        WebService(Activity context){
            this.context=context;
        }

        /* Proceso Invocado en la Interfaz de Usuario (IU) antes de ejecutar la tarea en segundo plano.
		   En este caso, muestra una barra de progreso
		 */
        protected void onPreExecute() {
            // Crea la barra de progreso
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Conectando a la Base de Datos....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // Tarea a realizar en segundo plano (con otro hilo que no está en el Interfaz de Usuario)
        // por lo tanto esta tarea no puede interaccionar con el usuario
        @Override
        protected String doInBackground(String... params) {
            String resultado ="ERROR";

            if(insertar())
                // la inserción del alumno ha sido exitosa
                resultado = "OK";
            else
                // ha habido un error al insertar el alumno y no se pudo insertar
                resultado = "ERROR";

            return resultado;
        }

        /* Una vez terminado doInBackground según lo que halla ocurrido
		   intentamos mostrar la tostada de que se pudo o no insertar el alumno */
        protected void onPostExecute (String result) {

            pDialog.dismiss();//ocultamos barra de progreso

            if (result.equals("OK")) {
                // inserción correcta
                tostada("Alumno insertado con éxito");
                alumno.setText("");
                matricula.setText("");
                telefono.setText("");
                email.setText("");
            }
            else
                tostada("ERROR, no se pudo insertar el alumno");
        } // fin onPostExecute()

    } // fin clase WebService
}