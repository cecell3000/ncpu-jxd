package com.vektor.ncpu;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GovParAdapter extends BaseAdapter {
	
	private Context context;
	private List<GovParameter> listPar;
	
	public GovParAdapter(Context context, List<GovParameter> listPar){
		this.context=context;
		this.listPar=listPar;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPar.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listPar.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		GovParameter entry = listPar.get(arg0);
		if(null == arg1){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.list_row, null);
		}
		TextView key = (TextView) arg1.findViewById(R.id.textView1);
		TextView val = (TextView) arg1.findViewById(R.id.textView2);
		
		key.setText(entry.getKey());
		val.setText(entry.getVal());
		
		return arg1;
	}

}
