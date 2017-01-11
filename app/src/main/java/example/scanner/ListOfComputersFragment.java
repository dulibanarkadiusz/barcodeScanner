package example.scanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Hanna on 09.01.2017.
 */

public class ListOfComputersFragment extends ListFragment {
    private Computer[] computers;
    private final String API_URL = "http://79.96.84.148/bc/GetData.php?type=computer";

    public ListOfComputersFragment() {

    }

    public static ListOfComputersFragment newInstance() {
        ListOfComputersFragment fragment = new ListOfComputersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_of_computers_fragment, container, false);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                try {
                    getComputers();

                } catch (Exception ex) {
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getComputers();

        } catch (Exception ex) {
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage("This is an alert with no consequence");
        dlgAlert.setTitle("App Title");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT);
                    }
                });
        dlgAlert.setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Cancle", Toast.LENGTH_SHORT);
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void getComputers() throws JSONException {
        ServerRespons.get("?type=computer", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                computers = (Computer[]) Serializer.deserialize(response, Computer.class);
                ComputerAdapter computerAdapter = new ComputerAdapter(getActivity(), computers);
                setListAdapter(computerAdapter);
            }
        });
    }
}
