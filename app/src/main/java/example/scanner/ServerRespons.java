package example.scanner;
import com.loopj.android.http.*;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Hanna on 10.01.2017.
 */

public final class ServerRespons {

   /* private String result;
    private String serverUrl;

    public final String getData(String url) {
        serverUrl = url;
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL(serverUrl);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("type", "computer");
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result = line;
                    }
                } catch (Exception ex) {
                    result=null;
                }
            }
        }).start();
        return result;
    }*/

    private static final String BASE_URL = "http://79.96.84.148/bc/GetData.php";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String u = getAbsoluteUrl(url);
        client.get(u, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}