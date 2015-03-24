package fr.thib3113.eva;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks,OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private CharSequence mTitle;
    private User MyUser = new User();
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static MyTts tts;
    public static boolean tts_initilized = false;
    private static Context context;
    private static Activity activity;
    ApiCall api_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
        getFragmentManager().findFragmentById(R.id.fragment_drawer);


        this.context = getApplicationContext();
        activity = this;
        tts = new MyTts(this);

        if(!MyUser.isConnect()){
            findViewById(R.id.drawer).setVisibility(View.INVISIBLE);
            MyUser.tryToConnect("test", "test");
        }
        else{
            // Set up the drawer.
            mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
            //on tente de se connecté au serveur
            api_call = new ApiCall(this);
            api_call.setActivity(this);
            api_call.setTts(tts);
            api_call.execute();
        }



    }

    @Override
    protected void onDestroy() {

        if(tts.getmTts() != null){
            tts.stop();
            tts.shutdown();
        }

        if(api_call != null){
            api_call.cancel(true);
        }
        super.onDestroy();
    }

    public MyTts getTts(){
        return tts;
    }
    
    public boolean gettts_initilized(){
        return tts_initilized;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
//        if(MyUser == null || !MyUser.isConnect()){
//            Toast.makeText(this,"Vous devez configurer l'application avant de changer de catégorie", Toast.LENGTH_LONG).show();
//            return;
//        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        Toast.makeText(this," "+position, Toast.LENGTH_LONG).show();
    }

    public static Context getAppContext(){
        return MainActivity.context;
    }

    public static Activity getActivity() {
        return activity;
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


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                    rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
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
