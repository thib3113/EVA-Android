package fr.thib3113.eva;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    String[] tab = {"membre1","membre2","membre3","membre4","membre5","membre6","membre7","membre8","membre9","membre10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        //on tente de récupéré qqc
//        try {
            Toast.makeText(this, "connection", Toast.LENGTH_LONG).show();
            System.out.println("connection");
            ApiCall a = new ApiCall();
            a.setActivity(this);
            a.execute();

        //Récuperation de ListView crée
//        ListView lv = (ListView) findViewById(R.id.widgetList);
        //création d'un ArrayAdapter
//        ArrayAdapter arrayadp=new ArrayAdapter(this,  android.R.layout.simple_list_item_1, tab);
        // associer l'adaptateur à la listeView
//        lv.setAdapter(arrayadp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }



    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }
}
