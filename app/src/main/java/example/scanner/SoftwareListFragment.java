package example.scanner;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class SoftwareListFragment extends DialogFragment {

    Activity mActivity;
    ListView mListView;
    ViewGroup viewGroup;


    public static SoftwareListFragment newInstance() {
        SoftwareListFragment fragment = new SoftwareListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_software_list, container, false);
        viewGroup = rootView;
        getDialog().setTitle("zzz");

        Log.d("x", "Wywolanie okna");

        return rootView;//inflater.inflate(R.layout.fragment_software_list, null);
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            getSoftwareList(viewGroup);
        }catch(JSONException e){}

    }

    public void getSoftwareList(final ViewGroup viewGroup) throws JSONException {
        Log.d("x", "Pobieranie listy z oprogramowaniem... ");
        ServerRespons.get("?type=software", null, new JsonHttpResponseHandler() {
            DropdownElement[] elementsList;

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                elementsList = (DropdownElement[]) Serializer.deserialize(response, DropdownElement.class);

                DropdownElementAdapter listAdapter = new DropdownElementAdapter(getActivity(), elementsList);

                //MainActivity m = (MainActivity)getActivity();
                ListView room_list = (ListView) viewGroup.findViewById(R.id.lv);
                room_list.setAdapter(listAdapter);
                Log.d("x", ""+room_list.getCount());
            }
        });
    }
}
