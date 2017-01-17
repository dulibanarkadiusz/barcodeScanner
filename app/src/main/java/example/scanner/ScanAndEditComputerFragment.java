package example.scanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Hanna on 09.01.2017.
 */

public class ScanAndEditComputerFragment extends Fragment {
    private DropdownElement[] elementsList;
    private Computer[] computers;
    private Computer currentComputer;
    private Boolean isNewComputer;

    public ScanAndEditComputerFragment() {

    }

    public static ScanAndEditComputerFragment newInstance() {
        ScanAndEditComputerFragment fragment = new ScanAndEditComputerFragment();
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scan_and_edit_computer_fragment, container, false);

        Button buttonScan = (Button) rootView.findViewById(R.id.buttonScan);

        try {
            getRoomsList();
            getOperatingSystemsList();
        } catch (JSONException e) {
        }

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setResultDisplayDuration(0);
                integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();

            }
        });
        return rootView;
    }


    public void getRoomsList() throws JSONException {
        ServerRespons.get("?type=locations", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                elementsList = (DropdownElement[]) Serializer.deserialize(response, DropdownElement.class);

                DropdownElementAdapter listAdapter = new DropdownElementAdapter(getActivity(), elementsList);

                MainActivity m = (MainActivity) getActivity();
                Spinner room_list = (Spinner) m.findViewById(R.id.spinner_room);
                room_list.setAdapter(listAdapter);
            }
        });
    }

    public Boolean getIsNewComputer(){
        return isNewComputer;
    }

    public void getOperatingSystemsList() throws JSONException {
        ServerRespons.get("?type=os", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                elementsList = (DropdownElement[]) Serializer.deserialize(response, DropdownElement.class);

                DropdownElementAdapter listAdapter = new DropdownElementAdapter(getActivity(), elementsList);

                MainActivity m = (MainActivity) getActivity();
                Spinner room_list = (Spinner) m.findViewById(R.id.spinner_os);
                room_list.setAdapter(listAdapter);
            }
        });
    }

    public void getComputerData(MainActivity mA, String barcode) throws JSONException {
        final MainActivity mainActivity = mA;

        ServerRespons.get("?type=computer&bc=" + barcode, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response.toString().contains("[]")){
                    mainActivity.showDialogToConfirmCreatingNewComputer();
                    return;
                }

                computers = (Computer[]) Serializer.deserialize(response, Computer.class);
                currentComputer = computers[0];
                isNewComputer = false;
                writeDataToForm(mainActivity);
            }
        });
    }

    public void createNewComputer(MainActivity m, String barcode){
        currentComputer = new Computer();
        currentComputer.setBarcode(barcode);
        isNewComputer = true;

        writeDataToForm(m);
    }

    private void writeDataToForm(MainActivity m){
        ((TextView)m.findViewById(R.id.textViewValueBarcode)).setText(currentComputer.getBarcode());
        ((EditText)m.findViewById(R.id.editTextName)).setText(currentComputer.getName());

        m.setVisibilityEditorForm(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            if (isVisibleToUser) {
                if(((MainActivity)getActivity()).getSelectedComputer() != null) {
                    currentComputer = ((MainActivity) getActivity()).getSelectedComputer();
                    fillForm(currentComputer);
                    ((MainActivity) getActivity()).clearSelectedComputer();
                }
            }
        }
    }

    private void fillForm(Computer computer){
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.textViewValueBarcode)).setText(computer.getBarcode());
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextName )).setText(computer.getName());
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextIP)).setText(computer.getIp());
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextMAC)).setText(computer.getMac());
        ((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_room)).setSelection(getLocationId(computer.getLocation()));
        ((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_os)).setSelection(getOsId(computer.getOs_name()));
    }

    private int getLocationId(String location){
        int count = ((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_room)).getCount();
        for(int i = 0;i< count;i++){
            if(location.equals(((DropdownElement)((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_room)).getItemAtPosition(i)).getName()))
                return i;
        }
        return -1;
    }

    private int getOsId(String os_name){
        int count = ((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_os)).getCount();
        for(int i = 0;i< count;i++){
            if(os_name.equals(((DropdownElement)((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_os)).getItemAtPosition(i)).getName()))
                return i;
        }
        return -1;
    }

}