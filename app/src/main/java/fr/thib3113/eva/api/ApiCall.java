package fr.thib3113.eva.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.thib3113.eva.MainActivity;
import fr.thib3113.eva.MyTts;

/**
 * Created by thibaut on 16/03/2015.
 */
public class ApiCall extends AsyncTask<Void, Void, Void> {
    public ArrayList<String> functionList = new ArrayList<String>();
    public Activity activity = null;
    public String url = null;

    public  String api_url = "http://myraspi.thib3113.fr/EVA/api/v";
    public  String api_version = "1";


    public String version = null;
    public List<String> Widget;

    public JSONObject jsonObj;
    public String str = null;
    public ProgressDialog pDialog;
    public MyTts tts;
    public boolean tts_initialized;
    private Map<String, String> result_api;

    public ApiCall(MainActivity activity){
        functionList.add("ping");// put(5, "yes");
        this.setUrl(api_url + api_version);
        this.setActivity(activity);
        this.setTts(activity.tts);
        this.setTts_initialized(activity.tts_initilized);
    }

    public void setTts_initialized(boolean nTts_initialized){
        tts_initialized = nTts_initialized;
    }

    public void setTts(MyTts ntts){
        tts = ntts;
    }

    public void setVersion(String version){
            this.version = version;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public void setUrl(String URL){
        this.url = URL;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        try {
            if(activity == null)
                throw new Error();
        } catch (Exception e) {
            System.out.println("activity is empty");
            e.printStackTrace();
        }

        try {
            if(url == null)
                throw new Error();
        } catch (Exception e) {
            System.out.println("url is empty");
            e.printStackTrace();
        }

        if(!isOnline()) {
            Toast.makeText(activity.getApplicationContext(), "Problème de connection internet", Toast.LENGTH_LONG).show();
            this.cancel(true);
            return;
        }

        // Showing progress dialog
            pDialog = new ProgressDialog(ApiCall.this.activity);
            pDialog.setMessage("Vérification de l'API");
            pDialog.setCancelable(false);
            pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // Creating service handler class instance
        ApiFunction a = new ApiFunction(this.url);
        for (String function:functionList) a.call(this, function);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        pDialog.hide();

        String out_str = "Erreur inconnue";
        System.out.println(result_api);
        if(this.version != null) {
            out_str = "Connection au serveur réussie";
        }
        else {
            if (!isOnline()) {
                out_str = "vous n'etes pas connecté à internet";
            } else {
                out_str = "La connection à votre serveur à échoué";
            }
        }

        tts.speak(out_str);
//        if(this.tts_initialized){
//            if(Build.VERSION.SDK_INT >= 21 ){
//                tts.speak((CharSequence) out_str, TextToSpeech.QUEUE_ADD, new Bundle(), TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
//            }
//            else{
//                tts.speak(out_str, TextToSpeech.QUEUE_ADD, null);
//            }
//        }
//        else{
//            Toast.makeText(activity.getApplicationContext(), out_str, Toast.LENGTH_LONG).show();
//        }
//        System.out.println(jsonObj.toString());
    }

    public Activity getActivity() {
        return activity;
    }
}
