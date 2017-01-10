package example.scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hanna on 10.01.2017.
 */

public class ComputerAdapter extends BaseAdapter {
    private Computer[] computers;
    private LayoutInflater songInf;


    public ComputerAdapter(Context c, Computer[] theComputers) {
        computers = theComputers;
        songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return computers.length;
    }

    @Override
    public Object getItem(int position) {
        return computers[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout computerLay;

        computerLay = (LinearLayout) songInf.inflate(R.layout.computer_list_item, parent, false);

        //get title and artist views
        TextView computerName = (TextView) computerLay.findViewById(R.id.computerName);
        TextView computerBarcode = (TextView) computerLay.findViewById(R.id.computerBarcode);
        TextView computerLocation = (TextView) computerLay.findViewById(R.id.computerLocation);

        //get song using position
        Computer currentComputer = computers[position];

        //get title and artist strings
        computerName.setText(currentComputer.getName());
        computerBarcode.setText(currentComputer.getBarcode());
        computerLocation.setText(currentComputer.getLocation());

        //set position as tag
        computerLay.setTag(position);

        return computerLay;
    }
}
