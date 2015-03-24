package fr.thib3113.eva;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import fr.thib3113.eva.api.ApiCall;

/**
 * Created by thibaut on 24/03/2015.
 */
public class User {
    private static boolean isConnect = false;

    public boolean isConnect() {
        return isConnect;
    }

    public void tryToConnect(String username,String password){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user", username));
        params.add(new BasicNameValuePair("pass", password));
        params.add(new BasicNameValuePair("remember_me", "true"));
        ApiCall a = new ApiCall((MainActivity)MainActivity.getActivity());
        a.addToFunctionQueue("connect", params);
        a.execute();
    }


}
