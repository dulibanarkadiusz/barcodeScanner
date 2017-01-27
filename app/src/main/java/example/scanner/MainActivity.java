package example.scanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements ISendComputer {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Computer selectedComputer;
    //ZADANIE 4
    // private Boolean save;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if (result.getContents() != null) {
                //((TextView) findViewById(R.id.textViewScan)).setText(result.getContents());
                ((EditText)findViewById(R.id.editTextBarCode)).setText(result.getContents());
                findViewById(R.id.buttonOK).performClick();
            }
        }
    }


    public void getComputerDataButtonOnclick(View view){
        EditText et = (EditText)findViewById(R.id.editTextBarCode);

        if (et.getText().length()<1){
            Toast.makeText(this, R.string.alert_emptyBarcode, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            ((ScanAndEditComputerFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem())).getComputerData(this, et.getText().toString());
        } catch (JSONException e) {}
    }

    public void showDialogToConfirmCreatingNewComputer(){
        final MainActivity m = this;
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_unknownBarcodeTitle)
                .setMessage(R.string.alert_unknownBarcodeText)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(m, "Dodano", Toast.LENGTH_SHORT).show();
                        String barcode = ((EditText)findViewById(R.id.editTextBarCode)).getText().toString();
                        ((ScanAndEditComputerFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem())).createNewComputer(m, barcode);
                    }
                })
                .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void setVisibilityEditorForm(boolean isVisible){
        ScrollView scroller = (ScrollView)findViewById(R.id.scroller);
        scroller.fullScroll(ScrollView.FOCUS_UP);

        LinearLayout lay = (LinearLayout)findViewById(R.id.editForm);
        if (isVisible) {
            lay.setVisibility(View.VISIBLE);
        }
        else{
            lay.setVisibility(View.INVISIBLE);
        }

    }

    public void SaveChanges(View view){
        Spinner spinner = (Spinner) findViewById(R.id.spinner_room);
        DropdownElement item = (DropdownElement)spinner.getSelectedItem();

        //setVisibilityEditorForm(false);
        ((ScanAndEditComputerFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem())).saveData();

        Log.d("x", item.getName());
    }

    public void showSoftwareListDialog(View view){
        /* Sposób I */
        SoftwareListFragment softwareListFragment = SoftwareListFragment.newInstance();
        softwareListFragment.show(getSupportFragmentManager(), "test");

    }

    @Override
    public void onComputerSend(Computer computer) {
        selectedComputer = computer;
        //ZADANIE 1
        //mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    //ZADANIE 4
    @Override
    public void onComputerSend(Computer computer, Boolean save) {
       /*
        selectedComputer = computer;
        this.save = save;
        //ZADANIE 1
        //mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        */
    }

    public Computer getSelectedComputer() {
        return selectedComputer;
    }

    //ZADANIE 4
    // public Boolean getSave(){return save;} //4

   public void clearSelectedComputer(){
       selectedComputer = null;
   }
}
