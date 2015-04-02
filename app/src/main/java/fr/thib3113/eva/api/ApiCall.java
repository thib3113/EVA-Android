package fr.thib3113.eva.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.thib3113.eva.MainActivity;
import fr.thib3113.eva.MyTts;

/**
 * Created by thibaut on 16/03/2015.
 */
public class ApiCall extends AsyncTask<Void, Void, Void> {
    public Map<String, List<NameValuePair>> functionList = new HashMap<String ,List<NameValuePair>>();
    public Activity activity = null;
    public String url = null;
    private boolean speak = true;
    public Handler handler = null;

    public  String api_url = "api/v";
    public  String api_version = "1";

    public String distantUrl;
    public String localUrl;
    public String username;
    public String passwd;

    public String version = null;
    public List<String> Widget;

    public JSONObject jsonObj;
    public String str = null;
    private String progressMessage = null;
    public ProgressDialog pDialog;
    public MyTts tts;
    public boolean tts_initialized;
    public Map<String, String> result_api;
    public ApiFunction a;

    public ApiCall(MainActivity activity){
        this.setUrl(api_url + api_version);
        this.setActivity(activity);
        this.setTts(activity.tts);
        this.setTts_initialized(activity.tts.isInit());
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

    public void addToFunctionQueue(String requete, List<NameValuePair> arguments){
        functionList.put(requete, arguments);
    }

    public void addToFunctionQueue(String requete){
        addToFunctionQueue(requete, null);
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
    protected void onCancelled() {
        if(pDialog != null){
            pDialog.dismiss();
        }
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        try {
            if(activity == null)
                throw new Error();
        } catch (Exception e) {
//            System.out.println("activity is empty");
            e.printStackTrace();
        }

        try {
            if(url == null)
                throw new Error();
        } catch (Exception e) {
//            System.out.println("url is empty");
            e.printStackTrace();
        }

        if(!isOnline()) {
            tts.speak("Problème de connection internet");
            this.cancel(true);
            return;
        }

        // Showing progress dialog
            pDialog = new ProgressDialog(this.activity);
            pDialog.setMessage((this.progressMessage != null)? this.progressMessage :"Discussion avec l'API");
            this.progressMessage = null; // on remet le message à 0
            pDialog.setCancelable(false);
            pDialog.show();

    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    @Override
    protected Void doInBackground(Void... arg0) {


        a = new ApiFunction(url, this.activity.getApplicationContext());

        result_api = null;
        for (Map.Entry<String, List<NameValuePair>> function:this.functionList.entrySet()) result_api = a.call(this, function.getKey(), (List)function.getValue());
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        pDialog.hide();

        String out_str = "Erreur inconnue";
//        System.out.println(result_api);
        int last_status_code = 0;

        if(result_api.get("status") == "true") {
            out_str = result_api.get("message");
        }
        else {
            if (!isOnline()) {
                out_str = "vous n'etes pas connecté à internet";
            } else {
                if(a.sh.httpResponse != null){
                    last_status_code = a.sh.httpResponse.getStatusLine().getStatusCode();
                }

                out_str = "La requète à échoué";
                if(last_status_code >= 300){
                    out_str += ", elle as retournée un code http "+last_status_code;
                }
                else{
                    if(result_api.get("message") != null)
                        out_str += ", elle à retournée une erreur \""+result_api.get("error_code")+"\", et un message \""+result_api.get("message")+"\"";
                }
            }
        }

        if(handler == null)
            tts.speak(out_str);
        else{
            handler.sendMessage(handler.obtainMessage());
        }

    }

    public Activity getActivity() {
        return activity;
    }
}
