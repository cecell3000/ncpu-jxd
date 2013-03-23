package com.vektor.ncpu;

import java.util.ArrayList;
import java.util.Arrays;

import android.widget.Spinner;

public class UIInterface {
	
	public static String[] getSteps(){
		int min = Integer.parseInt(ShInterface.cpuInfoMinFreq());
		int max = Integer.parseInt(ShInterface.cpuInfoMaxFreq());
		
		ArrayList<String> freqs = new ArrayList<String>();
		for(int i=8;i*min<max;i++){
			freqs.add(""+(i*min));
		}
		
		freqs.add(""+max);
		return freqs.toArray(new String[freqs.size()]);
		
	}
	
	public static int spinnerSelector(String toSelect, Spinner spinner){
		
		for(int i=0; i<spinner.getAdapter().getCount();i++ ){
			if(spinner.getAdapter().getItem(i).toString().equals(toSelect)) {
				return i;
			}
		}
		
		return 0;
	}
	public static String [] maxFreqArray(String minfr, String[]freqs){
		int lowerbound = 0;
		for(int i=0;i<freqs.length;i++){
			if(freqs[i].equals(minfr)){
				lowerbound = i;
				break;
			}
		}
		
		return (String[])Arrays.copyOfRange(freqs, lowerbound, freqs.length);
	}
}
