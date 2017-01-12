package example.scanner;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Arek on 12.01.2017.
 */

public class DropdownElementAdapter extends BaseAdapter {
    private DropdownElement[] rooms;
    private LayoutInflater songInf;


    public DropdownElementAdapter(Context c, DropdownElement[] theComputers) {
        rooms = theComputers;
        songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return rooms.length;
    }

    @Override
    public Object getItem(int position) {
        return rooms[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout computerLay;

        computerLay = (LinearLayout) songInf.inflate(R.layout.dropdownlist_element, parent, false);

        //get title and artist views
        TextView itemName = (TextView) computerLay.findViewById(R.id.textViewDropdownListText);

        //get song using position
        DropdownElement currentComputer = rooms[position];

        //get title and artist strings
        itemName.setText(currentComputer.getName());

        //set position as tag
        computerLay.setTag(position);

        return computerLay;
    }
}
