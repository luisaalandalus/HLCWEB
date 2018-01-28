package net.simplifiedcoding.mysqlcrud;

/* Código de la página web:
https://www.simplifiedcoding.net/android-mysql-tutorial-to-perform-basic-crud-operation/
y adaptado y comentado por Ramón José Martínez Cuevas */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Definiendo views
    private EditText editTextName;
    private EditText editTextDesg;
    private EditText editTextSal;

    private Button buttonAdd;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializando views
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDesg = (EditText) findViewById(R.id.editTextDesg);
        editTextSal = (EditText) findViewById(R.id.editTextSalary);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        //Estableciendo listeners para los botones
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
    }


    // Añadiendo un empleado a la BD
    private void addEmployee(){

        final String name = editTextName.getText().toString().trim();
        final String desg = editTextDesg.getText().toString().trim();
        final String sal = editTextSal.getText().toString().trim();

        // definimos la clase AddEmployee que añade un empleado en la BD
        // para que se lancen sus tareas en segundo plano
        class AddEmployee extends AsyncTask<Void,Void,String>{

            // barra de progreso
            ProgressDialog loading;

            // antes de lanzar la tarea en 2º plano (doInBackground) define que hacer:
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // muestra una barra de progreso circular
                loading = ProgressDialog.show(MainActivity.this,"Adding...","Wait...",false,false);
            }

            // después de terminar la tarea en 2º plano define que hacer:
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // oculta la barra de progreso
                loading.dismiss();
                // muestra una tostada con la cadena "s" que le ha pasado
                // con el return res => doInBackground()
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            // proceso que se ejecuta en 2º plano
            @Override
            protected String doInBackground(Void... v) {
                // Crear un nuevo objeto HashMap
                HashMap<String,String> params = new HashMap<>();

                // formando las parejas clave/valor que necesita para la solicitud POST
                params.put(Config.KEY_EMP_NAME,name); // nombre del empleado a insertar
                params.put(Config.KEY_EMP_DESG,desg); // puesto de trabajo
                params.put(Config.KEY_EMP_SAL,sal);   // salario

                // crea una instancia de la clase RequestHandler
                // que se necesita para realizar la petición POST
                RequestHandler rh = new RequestHandler();

                // mandar la petición POST al script addEmp.php y obtener su resultado
                String res = rh.sendPostRequest(Config.URL_ADD, params);

                return res;
            }

        } // fin de la clase AddEmployee que hereda de AsyncTask

        // creamos un objeto de la clase AddEmployee
        AddEmployee ae = new AddEmployee();

        // Ejecuta los métodos de la clase AddEmployee
        // => onPreExecute() antes de empezar la tarea en 2º plano
        // => doInBackground() => tarea en 2º plano
        // => y onPostExecute => después de acabar doInBackground()
        ae.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            // añadir un empleado
            addEmployee();
        }

        if(v == buttonView){
            // lanzar la actividad que muestra con un ListView todos los empleados
            startActivity(new Intent(this,ViewAllEmployee.class));
        }
    }
}
