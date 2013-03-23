package com.vektor.ncpu;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class FrequencyPageFragment extends Fragment {
	
	private View view = null;
	private GovernorListener mListener;
	
	private void updateCurrentGovernor(){
		TextView curr_gov = (TextView) view.findViewById(R.id.textView5);
		String curr_governor = ShInterface.getCurrentGovernor();
		curr_gov.setText(getResources().getString(R.string.curr_governor)+" "+curr_governor);
	}
	
	private void updateCurrentMinFreq(){
		TextView curr_min = (TextView) view.findViewById(R.id.textView6);
		String curr_minfreq = ShInterface.getCurrentMinFreq();
		curr_min.setText(getResources().getString(R.string.curr_minfreq)+" "+curr_minfreq+" "+getResources().getString(R.string.curr_freq2));
	}
	
	private void updateCurrentMaxFreq(){
		TextView curr_max = (TextView) view.findViewById(R.id.textView7);
		String curr_maxfreq = ShInterface.getCurrentMaxFreq();
		curr_max.setText(getResources().getString(R.string.curr_maxfreq)+" "+curr_maxfreq+" "+getResources().getString(R.string.curr_freq2));
	}
	
	private void updateCurrentFreq(){
		TextView cur_freq = (TextView) view.findViewById(R.id.textView8);
		String curr_freq = ShInterface.getCurrentFreq();
		cur_freq.setText(getResources().getString(R.string.curr_freq1)+" "+curr_freq+" "+getResources().getString(R.string.curr_freq2));
	}
	
	private void updateCheckBox(){
		CheckBox chk = (CheckBox) view.findViewById(R.id.checkBox1);
		chk.setChecked(FileSystemInterface.getGovernorBoot(this.getActivity()));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			protected Context ctx = FrequencyPageFragment.this.view.getContext();
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					if(FileSystemInterface.setGovernorBoot("1",ctx) && FileSystemInterface.setGovernor(ShInterface.getCurrentGovernor(),
						ShInterface.getCurrentMinFreq(), ShInterface.getCurrentMaxFreq(), ctx)){
					}
				}
				else {
					FileSystemInterface.setGovernorBoot("0",ctx);
				}
				FrequencyPageFragment.this.updateCheckBox();
			}
			
		});
	}
	
	private void updateSpinnerGovernor(){
		Spinner gov = (Spinner) view.findViewById(R.id.spinner1);
		String[] govs = ShInterface.getAvailableGovernors();

		ArrayAdapter<String> gov_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, govs);
		gov_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		gov.setAdapter(gov_adapter);
		gov.setSelection(UIInterface.spinnerSelector(ShInterface.getCurrentGovernor(), gov));
		
		gov.setOnItemSelectedListener(new OnItemSelectedListener(){
			protected Adapter initAdapter=null;
			protected Context ctx = FrequencyPageFragment.this.view.getContext();
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(initAdapter != arg0.getAdapter()) { 
					initAdapter = arg0.getAdapter();
					return;
				}
				String selected = arg0.getItemAtPosition(arg2).toString();
				ShInterface.setGovernor(selected);
				//If governor changes, disable loading at boot for parameters of old governor
				FileSystemInterface.setParametersBoot("0", ctx);
				
				//If apply settings at boot is enabled, update the governor and frequencies
				if(FileSystemInterface.getGovernorBoot(ctx)){
					FileSystemInterface.setGovernor(ShInterface.getCurrentGovernor(),
							ShInterface.getCurrentMinFreq(), ShInterface.getCurrentMaxFreq(), ctx);
				}
				FrequencyPageFragment.this.updateCurrentGovernor();
				FrequencyPageFragment.this.mListener.updateGovernor(selected);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
	}
	
	private void updateSpinnerMin(){
		Spinner minfr = (Spinner) view.findViewById(R.id.spinner2);
		String[] minfreqs = UIInterface.getSteps();
		
		ArrayAdapter<String> minfreq_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, minfreqs);
		
		minfreq_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		minfr.setAdapter(minfreq_adapter);
		minfr.setSelection(UIInterface.spinnerSelector(ShInterface.getCurrentMinFreq(),minfr));
		minfr.setOnItemSelectedListener(new OnItemSelectedListener(){
			protected Adapter initAdapter =null;
			protected Context ctx = FrequencyPageFragment.this.view.getContext();
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(initAdapter != arg0.getAdapter()) { 
					initAdapter = arg0.getAdapter();
					return;
				}
				String selected = arg0.getItemAtPosition(arg2).toString();
				if(Integer.parseInt(selected)>Integer.parseInt(ShInterface.getCurrentMaxFreq())){
					ShInterface.setMaxFreq(selected);
					ShInterface.setMinFreq(selected);		
					FrequencyPageFragment.this.updateCurrentMinFreq();
					FrequencyPageFragment.this.updateCurrentMaxFreq();
					FrequencyPageFragment.this.updateSpinnerMax();
				}
				else{
				ShInterface.setMinFreq(selected);
				}
				if(FileSystemInterface.getGovernorBoot(ctx)){
					FileSystemInterface.setGovernor(ShInterface.getCurrentGovernor(),ShInterface.getCurrentMinFreq(), ShInterface.getCurrentMaxFreq(), ctx);
					FrequencyPageFragment.this.updateCurrentMinFreq();
					FrequencyPageFragment.this.updateCurrentMaxFreq();
					FrequencyPageFragment.this.updateSpinnerMax();
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
	}
	
	private void updateSpinnerMax(){
		Spinner maxfr = (Spinner) view.findViewById(R.id.spinner3);
		String[] maxfreqs = UIInterface.maxFreqArray(ShInterface.getCurrentMinFreq(), UIInterface.getSteps());
		
		ArrayAdapter<String> maxfreq_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, maxfreqs);
		
		maxfreq_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		maxfr.setAdapter(maxfreq_adapter);
		maxfr.setSelection(UIInterface.spinnerSelector(ShInterface.getCurrentMaxFreq(),maxfr));
		
		maxfr.setOnItemSelectedListener(new OnItemSelectedListener(){
			protected Adapter initAdapter=null;
			protected Context ctx = FrequencyPageFragment.this.view.getContext();
			

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(initAdapter != arg0.getAdapter()) { 
					initAdapter = arg0.getAdapter();
					return;
				}
				String selected = arg0.getItemAtPosition(arg2).toString();
				ShInterface.setMaxFreq(selected);
				if(FileSystemInterface.getGovernorBoot(ctx)){
					FileSystemInterface.setGovernor(ShInterface.getCurrentGovernor(),
							ShInterface.getCurrentMinFreq(), ShInterface.getCurrentMaxFreq(), ctx);
				}
				FrequencyPageFragment.this.updateCurrentMaxFreq();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
	}
	
	
	void initFragment(){
		this.updateCurrentGovernor();
		this.updateCurrentMinFreq();
		this.updateCurrentMaxFreq();
		this.updateCurrentFreq();
		this.updateSpinnerGovernor();
		this.updateSpinnerMin();
		this.updateSpinnerMax();
		this.updateCheckBox();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_page_main,
				container, false);
		this.view = rootView;
		initFragment();
		
		return view;
	}

	public void updateGovernor(String governor) {
		this.updateSpinnerGovernor();
		
	}
	
	Handler h=new Handler();
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    h.post(new Runnable(){

	        @Override
	        public void run() {
	            FrequencyPageFragment.this.updateCurrentFreq();
	            h.postDelayed(this,2000);

	        }

	    });
	}

	
	public interface GovernorListener{
		public void updateGovernor(String governor);
	}
	
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (GovernorListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemSelectedListener");
        }
    }
	
	public void onDetach(Activity activity){
		super.onDetach();
		mListener = null;
	}
	
	
}
