package fr.thib3113.eva;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by thibaut on 16/03/2015.
 */
public class ApiCall extends AsyncTask<Void, Void, Void> {
    public ArrayList<String> functionList = new ArrayList<String>();
    public Activity activity = null;
    public String url = null;

    public  String api_url = "http://myraspi.thib3113.fr/EVA/api/v";
    public  String api_version = "1";

    public JSONObject jsonObj;
    public String str = null;
    public ProgressDialog pDialog;
    public String version;

    public ApiCall(){
         functionList.add("ping");// put(5, "yes");
        this.setUrl(api_url+api_version);

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
        ServiceHandler sh = new ServiceHandler();

          //ping api
          String jsonStr = sh.makeServiceCall(ApiCall.this.url+"?type=GET&API=PING", ServiceHandler.GET);
        try {
            jsonObj = new JSONObject(jsonStr);
            str = jsonObj.getString("ping");
            System.out.println("réponse au ping : "+str);
            ApiCall.this.setVersion(jsonObj.getString("version"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        pDialog.hide();
            Toast.makeText(activity.getApplicationContext(), "Votre serveur est en version "+ApiCall.this.version, Toast.LENGTH_LONG).show();
        System.out.println(jsonObj.toString());
    }

    public Activity getActivity() {
        return activity;
    }
}
