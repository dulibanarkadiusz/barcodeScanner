package example.scanner;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Hanna on 09.01.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return ScanAndEditComputerFragment.newInstance();
        else
            return ListOfComputersFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

}

