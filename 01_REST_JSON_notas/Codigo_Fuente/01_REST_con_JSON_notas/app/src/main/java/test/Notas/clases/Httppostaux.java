package test.Notas.clases;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/*CLASE AUXILIAR PARA EL ENVIO DE PETICIONES A NUESTRO SISTEMA Y MANEJO DE RESPUESTA.*/
public class Httppostaux {

    // Atributos
    InputStream is = null; // Flujo de entrada => respuesta del servidor
    String result = "";    // respuesta del servidor en formato String

    // Entrada: parameters => lista con las parejas (clave,valor) que se envían por POST
    //          urlwebserver => URL del script del servidor, por ejemplo de un script PHP
    // Salida: array JSON con la respuesta del servidor a la petición HTTP y POST
    public JSONArray getserverdata (ArrayList<NameValuePair> parameters, String urlwebserver) {

        JSONArray arrayjson = null;

        // conecta via http y envia la información (clave, valor) con un POST
        // la información obtenida queda grabada en el flujo de entrada => is
        httppostconnect(parameters, urlwebserver);

        if (is != null) {
            // hubo una respuesta por parte del servidor

            // convierte la respuesta del servidor => is a formato cadena (String) => result
            getpostresponse();

            // transforma la respuesta del servidor => String => en un vector JSON => JSONArray
            arrayjson = getjsonarray();

        } else
            arrayjson = null;

        return arrayjson;

    } // fin getserverdata()


    // peticion HTTP
    // Entrada: parameters => lista con las parejas (clave,valor) que se envían por POST
    //          urlwebserver => URL del script del servidor, por ejemplo de un script PHP
    // Salida: nada, queda grabada  la respuesta del servidor a la petición HTTP y POST
    //         en el flujo de entrada => is
    private void httppostconnect(ArrayList<NameValuePair> parametros, String urlwebserver) {

        // para generar bloque try - cath en Android Studio => Ctrl + Alt + T
        try {
            // creación de un cliente HTTP por defecto
            HttpClient httpclient = new DefaultHttpClient();

            // creamos el objeto httpost para realizar una solicitud POST
            // al script contenido en urlwebserver
            HttpPost httppost = new HttpPost(urlwebserver);

            // establece la entidad => como una lista de pares URL codificada.
            // Esto suele ser útil al enviar una solicitud HTTP POST
            httppost.setEntity(new UrlEncodedFormEntity(parametros));
            //ejecuto petición enviando datos por POST
            HttpResponse response = httpclient.execute(httppost);
            // obtiene la entidad del mensaje de respuesta HTTP
            HttpEntity entity = response.getEntity();
            // crea un nuevo flujo de entrada tipo InputStream => is => con la entidad HTTP => entity
            is = entity.getContent();

        } catch (UnsupportedEncodingException e) {
            Log.e("log_tag", "Error la codificación por defecto no es soportada " + e.toString());
            e.printStackTrace();

        } catch (org.apache.http.client.ClientProtocolException e) {
            Log.e("log_tag", "Error en el protocolo http: " + e.toString());
            e.printStackTrace();

        } catch (IOException e) {
            Log.e("log_tag", "Error en la conexión http " + e.toString());
            e.printStackTrace();

        } catch (UnsupportedOperationException e) {
            Log.e("log_tag", "Error el contenido de la entidad no puede ser representada como InputStream " + e.toString());
            e.printStackTrace();
        }


    } // fin httppostconnect()

    // convierte la respuesta del servidor => is => a formato cadena (String) => result
    public void getpostresponse() {

        BufferedReader reader = null;

        //Convierte respuesta a String
        try {
            // crear un flujo de entrada de tipo BufferedReader en base
            // a un flujo de entrada InputStreamReader con un juego de caracteres de tipo "iso-8859-1"
            // el tamaño del buffer es de 8 caracteres
            reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            // quién no sepa que es un StringBuilder =>
            // http://picandocodigo.net/2010/java-stringbuilder-stringbuffer/

            // crea una cadena de caracteres modificable => StringBuilder
            StringBuilder sb = new StringBuilder();

            // lee todas las líneas del fichero a través del flujo de entrada reader
            String line = null;
            while ((line = reader.readLine()) != null) {
                // añade cada línea leída del fichero con un salto de línea => "\n"
                sb.append(line + "\n");
            }

            // guardamos el resultado de la respuesta en el String => result
            result = sb.toString();

            Log.e("getpostresponse", " result= " + sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("log_tag", "Error E/S al convertir el resultado " + e.toString());

        } finally {
            // la clausula finally siempre se ejecuta => salten excepciones o no
            // por eso es conveniente intentar cerrar aquí los flujos => por si hay un error
            // en la lectura del flujo por ejemplo de tipo E/S => IOException
            try {
                is.close(); // cerrar el flujo de entrada is
                reader.close(); // cerrar el flujo de entrada reader
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("log_tag", "Error E/S al cerrar los flujos de entrada " + e.toString());
            }
        }

    } // fin getpostresponse()


    // Entrada: nada
    // devuelve un array JSON (JSONArray) a partir del String result
    public JSONArray getjsonarray() {
        JSONArray jArray = null;
        //parse json data
        try {
            // transforma la cadena result en un JSONArray
            jArray = new JSONArray(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error al transformar a vector JSON el String result " + e.toString());
            jArray = null;
        }

        return jArray;

    } // fin getjsonarray()


} // fin clase Httppostaux
  


	  
	  
