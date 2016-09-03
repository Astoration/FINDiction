package moe.koibito.findiction;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Astoration on 2016. 9. 3..
 */
public class NaviListAdapter extends BaseAdapter {
    private ArrayList<NaviMenuItem> MenuItemlist = new ArrayList<>();

    public NaviListAdapter(NaviMenuItem[] items){
        MenuItemlist.addAll(Arrays.asList(items));
    }

    public void addMenuItem(Drawable drawable,String title){
        NaviMenuItem item = new NaviMenuItem(drawable,title);
        MenuItemlist.add(item);
    }

    @Override
    public int getCount() {
        return MenuItemlist.size();
    }

    @Override
    public Object getItem(int i) {
        return MenuItemlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        Context context = viewGroup.getContext();
        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.navidrawer_list_item,viewGroup,false);
        }
        ImageView icon = (ImageView) view.findViewById(R.id.list_icon);
        TextView title= (TextView) view.findViewById(R.id.list_title);
        NaviMenuItem item = MenuItemlist.get(pos);
        icon.setImageDrawable(item.getIconDrawable());
        title.setText(item.getTitle());
        return view;
    }


    @Override
    public boolean isEmpty() {
        return MenuItemlist.isEmpty();
    }
}
