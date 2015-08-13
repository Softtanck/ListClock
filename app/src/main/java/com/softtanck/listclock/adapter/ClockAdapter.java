package com.softtanck.listclock.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.softtanck.listclock.R;
import com.softtanck.listclock.view.ClockView;

import java.util.List;

/**
 * @author : Tanck
 * @Description : TODO
 * @date 8/12/2015
 */
public class ClockAdapter extends BaseAdapter {

    private List<Long> mTimes;

    private Context context;

    public ClockAdapter(Context context, List<Long> list) {
        this.context = context;
        this.mTimes = list;
    }

    @Override
    public int getCount() {
        return mTimes.size();
    }

    @Override
    public Long getItem(int position) {
        return mTimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_list, null);
            holder.cv = (ClockView) convertView.findViewById(R.id.cv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.cv.changeTicker();
        }
        holder.cv.setEndTime(mTimes.get(position));
        return convertView;
    }

    private class ViewHolder {
        ClockView cv;
    }
}
