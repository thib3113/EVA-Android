package fr.thib3113.eva;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fr.thib3113.eva.api.ApiCall;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private CharSequence mTitle;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static MyTts tts;
    public static boolean tts_initilized = false;
    private static Context context;
    ApiCall api_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = getApplicationContext();
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        tts = new MyTts(this);

        //on tente de récupéré qqc
        Toast.makeText(this, "connection", Toast.LENGTH_LONG).show();
        System.out.println("connection");
        api_call = new ApiCall(this);
        api_call.setActivity(this);
        api_call.setTts(tts);
        api_call.execute();
    }

    @Override
    protected void onDestroy() {

        if(tts.getmTts() != null){
            tts.stop();
            tts.shutdown();
        }

        api_call.cancel(true);
        super.onDestroy();
    }

    public MyTts getTts(){
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
        Toast.makeText(this, " item = "+position, Toast.LENGTH_LONG);
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        startActivity(new Intent(this, SettingsActivity.class));
        super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    public static Context getAppContext() {
        return context;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.dashboard);
                break;
            case 2:
                mTitle = getString(R.string.vocal);
                break;
            case 3:
                mTitle = getString(R.string.settings);
                break;
        }
    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Bundle args = getArguments();
            switch (args.getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_dashbaord, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_vocal, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_setting, container, false);
                    break;
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
