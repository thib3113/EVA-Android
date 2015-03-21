package fr.thib3113.eva.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.thib3113.eva.ServiceHandler;

/**
 * Created by thibaut on 19/03/2015.
 */
public class ApiFunction {

    public String api_url;
    private JSONObject jsonObj;

    public ApiFunction(String api_url) {
        this.api_url = api_url;
    }

    protected Map<String,String> call(ApiCall api ,String requete) {
        Map<String,String> retour = new HashMap<String, String>();
        retour.put("status", "false");
        retour.put("error_code", "503");

        switch (requete.toLowerCase()){
            case "ping":
                pingCall(api, retour);
                pingExec(api, retour);
            break;
        }
        return retour;
    }

    private void basicRetour(JSONObject json, Map<String, String> map ){
        try {
            map.put("status", json.getString("status"));
            map.put("message", json.getString("message"));
            map.put("error_code", json.getString("error_code"));
        } catch (JSONException e) {
            Log.e("[JsonException]", "Impossible d'ajouter les json basiques");
            System.out.println("Impossible d'ajouter les json basiques");
            e.printStackTrace();
        }
    }

    private  void pingExec(ApiCall api, Map<String, String> retour){
        api.version = retour.get("version");
    }

    private void pingCall(ApiCall api, Map<String, String> retour){
        String str;

        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(this.api_url+"?type=GET&API=PING", ServiceHandler.GET);
        System.out.println(jsonStr);
        try {
            jsonObj = new JSONObject(jsonStr);
            this.basicRetour(jsonObj, retour);
            retour.put("ping", jsonObj.getString("ping"));
            retour.put("version", jsonObj.getString("version"));
            System.out.println(retour);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
