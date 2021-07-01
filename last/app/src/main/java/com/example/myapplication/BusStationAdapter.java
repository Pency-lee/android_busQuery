package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;

import java.util.List;

public class BusStationAdapter extends BaseAdapter {
	private List<BusStationItem> busStationItems;
	private LayoutInflater layoutInflater;

	public BusStationAdapter(Context context, List<BusStationItem> busStationItems) {
		this.busStationItems = busStationItems;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return busStationItems.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.busline_item, null);
			holder = new ViewHolder();
			holder.busStationName = (TextView) convertView.findViewById(R.id.busname);
			holder.busStationId = (TextView) convertView.findViewById(R.id.busid);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.busStationName.setText("站点名 :"
				+ busStationItems.get(position).getBusStationName());
		holder.busStationId.setText("站点ID:"
				+ busStationItems.get(position).getBusStationId());
		return convertView;
	}

	class ViewHolder {
		public TextView busStationName;
		public TextView busStationId;
	}

}
