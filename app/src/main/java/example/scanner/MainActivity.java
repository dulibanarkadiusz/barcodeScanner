package example.scanner;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

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
            }
        }
    }


    public void SaveChanges(View view){
        Spinner spinner = (Spinner) findViewById(R.id.spinner_room);
        DropdownElement item = (DropdownElement)spinner.getSelectedItem();
        Log.d("x", item.getName());
    }

    public void showSoftwareListDialog(View view){
        /* Sposób I */
        SoftwareListFragment softwareListFragment = new SoftwareListFragment();
        softwareListFragment.show(getSupportFragmentManager(), "test");


       /* Sposób II
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.fragment_software_list);

        ListView lv = (ListView)dialog.findViewById(R.id.lv);
        dialog.setCancelable(true);
        dialog.setTitle("test");
        dialog.show();*/
    }
}
