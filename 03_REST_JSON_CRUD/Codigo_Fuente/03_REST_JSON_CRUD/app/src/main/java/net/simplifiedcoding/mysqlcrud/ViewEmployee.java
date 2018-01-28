package net.simplifiedcoding.mysqlcrud;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewEmployee extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextDesg;
    private EditText editTextSalary;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);

        // obtiene el intent que lanzo esta actividad => ViewAllEmployee
        Intent intent = getIntent();

        // Guarda el id del empleado con el que se lanzo esta actividad con el intent
        id = intent.getStringExtra(Config.EMP_ID);

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDesg = (EditText) findViewById(R.id.editTextDesg);
        editTextSalary = (EditText) findViewById(R.id.editTextSalary);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        editTextId.setText(id);

        // obtiene el empleado de la BD con el id especificado
        getEmployee();
    }


    // obtiene un empleado de la BD
    private void getEmployee(){

        // definimos la clase GetEmployee que obtiene un empleado en la BD
        // para que se lancen sus tareas en segundo plano
        class GetEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading; // barra de progreso

            // antes de lanzar la tarea en 2º plano (doInBackground) define que hacer:
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // muestra una barra de progreso circular
                loading = ProgressDialog.show(ViewEmployee.this,"Fetching...","Wait...",false,false);
            }

            // después de terminar la tarea en 2º plano define que hacer:
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // oculta la barra de progreso
                loading.dismiss();
                // muestra los datos del empleado actualizando los valores de los EditText
                showEmployee(s);
            }

            // proceso que se ejecuta en 2º plano
            @Override
            protected String doInBackground(Void... params) {
                // Crear una instancia de RequestHandler para hacer una petición GET con parámetros
                RequestHandler rh = new RequestHandler();

                // realiza una petición GET que manda al script PHP getEmp.php
                // y obtiene la respuesta del servidor HTTP
                String s = rh.sendGetRequestParam(Config.URL_GET_EMP,id);

                return s;
            }
        }

        GetEmployee ge = new GetEmployee();

        // Ejecuta los métodos de la clase GetEmployee
        // => onPreExecute() antes de empezar la tarea en 2º plano
        // => doInBackground() => tarea en 2º plano
        // => y onPostExecute => después de acabar doInBackground()
        ge.execute();

    } // fin getEmployee()

    // muestra los datos del empleado actualizando los valores de los EditText
    private void showEmployee(String json){
        try {
            // crea un objeto JSON con el String json
            JSONObject jsonObject = new JSONObject(json);
            // crea el Array JSON con el objeto JSON
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            // recoger el primero de los elementos del vector JSON
            JSONObject c = result.getJSONObject(0);
            // Guardar el nombre, puesto y salario del trabajor con el objeto JSON
            String name = c.getString(Config.TAG_NAME);
            String desg = c.getString(Config.TAG_DESG);
            String sal = c.getString(Config.TAG_SAL);

            // Actualizar los valores de los cuadros de texto
            editTextName.setText(name);
            editTextDesg.setText(desg);
            editTextSalary.setText(sal);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Actualizar la información de un empleado
    private void updateEmployee(){
        // recoger la información del empleado de los cuadros de texto
        final String name = editTextName.getText().toString().trim();
        final String desg = editTextDesg.getText().toString().trim();
        final String salary = editTextSalary.getText().toString().trim();

        // definimos la clase UpdateEmployee que actualiza la información de un empleado en la BD
        // para que se lancen sus tareas en segundo plano
        class UpdateEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;

            // antes de lanzar la tarea en 2º plano (doInBackground) define que hacer:
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // barra de progreso circular
                loading = ProgressDialog.show(ViewEmployee.this,"Updating...","Wait...",false,false);
            }

            // después de terminar la tarea en 2º plano define que hacer:
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // ocultar barra de progreso
                loading.dismiss();
                // muestra una tostada con la cadena "s" que le ha pasado
                // con el return res => doInBackground()
                Toast.makeText(ViewEmployee.this,s,Toast.LENGTH_LONG).show();
            }

            // proceso que se ejecuta en 2º plano
            @Override
            protected String doInBackground(Void... params) {
                // Crea un objeto HashMap
                HashMap<String,String> hashMap = new HashMap<>();

                // formando las parejas clave/valor que necesita para la solicitud POST de actualización
                hashMap.put(Config.KEY_EMP_ID,id);     // id => único valor que no cambiará
                hashMap.put(Config.KEY_EMP_NAME,name); // nombre
                hashMap.put(Config.KEY_EMP_DESG,desg); // puesto de trabajo
                hashMap.put(Config.KEY_EMP_SAL,salary); // salario

                // crea una instancia de la clase RequestHandler
                // que se necesita para realizar la petición POST
                RequestHandler rh = new RequestHandler();

                // mandar la petición POST al script updateEmp.php y obtener su resultado
                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();

        // Ejecuta los métodos de la clase UpdateEmployee
        // => onPreExecute() antes de empezar la tarea en 2º plano
        // => doInBackground() => tarea en 2º plano
        // => y onPostExecute => después de acabar doInBackground()
        ue.execute();

    } // fin updateEmployee()

    // borrar el empleado  actual de la BD
    private void deleteEmployee(){

        // definimos la clase DeleteEmployee que borra a un empleado en la BD
        // para que se lancen sus tareas en segundo plano
        class DeleteEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewEmployee.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewEmployee.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                // manda a borrar al empleado actual "id" mediante GET con el script "deleteEmp.php"
                String s = rh.sendGetRequestParam(Config.URL_DELETE_EMP, id);
                return s;
            }
        }

        DeleteEmployee de = new DeleteEmployee();
        de.execute();

    } // fin deleteEmployee()


    // Pregunta al usuario si quiere borrar o no al empleado actual
    // en caso de que responda Yes => lo borra
    private void confirmDeleteEmployee(){
        // Definir el cuadro de diálogo
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this employee?");

        // Si el usuario pulsa el botón del "Yes"
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // borrar al empleado actual
                        deleteEmployee();
                        // lanza la actividad para ver todos los empleados
                        startActivity(new Intent(ViewEmployee.this,ViewAllEmployee.class));
                    }
                });

        // Si el usuario pulsa el botón del "Yes" => no hace nada
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            // actualizar la información del empleado actual
            updateEmployee();
        }

        if(v == buttonDelete){
            // Confirma si quieres borrar o no el empleado actual
            confirmDeleteEmployee();
        }
    }
}
