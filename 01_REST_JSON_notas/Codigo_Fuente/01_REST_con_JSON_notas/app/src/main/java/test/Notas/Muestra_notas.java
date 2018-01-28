package test.Notas;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/*PANTALLA DE BIENVENIDA*/
public class Muestra_notas extends Activity {
	// atributos
	String user;
	TextView txt_usr, logoff;

	public void onCreate (Bundle savedInstanceState) {

	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.lay_screen);

		txt_usr= (TextView) findViewById(R.id.usr_name);
		logoff= (TextView) findViewById(R.id.logoff);

		// valor para la nota incorrecta o no introducida
		int nota = -1;

		//Obtenemos datos enviados en el intent => alumno y su nota
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		   user  = extras.getString("alumno");
		   nota = extras.getInt("nota");
		}else{
		   user="error";
		}

		// comprobar si la nota es correcta o no
		if (nota != -1)
			// nota correcta
			txt_usr.setText(user+", tu nota es: " + nota);//cambiamos texto al nombre del usuario logueado
		else
			// nota incorrecta
			txt_usr.setText(user+", no tienes la nota puesta");

		logoff.setOnClickListener(new View.OnClickListener(){

			public void onClick(View view){
			//'cerrar  sesion' nos regresa a la ventana anterior.
				finish();
			}
		});

	} // fin onCreate()
	 
	//Definimos que para cuando se presione la tecla BACK no volvamos para atrás
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)  {
		boolean resul = true;

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// no hacemos nada.
			resul = true;
		}
		else
			resul = super.onKeyDown(keyCode, event);

		return resul;
	 }
	
	
}
