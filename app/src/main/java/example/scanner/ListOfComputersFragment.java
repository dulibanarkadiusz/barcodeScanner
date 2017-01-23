package example.scanner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.util.Pools;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
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
    private ISendComputer mCallback;
    private DropdownElement[] elementsList;


    public ListOfComputersFragment() {

    }

    public static ListOfComputersFragment newInstance() {
        ListOfComputersFragment fragment = new ListOfComputersFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (ISendComputer) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot send clicekd computer to edit.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_of_computers_fragment, container, false);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_search_room);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    getComputers();
                } catch (Exception ex) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    getComputers();
                } catch (Exception ex) {
                }
            }

        });
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
            getRoomsList();
            getComputers();
        } catch (Exception ex) {
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage("Czy na pewno chcesz edytować ten komputer?");
        dlgAlert.setTitle("Uwaga!");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.onComputerSend(computers[position]);
                    }
                });
        dlgAlert.setNegativeButton("Anuluj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void getComputers() throws JSONException {
        int roomId = ((DropdownElement)((Spinner)getActivity().findViewById(R.id.spinner_search_room)).getSelectedItem()).getId();
        ServerRespons.get("?type=computer&locationId="+String.valueOf(roomId), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                computers = (Computer[]) Serializer.deserialize(response, Computer.class);
                ComputerAdapter computerAdapter = new ComputerAdapter(getActivity(), computers);
                setListAdapter(computerAdapter);
            }
        });
    }

    public void getRoomsList() throws JSONException {
        ServerRespons.get("?type=locations", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                elementsList = (DropdownElement[]) Serializer.deserialize(response, DropdownElement.class);

                DropdownElementAdapter listAdapter = new DropdownElementAdapter(getActivity(), elementsList);

                MainActivity m = (MainActivity) getActivity();
                Spinner room_list = (Spinner) m.findViewById(R.id.spinner_search_room);
                room_list.setAdapter(listAdapter);
            }
        });
    }
}
