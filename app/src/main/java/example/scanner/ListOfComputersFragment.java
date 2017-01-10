package example.scanner;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by Hanna on 09.01.2017.
 */

public class ListOfComputersFragment extends android.support.v4.app.Fragment {
    public ListOfComputersFragment(){

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
}
