package fr.thib3113.eva;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.util.Locale;

import fr.thib3113.eva.api.ApiCall;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    String[] tab = {"membre1","membre2","membre3","membre4","membre5","membre6","membre7","membre8","membre9","membre10"};
    public static TextToSpeech tts;
    public static boolean tts_initilized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        //on initialise la synthèse vocale
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.FRENCH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        tts_initilized = true;
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });



        //on tente de récupéré qqc
        Toast.makeText(this, "connection", Toast.LENGTH_LONG).show();
        System.out.println("connection");
        ApiCall a = new ApiCall(this);
        a.setActivity(this);
        a.setTts(tts);
        a.execute();
    }

    public TextToSpeech getTts(){
        return tts;
    }
    
    public boolean gettts_initilized(){
        return tts_initilized;
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
