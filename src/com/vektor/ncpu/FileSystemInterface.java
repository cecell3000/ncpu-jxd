package com.vektor.ncpu;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.os.Environment;


public class FileSystemInterface {
	public final static String external_storage = Environment.getExternalStorageDirectory().getPath();
	//public final String external_storage;
	private final static String cpufreq_path= "/sys/devices/system/cpu/cpu0/cpufreq";
	private final static String governor_parameters_path = "/sys/devices/system/cpu/cpufreq";
	
	public static String getCpuFreqPath(){
		return cpufreq_path;
	}
	public static String getGovernorParametersPath(){
		return governor_parameters_path;
	}
	
	private static String convertToString(InputStream fin) {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while((line = reader.readLine())!=null){
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static boolean setGovernorBoot(String boot, Context ctx){
		String fname = "governor_boot";
		try {
			FileOutputStream fo = ctx.openFileOutput(fname, Context.MODE_PRIVATE);
			fo.write(boot.getBytes());
			fo.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean getGovernorBoot(Context ctx){
		String fname = "governor_boot";
		FileInputStream fin;
		try {
			fin = ctx.openFileInput(fname);
			return convertToString(fin).equals("1\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean setParametersBoot(String boot, Context ctx){
		String fname = "parameters_boot";
		try {
			FileOutputStream fo = ctx.openFileOutput(fname, Context.MODE_PRIVATE);
			fo.write(boot.getBytes());
			fo.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}	
	
	public static boolean getParametersBoot(Context ctx){
		String fname = "parameters_boot";
		FileInputStream fin;
		try {
			fin = ctx.openFileInput(fname);
			return convertToString(fin).equals("1\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean setGovernor(String governor, String min, String max, Context ctx){
		String fname = "governor";
		try {
			FileOutputStream fo = ctx.openFileOutput(fname, Context.MODE_PRIVATE);
			String gov = governor+" "+min+" "+max;
			fo.write(gov.getBytes());
			fo.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean applyGovernor(Context ctx){
		String fname = "governor";
		String cmd = "";
		try{
			FileInputStream fin = ctx.openFileInput(fname);
			String set = convertToString(fin);
			fin.close();
			set.trim();
			set.replace("\n", "");
			set.replace("\r", "");
			String[] settings = set.split("\\s+");
			return ShInterface.setGovernor(settings[0]) &&
				   ShInterface.setMinFreq(settings[1]) &&
				   ShInterface.setMaxFreq(settings[2]);
			
			
		}catch(FileNotFoundException e){} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static String getStoredGovernor(Context ctx){
		String fname = "governor";
		try{
			FileInputStream fin = ctx.openFileInput(fname);
			String set = convertToString(fin);
			fin.close();
			set.trim();
			set.replace("\n", "");
			set.replace("\r", "");
			String[] settings = set.split("\\s+");
			return settings[0];
		}catch(FileNotFoundException e){} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";	
	}
	public static boolean setParameters(Context ctx){
		String fname = "parameters";
		try {
			String params = "";
			List<GovParameter> list = ShInterface.createListPar();
			params=ShInterface.getCurrentGovernor()+"\n";
			for(int i=0;i<list.size();i++){
				params=params+list.get(i).getKey()+" "+list.get(i).getVal()+"\n";
			}
			FileOutputStream fo = ctx.openFileOutput(fname, Context.MODE_PRIVATE);
			fo.write((params).getBytes());
			fo.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block	
		}
		return false;
	}

	public static boolean applyParameters(Context ctx){
		String governor = getStoredGovernor(ctx); 
		String cmd = "";
		if(FileSystemInterface.getParametersBoot(ctx)){
			String propPath=governor_parameters_path+"/"+governor;
			String fname = "parameters";
			try{
				FileInputStream fin = ctx.openFileInput(fname);
				String set = convertToString(fin);
				fin.close();
				String[] param_couples = set.split("\\r?\\n+");
				
				
				if(!param_couples[0].equals(governor)) return false;
				
				for(int i=1;i<param_couples.length;i++){	
					String[] keyval = param_couples[i].split("\\s+");
					cmd = "echo "+keyval[1]+" > "+propPath+"/"+keyval[0]+" ; ";
					if(!ShInterface.execute(cmd)) return false;
				}
				return true;
			}catch(FileNotFoundException e){} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
		
	}
}
