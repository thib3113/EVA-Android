package fr.thib3113.eva.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thibaut on 19/03/2015.
 */
public class ApiFunction {

    protected Map<String,String> execute(int requete) {
        Map<String,String> retour = new HashMap<String, String>();
        retour.put("status", "false");
        retour.put("error_code", "503");

        switch (requete){
            case 1:
                retour = ping();
        }
        return retour;
    }

    private Map<String,String> ping(){
        Map<String,String> retour = new HashMap<String, String>();

        return retour;
    }
}
