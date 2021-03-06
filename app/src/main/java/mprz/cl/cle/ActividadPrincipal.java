package mprz.cl.cle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.HashMap;

import mprz.cl.cle.fragments.Doctrina;
import mprz.cl.cle.fragments.Herramientas;
import mprz.cl.cle.fragments.Home;
import mprz.cl.cle.fragments.Login;
import mprz.cl.cle.fragments.MisEncuestas;
import mprz.cl.cle.fragments.Mision;
import mprz.cl.cle.fragments.Organica;
import mprz.cl.cle.fragments.misEvaluadores;
import mprz.cl.cle.util.SQLiteEncuestasHandler;
import mprz.cl.cle.util.SQLiteHandler;
import mprz.cl.cle.util.SessionManager;


public class ActividadPrincipal extends AppCompatActivity {

    private TextView saludo;
    DrawerLayout drawerLayout;
    private SessionManager session;
    private SQLiteHandler db;
    private SQLiteEncuestasHandler dbEncuestados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Fresco.initialize(this);
        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());
        dbEncuestados = new SQLiteEncuestasHandler(getApplicationContext());


        if (savedInstanceState == null) {
            Fragment fragment = new Home();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.content_fragment, fragment)
                    .commit();
        }

        inicializarToolbar();
        setearMenu();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Desea salir de la applicacion?")
                .setCancelable(true)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActividadPrincipal.this.finish();
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializarToolbar() {


        //App bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Noticias");

        final ActionBar ab = getSupportActionBar();

        if(ab != null){
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setearMenu() {

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        HashMap<String, String> dataUser = db.getUserDetails();
        String name = dataUser.get("nombre")+" "+dataUser.get("paterno")+" "+dataUser.get("materno");
        NavigationView nv = (NavigationView)findViewById(R.id.nav_view);
        /*TextView nombre = (TextView)findViewById(R.id.hnav_username);
            nombre.setText(name);*/

        if(nv != null){

            nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    boolean fragmentTransaction =false;
                    Fragment fragment = null;


                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home:
                            fragment = new Home();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_mis_encuestas:
                            if (session.isLoggedIn())
                            {
                                fragment = new MisEncuestas();
                                fragmentTransaction = true;
                            }
                            else{
                                fragment = new Login();
                                Bundle args = new Bundle();
                                args.putString("origen", "misEncuestas");
                                fragment.setArguments(args);
                                fragmentTransaction = true;
                            }
                            break;
                        case R.id.nav_mis_evaluadores:
                            if (session.isLoggedIn())
                            {
                                fragment = new misEvaluadores();
                                fragmentTransaction = true;
                            }
                            else{
                                fragment = new Login();
                                Bundle args = new Bundle();
                                args.putString("origen", "misEvaluadores");
                                fragment.setArguments(args);
                                fragmentTransaction = true;
                            }
                            break;
                        case R.id.nav_mision:
                            fragment = new Mision();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_doctrina:
                            fragment = new Doctrina();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_herramientas:
                            fragment = new Herramientas();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_organica:
                            fragment = new Organica();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_acerca_de:
                            Toast.makeText(ActividadPrincipal.this, "Esta aplicación fue desarrollada a medida para el Ejército de Chile por: CMT y Mprz. \n \r cmartinezt.91@gmail.com \n \r Versión 1.0", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_log_out:
                            if(session.isLoggedIn()){

                                final AlertDialog alert = new AlertDialog.Builder(ActividadPrincipal.this).create();

                                alert.setTitle("¿Desea cerrar la sesión?");
                                alert.setMessage("Al cerrar la sesion eliminará todo progreso en las encuestas que no haya enviado a nuestra base de datos");
                                alert.setIcon(android.R.drawable.ic_dialog_alert);
                                alert.setCancelable(true);
                                alert.setCanceledOnTouchOutside(true);
                                alert.setButton(DialogInterface.BUTTON_POSITIVE, "SI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        alert.dismiss();
                                        session.EliminarUsuarioLogeado();
                                        db.eliminarUsuario();
                                        db.eliminarEvaluadores();
                                        db.recrearTablas();
                                        dbEncuestados.recrearTablas();

                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.content_fragment, new Home())
                                                .commit();
                                        Toast.makeText(ActividadPrincipal.this, "Sesion cerrada", Toast.LENGTH_LONG).show();

                                    }
                                });

                                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        alert.dismiss();
                                    }
                                });
                                alert.show();

                            }
                            break;
                    }
                    if(fragmentTransaction) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_fragment, fragment,"menu")
                                .commit();

                        menuItem.setChecked(true);
                        String titulo = menuItem.getTitle().toString();
                        if(titulo != null){
                            getSupportActionBar().setTitle(titulo);
                        }
                    }

                    drawerLayout.closeDrawers();
//                    Toast.makeText(getApplicationContext(), menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 123){


                MisEncuestas f = (MisEncuestas) getSupportFragmentManager().findFragmentByTag("menu");
                f.obtenerEncuestadosdeWS();

        }
    }
}
