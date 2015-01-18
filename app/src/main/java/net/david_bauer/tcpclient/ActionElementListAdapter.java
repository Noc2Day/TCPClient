package net.david_bauer.tcpclient;

import android.app.Notification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by David on 17.12.2014.
 */
public class ActionElementListAdapter extends BaseAdapter {
    static class ViewHolder {
        TextView name;
        TextView task;
        TextView positiontv;
        int position;
    }
    private final ActionElement[] mData;

    public ActionElementListAdapter(ActionElementList actionElementList) {
        mData = actionElementList.getArray();
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public ActionElement getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.task = (TextView) convertView.findViewById(R.id.text2);
            viewHolder.positiontv = (TextView) convertView.findViewById(R.id.text3);
            // store the holder with the view.
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ActionElement item = getItem(position);

        if(item != null) {
            viewHolder.name.setText(item.getName());
            viewHolder.task.setText(item.getTask());
            Integer i = position;
            viewHolder.positiontv.setText(i.toString());
        }

        return convertView;
    }
}


