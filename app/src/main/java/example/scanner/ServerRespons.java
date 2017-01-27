package example.scanner;
import com.loopj.android.http.*;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Hanna on 10.01.2017.
 */

public final class ServerRespons {

    private static final String BASE_URL = "http://79.96.84.148/bc/GetData.php";
    private static final String POST_URL = "http://79.96.84.148/bc/PostData.php";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String u = getAbsoluteUrl(url);
        client.get(u, params, responseHandler);
    }



    public static void post(Context ctx, String url, String jsonParam, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        try {
            RequestParams param = new RequestParams();
            param.add("json", jsonParam);

            String u = getAbsolutePOSTurl(url);
            Log.d("x", "POST url: " + u);
            client.post(ctx, u, param, responseHandler);
        }
        catch(Exception e){
            Log.d("x", "Exception" + e);
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static String getAbsolutePOSTurl (String relativeUrl){ return POST_URL + relativeUrl; }

}