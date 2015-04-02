package fr.thib3113.eva;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import fr.thib3113.eva.api.ApiCall;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks,OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private CharSequence mTitle;
    private User MyUser = new User();
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static MyTts tts;
    private static Context context;
    private static Activity activity;
    ApiCall api_call;
    public Handler config_handler;

    public  String api_url = "api/v";
    public  String api_version = "1";
    public int step = 0;

    //configurations
    public static String config_localeIp;
    public static String config_distantIp;
    public static String config_username;
    public static String config_password;

    private static final int CONFIGURE_ID = 100;

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


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // then you use
        config_localeIp = prefs.getString("localeIp", null);
        config_distantIp = prefs.getString("distantIp", null);
        config_username = prefs.getString("username", null);
        config_password = prefs.getString("password", null);

        if(config_username != null){
            config(config_localeIp,  config_distantIp,  config_username,  config_password);
        }


        if (config_username != null) {
//            findViewById(R.id.drawer).setVisibility(View.INVISIBLE);

            api_call = new ApiCall(this);
            api_call.setTts(tts);
//            api_call.url = "http://myraspi.thib3113.fr/EVA/api/v1";
//            MyUser.tryToConnect(api_call,"test", "test");
            DrawerLayout d = (DrawerLayout) findViewById(R.id.drawer);
            d.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(100))
                    .commit();


        } else {
            init_handler.sendMessage(init_handler.obtainMessage());
        }

    }

    public String extract(EditText s){
        return s.getText().toString();
    }

    public void checkConfig(View view) {
        Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show();
        //get EditText
        String localeip = extract((EditText) getActivity().findViewById(R.id.localeip));
        String distantip = extract((EditText) getActivity().findViewById(R.id.distantip));
        String username = extract((EditText) getActivity().findViewById(R.id.username));
        String password = extract((EditText) getActivity().findViewById(R.id.passwd));

        String[] mStrings  =  {localeip, distantip, username, password};
        boolean one_empty = false;
        for( int i=0; i < mStrings.length; i++){
            if(mStrings[i].length() < 1){
                one_empty = true;
            }
        }

        if(one_empty){
            Toast.makeText(MainActivity.getAppContext(), "Vous devez remplir tous les champs", Toast.LENGTH_LONG).show();
            return;
        }

        ((MainActivity)getActivity()).config(localeip,  distantip,  username,  password);
         config_handler.sendMessage(config_handler.obtainMessage());
    }

    public String formatUrl(String url){
        //ajout du http au début
        if(url.substring(0, 7) != "http://"){
            if(url.substring(0, 8) != "https://"){
                if(url.substring(0, 2) != "//")
                    url = "http://"+url;
                else
                    url = "http:"+url;
            }
        }

        return (url.substring(url.length()-1) == "/")? url : url+"/";
    }

    public void config(String localeip, String distantip, final String username, final String password){
        final int LOCALE_IP_TEST = 0;
        final int LOCALE_IP_RESULT = 1;
        final int DISTANT_IP_TEST = 2;
        final int DISTANT_IP_RESULT = 3;
        final int USER_TEST = 4;
        final int USER_RESULT = 5;
        step = LOCALE_IP_TEST;
        localeip = formatUrl(localeip);
        distantip = formatUrl(distantip);

        final String finalLocaleip = localeip;
        final String finalDistantip = distantip;
        config_handler = new Handler(){
            String url = null;
            @Override
            public void handleMessage(Message msg) {

                switch (step){
                    case LOCALE_IP_TEST :
                        System.out.println("LOCALE_IP_TEST");
                        step=LOCALE_IP_RESULT;
                        api_call = new ApiCall((MainActivity)getActivity());
                        api_call.setTts(((MainActivity) getActivity()).getTts());
                        api_call.url = finalLocaleip +api_url+api_version;
                        api_call.addToFunctionQueue("ping");
                        api_call.setProgressMessage("Test de l'EVA locale");
                        api_call.handler = config_handler;

                        api_call.execute();
                    break;
                    case LOCALE_IP_RESULT :
                        System.out.println("LOCALE_IP_RESULT");
                        try{
                            System.out.println(api_call.result_api.toString());
                            String status = api_call.result_api.get("status");
                            if(status.equals("true")){
                                step=USER_TEST;
                                url = finalLocaleip;
                                Toast.makeText(getActivity(), "iplocal work", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                step=DISTANT_IP_TEST;
                            }
                        }
                        catch (NullPointerException e){
                            step=DISTANT_IP_TEST;
                        }
                        this.sendMessage(this.obtainMessage());
                        break;
                    case DISTANT_IP_TEST:
                        System.out.println("DISTANT_IP_TEST");
                        step = DISTANT_IP_RESULT;
                        api_call = new ApiCall((MainActivity)getActivity());
                        api_call.setTts(((MainActivity) getActivity()).getTts());
                        api_call.url = finalDistantip +api_url+api_version;
                        api_call.setProgressMessage("Test de l'EVA distante");
                        api_call.addToFunctionQueue("ping");
                        api_call.handler = config_handler;
                        api_call.execute();
                        break;
                    case DISTANT_IP_RESULT:
                        System.out.println("DISTANT_IP_RESULT");
                        try{
                            String status = api_call.result_api.get("status");
                            System.out.println(api_call.result_api.toString());
                            if(status.equals("true")){
                                step=USER_TEST;
                                url = finalDistantip;
                                Toast.makeText(getActivity(), "IpDistant work", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                step=USER_TEST;
                            }
                        }
                        catch (NullPointerException e){
                            step=USER_TEST;
                        }
                        this.sendMessage(this.obtainMessage());
                        break;
                    case USER_TEST:
                        System.out.println("USER_TEST");
                        step = USER_RESULT;
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("user", username));
                        params.add(new BasicNameValuePair("pass", password));
                        params.add(new BasicNameValuePair("remember_me", "true"));
                        api_call = new ApiCall((MainActivity)getActivity());
                        api_call.setProgressMessage("Tentative de connection");
                        api_call.addToFunctionQueue("connect", params);
                        api_call.handler = config_handler;
                        api_call.url = url+api_url+api_version;
                        api_call.execute();
                        System.out.println("next step : " + step);
//                        this.sendMessage(this.obtainMessage());
                        break;
                    case USER_RESULT:
                        System.out.println("USER_RESULT");

                        String out_str = "Le nom d'utilisateur et/ou le mot de passe ne correspondent pas";
                        try{
                            System.out.println(api_call.result_api.toString());
                            String status = api_call.result_api.get("status");
                            if(status == "true"){
                                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getAppContext()).edit();
                                prefs.putString("localeIp", finalLocaleip);
                                prefs.putString("distantIp", finalDistantip);
                                prefs.putString("username", username);
                                prefs.putString("password", password);
                                prefs.commit();
                                out_str = "Vos paramètres sont enregistrés";
                                init_handler.sendMessage(init_handler.obtainMessage());

                            }
                            else{
                                System.out.println("USER_TEST");

                            }
                        }
                        catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), out_str, Toast.LENGTH_LONG).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }


    Handler init_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Set up the drawer.
            DrawerLayout d = (DrawerLayout) findViewById(R.id.drawer);
            d.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        }
    };

    public void makeToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

//    public void checkConfig(View v){
//        ArrayList<EditText> myEditTextList = new ArrayList<EditText>();
//
//        for( int i = 0; i < v.getParent().this.getParent().child(); i++ )
//            if( myLayout.getChildAt( i ) instanceof EditText )
//                myEditTextList.add( (EditText) myLayout.getChildAt( i ) );
//        System.out.println(v.toString());
//    }

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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
//        Toast.makeText(this," "+position, Toast.LENGTH_LONG).show();
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
            case CONFIGURE_ID:
                mTitle = getString(R.string.config);
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
                    rootView = inflater.inflate(R.layout.fragment_settings, container, false);
                    break;
                case CONFIGURE_ID:
                    rootView = inflater.inflate(R.layout.fragment_settings, container, false);
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
