package mprz.cl.cle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.Usuario;

public class Inicio extends AppCompatActivity {

    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

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

        NavigationView nv = (NavigationView)findViewById(R.id.nav_view);
        TextView nombre = (TextView)findViewById(R.id.hnav_username);
            nombre.setText(Usuario.getUserName(Inicio.this));

        if(nv != null){

            nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    boolean fragmentTransaction =false;
                    Fragment fragment = null;


                    switch (menuItem.getItemId())
                    {
                        case R.id.home:
                            break;
                        case R.id.nav_mis_encuestas:
                            fragment = new MisEncuestas();
                            fragmentTransaction = true;
                            break;
                        case R.id.nav_mision:
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
                            Usuario.clearUserName(Inicio.this);
                            finish();
                            break;
                    }
                    if(fragmentTransaction) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_fragment, fragment)
                                .commit();

                        menuItem.setChecked(true);
                        getSupportActionBar().setTitle(menuItem.getTitle());
                    }

                    drawerLayout.closeDrawers();
//                    Toast.makeText(getApplicationContext(), menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
    }
}
