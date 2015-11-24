package mprz.cl.cle;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.HashMap;
import mprz.cl.cle.util.SQLiteHandler;
import mprz.cl.cle.util.SessionManager;


public class ActividadPrincipal extends AppCompatActivity {

    private TextView saludo;
    DrawerLayout drawerLayout;
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());


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
        getSupportActionBar().setTitle("Dashboard");

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
        TextView nombre = (TextView)findViewById(R.id.hnav_username);
            nombre.setText(name);

        if(nv != null){

            nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    boolean fragmentTransaction =false;
                    Fragment fragment = null;


                    switch (menuItem.getItemId())
                    {
                        case R.id.home:
                            fragment = new Home();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_mis_encuestas:
                            fragment = new MisEncuestas();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_mision:
                            fragment = new Mision();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_doctrina:
                            fragment = new Doctrina();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_mis_datos:
                            break;
                        case R.id.nav_acerca_de:
                            break;
                        case R.id.nav_log_out:
                            session.setLogin(false);
                            db.deleteUsers();
                            Intent intent = new Intent(ActividadPrincipal.this, Login.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                    if(fragmentTransaction) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_fragment, fragment)
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
}
