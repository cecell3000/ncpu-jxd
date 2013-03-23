package com.vektor.ncpu;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class GovernorPageFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public GovernorPageFragment() {
	}
	private View v = null;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_page_governor,
				container, false);
		v=rootView;
		updateGovernor();
		return rootView;
	}
	


	public void updateGovernor() {
		TextView title = (TextView) v.findViewById(R.id.textView1);

		String governor = ShInterface.getCurrentGovernor();
		
		title.setText(getResources().getString(R.string.governor_par)+"("+governor+"):");
		createParamList(governor);
		setCheckBox();
		
	}
	
	public void createParamList(String governor){
		ListView list = (ListView) v.findViewById(R.id.listView1);
		list.setClickable(true);
		List<GovParameter> listPar = ShInterface.createListPar();
		GovParAdapter adapter = new GovParAdapter(this.getActivity(),listPar);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			protected GovParAdapter initAdapter=null;
			
			@Override
			public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2, long arg3) {
				if(initAdapter != arg0.getAdapter()) { 
					initAdapter = (GovParAdapter) arg0.getAdapter();
					return;
				}
				final String selected = ((GovParameter) initAdapter.getItem(arg2)).getKey();
				final String selected_val = ((GovParameter) initAdapter.getItem(arg2)).getVal();

				AlertDialog.Builder alert = new AlertDialog.Builder(arg1.getContext());

				alert.setTitle("Editing "+selected);
				alert.setMessage("Insert new value for "+selected+":");
				
				// Set an EditText view to get user input 
				final EditText input = new EditText(arg1.getContext());
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				input.setText(selected_val);
				alert.setView(input);

				alert.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					  Editable value = input.getEditableText();
					  String newval = value.toString();
					  // Do something with value!
					  if(ShInterface.setGovParam(selected,newval)){
						  GovernorPageFragment.this.updateGovernor();
						//If apply parameters at boot is enabled, update the governor and frequencies
							if(FileSystemInterface.getParametersBoot(v.getContext())){
								FileSystemInterface.setParameters(v.getContext());
							}
					  }
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    //Do nothing.
				  }
				});

				alert.show();
				
			}
			});
	}
	
	public void setCheckBox(){
		CheckBox chk = (CheckBox) v.findViewById(R.id.checkBox1);
		
		chk.setChecked(FileSystemInterface.getParametersBoot(v.getContext()));
		
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				
				Context ctx = GovernorPageFragment.this.v.getContext();
				
				if(isChecked){
					if(FileSystemInterface.setParametersBoot("1",ctx) && FileSystemInterface.setParameters(v.getContext())){
					}
				}
			}
		});
	}
	
}
