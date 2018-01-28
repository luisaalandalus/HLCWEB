package com.pmdm.rest_sin_json2;

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
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

// recuerda que debes añadir el permiso de internet al manifiesto para poder acceder a
// los scripts de php y mandar información por POST mediante HTTP
// <uses-permission android:name="android.permission.INTERNET" />

public class MainActivity extends Activity {

    // borra => esta variable auxiliar se usa para depurar el código
    // se puede prescindir de ella
    private String aux = "";

    // atributos
    public EditText matricula;
    public EditText alumno;
    public EditText telefono;
    public EditText email;
    private Button insertar;

    private Button mostrar;
    private ImageButton mas;
    private ImageButton menos;
    private int posicion=0;  // posición  del alumno a mostrar de la lista de alumnos
    private List<Alumno> listaAlumnos = null; // Lista de alumnos obtenidos de la BD

    private ProgressDialog pDialog = null; // barra de progreso (mostrada mientras se conecta a la BD)

    // dirección IP o URL del servidor
    // private final static String URL_SERVIDOR ="192.168.43.19"; // Servidor Local
    private final static String URL_SERVIDOR ="ramonjmc.pe.hu"; // servidor hostinger.es
    // URL del directorio de los scripts php del servidor
    private final static String URL_PHP ="http://"+URL_SERVIDOR+"/rest_sin_json/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // establacer que la orientación del dispositivo sea siempre vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);

        // acceder a los cuadros de texto
        matricula=(EditText)findViewById(R.id.matricula);
        alumno=(EditText)findViewById(R.id.nombre);
        telefono=(EditText)findViewById(R.id.telefono);
        email=(EditText)findViewById(R.id.email);

        // crear la lista de alumnos
        listaAlumnos=new ArrayList<Alumno>();

        // acceder al botón insertar
        insertar=(Button)findViewById(R.id.insertar);
        //Definir la acción del botón Insertar => Insertamos los datos del alumno
        insertar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //controlar que la información no esté vacía
                if (!matricula.getText().toString().trim().equalsIgnoreCase("") ||
                        !alumno.getText().toString().trim().equalsIgnoreCase("") ||
                        !telefono.getText().toString().trim().equalsIgnoreCase("") ||
                        !email.getText().toString().trim().equalsIgnoreCase(""))

                    // intentar insertar los datos del alumno con el servicio web
                    new WebService_insertar(MainActivity.this).execute();

                else
                    tostada("Hay información por rellenar");
            }

        });


        // acceder al botón mostrar
        mostrar=(Button)findViewById(R.id.mostrar);
        //Definir la acción del botón Mostrar =>Mostramos los datos de la persona por pantalla
        mostrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // mostrar el alumno
                new WebService_mostrar(MainActivity.this).execute();
            }
        });

        // acceder al botón más => +
        mas=(ImageButton)findViewById(R.id.mas);
        // Definir la acción del botón + => Se mueve por nuestro ArrayList mostrando el alumno siguiente
        mas.setOnClickListener(new View.OnClickListener() {

            // la lista de alumnos va desde la posición 0 hasta el tamaño-1 => size()-1

            @Override
            public void onClick(View v) {
                // Comprobar si la lista de alumnos no está vacía
                if (!listaAlumnos.isEmpty()) {

                    if (posicion >= listaAlumnos.size() - 1)
                        // se ha alcanzando o superado el final de lista
                        // posición debe valer el final de la lista por si se ha superado el valor
                        posicion = listaAlumnos.size() - 1;
                    else
                        // no se ha alcanzando o superado el final de lista => avanzar
                        posicion++;

                    // mostrar el alumno de la lista situado en posición
                    mostrarAlumno(posicion);
                }
            }

        });

        // Se mueve por nuestro ArrayList mostrando el objeto anterior
        menos=(ImageButton)findViewById(R.id.menos);
        // Definir la acción del botón - => Se mueve por nuestro ArrayList mostrando el alumno anterior
        menos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Comprobar si la lista de alumnos no está vacía
                if(!listaAlumnos.isEmpty()){
                    if(posicion<=0)
                        // se ha alcanzando el principio de lista o posición tiene valor negativo
                        // posición debe valer el principio de la lista por si tiene valor negativo
                        posicion=0;
                    else
                        // no se ha alcanzando el principio de lista => retroceder
                        posicion--;

                    // mostrar el alumno de la lista situado en posición
                    mostrarAlumno(posicion);

                }
            }
        });


    } // fin onCreate()

    // Intenta insertar los datos de las Personas en el servidor
    // a través del script => insert.php
    // devuelve true => sin la inserción es correcta
    // devuelve false => si hubo un error en la inserción
    private boolean insertar(){
        boolean resul = false;

        // interfaz para un cliente HTTP
        HttpClient httpclient;
        // define una lista de parámetros ("clave" "valor") que serán enviados por POST al script php
        List<NameValuePair> parametros_POST;
        // define un objeto para realizar una solicitud POST a través de HTTP
        HttpPost httppost;
        // crea el cliente HTTP
        httpclient=new DefaultHttpClient();
        // creamos el objeto httpost para realizar una solicitud POST al script insert.php
        httppost= new HttpPost(URL_PHP+"insert.php"); // Url del Servidor

        /*como estamos trabajando de manera local el ida y vuelta será casi inmediato
		 * para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
		 * observar el progressdialog, la podemos eliminar si queremos
		 */
        //SystemClock.sleep(950); // dormir el proceso actual 950 milisegundos

        //Añadimos nuestros datos que vamos a enviar por POST al script insert.php
        parametros_POST = new ArrayList<NameValuePair>(4);
        parametros_POST.add(new BasicNameValuePair("alumno", alumno.getText().toString().trim()));
        parametros_POST.add(new BasicNameValuePair("matricula",matricula.getText().toString().trim()));
        parametros_POST.add(new BasicNameValuePair("telefono", telefono.getText().toString().trim()));
        parametros_POST.add(new BasicNameValuePair("email", email.getText().toString().trim()));

        try {
            // establece la entidad => como una lista de pares URL codificada.
            // Esto suele ser útil al enviar una solicitud HTTP POST
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


    // Realiza una consulta a la BD de todas las matriculas de los alumnos
    // a través del script => selectAll.php
    // Devuelve los datos del servidor en forma de String
    private String mostrar() {

        // almacenará la respuesta del servidor BD
        String resquest="";

        // interfaz para un cliente HTTP
        HttpClient httpclient;
        // define una lista de parámetros ("clave" "valor") que serán enviados por POST al script php
        List<NameValuePair> parametros_POST;
        // define un objeto para realizar una solicitud POST a través de HTTP
        HttpPost httppost;
        // crea el cliente HTTP
        httpclient=new DefaultHttpClient();
        // creamos el objeto httpost para realizar una solicitud POST al script insert.php
        httppost= new HttpPost(URL_PHP+"selectAll.php"); // Url del Servidor

        try {
            // Ejecutamos y obtenemos la respuesta del servidor en forma de String
            // con un ResponseHandler y BasicResponseHandler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            resquest = httpclient.execute(httppost, responseHandler);
        } catch (UnsupportedEncodingException e) {
            // La codificación de caracteres no es compatible
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // Señala un error en el protocolo HTTP
            e.printStackTrace();
        } catch (IOException e) {
            // Error de Entrada / Salida
            e.printStackTrace();
        }
        return resquest;
    } // fin mostrar()

    // Descompone, crea un objeto con los datos descompuestos y lo almacena en nuestro ArrayList
    // devuelve true => si hay algún alumno que mostrar
    //          false => en caso contrario
    private boolean filtrarDatos(){
        boolean resul = false;
        listaAlumnos.clear();

        // realizar la consulta a la BD de todos los alumnos y almacenarlos en respuesta
        String respuesta = mostrar();

        // comprobar que hay algún dato en respuesta
        if(!respuesta.equalsIgnoreCase("")){

            // cada alumno va separado por la barra /
            // crear un vector con los alumnos sabiendo que están separados por "/"
            String [] cargarDatos=respuesta.split("/");

            // recorrer el vector de alumnos cargarDatos
            for (int i = 0; i < cargarDatos.length; i++) {

                // crear un vector con la información del alumno actual cargarDatos[i]
                // sabiendo que la información está separada por "<br>"
                final String datosAlumno[]=cargarDatos[i].split("<br>");

                Alumno alumno2 =new Alumno();

                // recoger en alumno2 la información del alumno actual
                alumno2.setAlumno(datosAlumno[0]);
                alumno2.setMatricula(Integer.parseInt(datosAlumno[1].trim()));
                alumno2.setTelefono(Integer.parseInt(datosAlumno[2].trim()));
                alumno2.setEmail(datosAlumno[3]);

                // añadir a la lista de alumnos el alumno2
                listaAlumnos.add(alumno2);
            }

            /* pare depurar el código*/
            aux = "num alumnos = " + cargarDatos.length + " size = " +listaAlumnos.size();

            // para ejecutar en primer plano en el hilo principal
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tostada(aux);
                }
            });

            resul = true;
        }
        else
            resul = false;

        return resul;
    } // fin filtrarDatos()

    //Muestra el alumno almacenado como objeto en nuestro ArrayList
    private void mostrarAlumno (int posicion){
        // guardar en alumno2 la información contenida en la posición "posicion"
        // de la lista de alumnos
        Alumno alumno2=listaAlumnos.get(posicion);

        // Poner la información del alumno en los cuadros de texto
        alumno.setText(alumno2.getAlumno());
        matricula.setText(""+alumno2.getMatricula());
        telefono.setText(""+alumno2.getTelefono());
        email.setText(alumno2.getEmail());
    } // fin mostrarAlumno()

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
	*
	* Este Web Service permitirá insertar un alumno en la BD
	*/
    class WebService_insertar extends AsyncTask<String,String,String> {

        private Activity context;

        WebService_insertar (Activity context){
            this.context=context;
        }

        /* Proceso Invocado en la Interfaz de Usuario (IU) antes de ejecutar la tarea en segundo plano.
		   En este caso, muestra una barra de progreso
		 */
        protected void onPreExecute() {
            // Crea la barra de progreso si es necesario
            if (pDialog == null)
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

    } // fin clase WebService_insertar


    /* Este Web Service permitirá mostrar un alumno de la BD
	*/
    class WebService_mostrar extends AsyncTask<String,String,String> {

        private Activity context;

        WebService_mostrar (Activity context){
            this.context=context;
        }

        /* Proceso Invocado en la Interfaz de Usuario (IU) antes de ejecutar la tarea en segundo plano.
		   En este caso, muestra una barra de progreso
		 */
        protected void onPreExecute() {
            // Crea la barra de progreso si es necesario
            if (pDialog == null)
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

            if(filtrarDatos()) {
                // hay un alumno que mostrar
                resultado = "OK";
            }
            else
                // no hay alumno que mostrar
                resultado = "ERROR";

            return resultado;
        }

        /* Una vez terminado doInBackground según lo que halla ocurrido
		   intentamos mostrar el alumno */
        protected void onPostExecute (String result) {

            pDialog.dismiss();//ocultamos barra de progreso

            if (result.equals("OK")) {
                // se puede mostrar el alumno
                mostrarAlumno(posicion);
                //tostada("muestra alumno");
            }
            else
                tostada("ERROR, no hay más alumnos que mostrar");
        } // fin onPostExecute()

    } // fin clase WebService_mostrar

}