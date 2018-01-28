Pasos para configuración local de PHP en el mismo ordenador donde arrancas el emulador:

La versión de MySQL tiene instalada la versión mejorada de MySQL (La extensión mysqli (mysql improved) permite acceder a la funcionalidad proporcionada por MySQL 4.1 y posterior.) pero no se va a utilizar la extensión mysqli para estos scripts de PHP.

1.-Crear la carpeta con el nombre acceso dentro del directorio /var/www/html/acceso (en el caso de Linux) dentro del servidor apache.

2.- Copiar a la carpeta /var/www/html/acceso (en el caso de Linux) dentro del servidor apache, los siguientes archivos:

acces_nota.html  addnota.html  adduser.php     connectbd.php
acces_nota.php   addnota.php   androidarg.png  funciones_bd.php
acces.php        adduser.html  config.php      login.html

3.-Modificar del archivo config.php  las siguiente lineas:

<?php
/**
 * Database config variables
 */
define("DB_HOST", "localhost");
define("DB_USER", "root");//cambiar por el nombre de usuario definido en la configuracion de la BD.
define("DB_PASSWORD", "123456");//Modificar por el password elegido
define("DB_DATABASE", "notas");//Nombre de la base de datos reemplazar si se utilizó otra diferente
?>


PARA PROBAR QUE LOS SCRIPTS PHP  FUNCIONAN BIEN:

4.- Para insertar alumnos a la BD => http://127.0.0.1/acceso/adduser.html

5.- Por cada alumno insertado hay que introducir su nota con => http://127.0.0.1/acceso/addnota.html

6.- Al acceder a http://127.0.0.1/acceso/login.html deberiamos ver una ventana de login.

Si el nombre del alumnno y contraseña debería mostrar la cadena JSON: [{"logstatus":"1"}]

7.- Para mostrar la nota:

Nota incorrecta => no tiene el alumno nota o no existe en la tabla de calificaciones => muestra la cadena JSON: [{"nota":-1}]

Nota correcta => muestra una nota entre 0 y 10, por ejemplo la cadena JSON:  [{"nota":"7"}]


NOTA: Los scripts de PHP funcionan para esta versión de PHP y compatibles: PHP Version 5.5.9-1ubuntu4.14


COMPROBACIÓN DE QUE EL CÓDIGO de Android Studio FUNCIONA BIEN

IMPORTANTE!!! si vas a probar en un dispositivo real (móvil) o emulador, tendrás que asegurarte que está en la misma red que tu ordenador y que hay conectividad por red, por ejemplo que esté conectado a la misma wifi.

8.- Solo hay que saber cual es la dirección IP de tu ordenador local, en Linux se puede comprobar con ifconfig, o con Windows con ipconfig

wlan0     Link encap:Ethernet  direcciónHW ac:7b:a1:3f:9b:f2  
          Direc. inet:192.168.43.19  Difus.:192.168.43.255  Másc:255.255.255.0
          Dirección inet6: fe80::ae7b:a1ff:fe3f:9bf2/64 Alcance:Enlace
          ACTIVO DIFUSIÓN FUNCIONANDO MULTICAST  MTU:1500  Métrica:1
          Paquetes RX:4242 errores:0 perdidos:0 overruns:0 frame:0
          Paquetes TX:4592 errores:0 perdidos:0 overruns:0 carrier:0
          colisiones:0 long.colaTX:1000 
          Bytes RX:2330171 (2.3 MB)  TX bytes:771241 (771.2 KB)


9.- Cambiar en el código de Android Studio en la clase Acceso.java la línea que tiene la dirección IP del servidor y poner la dir. IP correcta
 No vale poner localhost o 127.0.0.1, ya que el emulador o la máquina

    private final static String URL_SERVIDOR ="192.168.43.19"; // LA IP CAMBIARÁ DE UNA VEZ A OTRA COMPROBAR CON ifconfig

10.- Ejecuta el programa de Android Studio y prueba que funciona


OJO!!!

Cuidado con el cotejamiento o juego de caracteres usados tanto en la base de datos como en las tablas, debes asegurarte que sea UTF8-bin

Por defecto suele ser otro valor distinto, desde PHPMyAdmin puedes cambiar fácilmente el cotejamiento a UTF8-bin (tienes que hacerlo tanto en las tablas y todos sus campos como en la BD), también es bueno el cotejamiento: utf8_unicode_ci

Si no tienes el valor UTF8-bin puede ser que no se guarda bien la información en la BD como en el caso de acentos y eñes, también hay que tener cuidado en el móvil o emulador donde pruebes el programa que permita introducir eñes, acentos, etc... es decir que el teclado sea en español.





