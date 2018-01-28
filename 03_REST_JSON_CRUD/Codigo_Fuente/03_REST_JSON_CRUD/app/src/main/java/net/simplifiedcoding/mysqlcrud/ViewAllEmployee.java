package net.simplifiedcoding.mysqlcrud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewAllEmployee extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_employee);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        // obtener el vector JSON de todos los empleados
        getJSON();
    }


    // Muestra todos los empleados obtenidos del vector JSON en un ListView
    private void showEmployee(){
        // Define un objeto JSON
        JSONObject jsonObject = null;
        // Crea ArrayList de HashMap de dos string => que tendra el id y el nombre del empleado
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            // crea un objeto JSON en base a JSON_STRING
            // => cadena con el vector JSON de todos los empleados
            jsonObject = new JSONObject(JSON_STRING);

            // obtiene  un array JSON (JSONArray) a partir del objeto JSON
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            // recorrer el vector JSON de todos los empleados
            for(int i = 0; i<result.length(); i++){
                // obtener el objeto JSON de la posición i del vector
                JSONObject jo = result.getJSONObject(i);
                // guardar el id del empleado
                String id = jo.getString(Config.TAG_ID);
                // guardar el nombre del empleado
                String name = jo.getString(Config.TAG_NAME);

                // Crear un nuevo objeto HashMap
                HashMap<String,String> employees = new HashMap<>();
                // añadir al HashMap employees la clave/valor => id/valor
                employees.put(Config.TAG_ID,id);
                // añadir al HashMap employees la clave/valor => nombre/valor
                employees.put(Config.TAG_NAME,name);
                // añadir el HashMap del empleado al ArrayList
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear el adaptador del ListView que contendrá el id y el nombre del empleado
        ListAdapter adapter = new SimpleAdapter(
                ViewAllEmployee.this, list, R.layout.list_item,
                new String[]{Config.TAG_ID,Config.TAG_NAME},
                new int[]{R.id.id, R.id.name});

        // Establecer el adaptador anterior en el ListView
        listView.setAdapter(adapter);

    } // fin ShowEmployee


    // obtener el vector JSON de todos los empleados
    private void getJSON(){

        // definimos la clase GetJSON que obtiene el vector JSON con todos los empleados
        // para que se lancen sus tareas en segundo plano
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            // antes de lanzar la tarea en 2º plano (doInBackground) define que hacer:
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // muestra una barra de progreso circular
                loading = ProgressDialog.show(ViewAllEmployee.this,"Fetching Data","Wait...",false,false);
            }

            // después de terminar la tarea en 2º plano define que hacer:
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // ocultar barra de progreso
                loading.dismiss();
                // guarda la cadena o String s que le ha pasado doInBackground()
                // que es la cadena con el vector JSON de todos los empleados
                JSON_STRING = s;
                showEmployee();
            }

            // tarea que se ejecuta en 2º plano
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                // Manda una petición GET con el script getAllEmp.php
                // para obtener todos los empleados como un vector JSON
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }

        GetJSON gj = new GetJSON();

        // Ejecuta los métodos de la clase GetJSON
        // => onPreExecute() antes de empezar la tarea en 2º plano
        // => doInBackground() => tarea en 2º plano
        // => y onPostExecute => después de acabar doInBackground()
        gj.execute();
    }

    // Al hacer click sobre un elemento del ListView
    // => lanza la actividad ViewEmployee => ver el empleado con el id seleccionado
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Define que el intent va a lanzar la actividad => ViewEmployee
        Intent intent = new Intent(this, ViewEmployee.class);
        // Guarda el HashMap (id y nombre empleado) del elemento del ListView seleccionado
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);

        // Obtiene el id del empleado
        String empId = map.get(Config.TAG_ID);

        // guarda la información del id del empleado para que sea mandada con el Intent
        intent.putExtra(Config.EMP_ID,empId);

        // Lanzar la actividad del Intent
        startActivity(intent);
    }
}
