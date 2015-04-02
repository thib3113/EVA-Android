package fr.thib3113.eva.api;

import android.provider.Settings;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

import fr.thib3113.eva.MainActivity;

/**
 * Created by thibaut on 13/03/2015.
 */
public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    public int status_code;
    public HttpResponse httpResponse = null;
    public Exception Exception = null;

    private String android_id = Settings.Secure.getString(MainActivity.getAppContext().getContentResolver(),
            Settings.Secure.ANDROID_ID);

    public ServiceHandler() {

    }

    /**
     * Making service call
     * @param  url - url to make request
     * @param method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     * @param url - url to make request
     * @param method - http request method
     * @param params - http request params
     * */
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpEntity httpEntity;

//            System.out.println(url);
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("USER_AGENT", "android|"+android_id);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                BasicCookieStore cookieStore = new BasicCookieStore();
                BasicClientCookie cookie = new BasicClientCookie("EVA-COOKIE", "");
                cookieStore.addCookie(cookie);
                BasicHttpContext httpContext = new BasicHttpContext();
//                System.out.println("set cookie");
                httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

                httpGet.setHeader("USER_AGENT", "android|"+android_id);
                httpResponse = httpClient.execute(httpGet, httpContext);

            }
            status_code = httpResponse.getStatusLine().getStatusCode();
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            System.out.println(httpResponse.getAllHeaders().toString());

        } catch (UnsupportedEncodingException e) {
//            System.out.println("UnsupportedEncodingException");
            this.Exception = e;
//            e.printStackTrace();
        } catch (ConnectTimeoutException e){
//            System.out.println("ConnectTimeoutException");
            this.Exception = e;
//            e.printStackTrace();
        } catch (UnknownHostException e){
//            System.out.println("UnknownHostException");
            this.Exception = e;
//            e.printStackTrace();
        }
        catch (ClientProtocolException e) {
//            System.out.println("ClientProtocolException");
            this.Exception = e;
//            e.printStackTrace();
        } catch (IOException e) {
//            System.out.println("IOException ");
            this.Exception = e;
//            e.printStackTrace();
        }


        return response;

    }
}
