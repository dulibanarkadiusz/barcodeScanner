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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

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

    @Override
    public void onStart() {
        super.onStart();
        if (currentComputer==null||currentComputer.getBarcode().length()==0) {
            setEnabledForm(false);
        }
    }

    private void setEnabledForm(boolean isEnabled){
        Log.d("x", "Enabled changing..."+isEnabled);
        MainActivity m = (MainActivity)getActivity();
        LinearLayout lay = (LinearLayout)m.findViewById(R.id.editForm);
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextName)).setEnabled(isEnabled);
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextIP)).setEnabled(isEnabled);
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextMAC)).setEnabled(isEnabled);
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextKey)).setEnabled(isEnabled);
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextSoftware)).setEnabled(isEnabled);
        ((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_os)).setEnabled(isEnabled);
        ((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_room)).setEnabled(isEnabled);
        ((Button)((MainActivity)getActivity()).findViewById(R.id.buttonSave)).setEnabled(isEnabled);

        if (isEnabled) {
            lay.setAlpha(1f);
        }
        else{
            lay.setAlpha(0.5f);
        }
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


    public void saveData(){
        //String s = currentComputer.getBarcode();
        if (currentComputer==null){
            Toast.makeText(getContext(), R.string.alert_error, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("x", "Saving information..");
        updateObjectComputer();
        //Log.d("x", );
        String jsonString = Serializer.serialize(currentComputer);

        ServerRespons.post(getContext(), "?type=computer", jsonString, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("xy", response.toString());
                Toast.makeText(getContext(), R.string.alert_computerSave, Toast.LENGTH_LONG).show();
                setEnabledForm(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getContext(), "Błąd zapisu: "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getContext(), "Błąd pobierania danych.\nSprawdź połączenie internetowe.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getComputerData(MainActivity mA, String barcode) throws JSONException {
        final MainActivity mainActivity = mA;
        Log.d("x", "GetComputerData()");

        ServerRespons.get("?type=computer&bc=" + barcode, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                if (response.toString().contains("[]")){
                    mainActivity.showDialogToConfirmCreatingNewComputer();
                    return;
                }

                setEnabledForm(true);
                computers = (Computer[]) Serializer.deserialize(response, Computer.class);
                currentComputer = computers[0];
                //writeDataToForm(mainActivity);
                fillForm(currentComputer);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(mainActivity, "Błąd pobierania danych.\nSprawdź połączenie internetowe.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(mainActivity, "Błąd pobierania danych.\nSprawdź połączenie internetowe.", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void createNewComputer(MainActivity m, String barcode){
        currentComputer = new Computer();
        currentComputer.setBarcode(barcode);
        isNewComputer = true;

        fillForm(currentComputer);
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


    private void clearForm(){
        fillForm(new Computer());
    }

    private void fillForm(Computer computer){
        Log.d("x", "FillForm()");
        setEnabledForm(true);
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.textViewValueBarcode)).setText(computer.getBarcode());
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextName )).setText(computer.getName());
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextIP)).setText(computer.getIp());
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextMAC)).setText(computer.getMac());
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextKey)).setText(computer.getKey());
        ((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextSoftware)).setText(computer.getSoftware());
        ((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_room)).setSelection(getLocationId(computer.getLocation()));
        ((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_os)).setSelection(getOsId(computer.getOs_name()));
    }

    private void updateObjectComputer(){
        currentComputer.setIp(((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextIP)).getText().toString());
        currentComputer.setName(((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextName)).getText().toString());
        currentComputer.setMac(((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextMAC)).getText().toString());
        currentComputer.setKey(((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextKey)).getText().toString());
        currentComputer.setSoftware(((TextView)((MainActivity)getActivity()).findViewById(R.id.editTextSoftware)).getText().toString());
        currentComputer.setLocation(((DropdownElement)(((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_room)).getSelectedItem())).getName());
        currentComputer.setOs_name(((DropdownElement)(((Spinner)((MainActivity)getActivity()).findViewById(R.id.spinner_os)).getSelectedItem())).getName());
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