package example.scanner;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Hanna on 09.01.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ScanAndEditComputerFragment scanAndEditComputerFragment;
    private ListOfComputersFragment listOfComputersFragment=null;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            if(scanAndEditComputerFragment == null )
                scanAndEditComputerFragment = ScanAndEditComputerFragment.newInstance();
            return scanAndEditComputerFragment;
        }
        else {
            listOfComputersFragment = ListOfComputersFragment.newInstance();
            return listOfComputersFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public Fragment getPage( int possition){
        if( possition == 0 )
            return scanAndEditComputerFragment;
        else
            return listOfComputersFragment;
    }

}

