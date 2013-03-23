package com.vektor.ncpu;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

public class ShInterface {
	private final static String cpufreq_path= "/sys/devices/system/cpu/cpu0/cpufreq";
	private final static String governor_parameters_path = "/sys/devices/system/cpu/cpufreq";
	
	public static String getCpuFreqPath(){
		return cpufreq_path;
	}
	public static String getGovernorParametersPath(){
		return governor_parameters_path;
	}
	
	public static boolean canSu(){
		boolean ret = false;
		Process su;
		try{
			su = Runtime.getRuntime().exec("su");
			
			DataOutputStream os = new DataOutputStream(su.getOutputStream());
			DataInputStream is = new DataInputStream(su.getInputStream());
			
			if(null != os && null != is){
				os.writeBytes("id\n");
				os.flush();
				
				String currUid = is.readLine();
				boolean exitSu = false;
				if(null == currUid)
				{
					ret = false;
					exitSu = false;
					Log.d("ROOT", "Can't get root access or denied by user.");
					
				}
				else if (true == currUid.contains("uid=0"))
				{
					ret = true;
					exitSu = true;
					Log.d("ROOT","Root access Granted");
				}
				else {
					ret=false;
					exitSu=true;
					Log.d("ROOT", "Root access rejected: "+currUid );
				}
				

	            if (exitSu)
	            {
	               os.writeBytes("exit\n");
	               os.flush();
	            }
			}
		}       catch (Exception e)
	      {
	         // Can't get root !
	         // Probably broken pipe exception on trying to write to output stream (os) after su failed, meaning that the device is not rooted

	         ret = false;
	         Log.d("ROOT", "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
	      }

	      return ret;
	}
	
	public final static boolean execute(String cmd){
		try {
			ExecuteCommandTask ect = new ExecuteCommandTask();
			ect.execute(new String[] {cmd});
			return ect.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public final static boolean doCmd(String command){
		boolean retval = false;
	      
	      try
	      {
	         if (null != command && !command.equals(""))
	         {
	            Process suProcess = Runtime.getRuntime().exec("su");

	            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

	            // Execute command that require root access
	            os.writeBytes(command+"\n");
	            os.writeBytes("exit\n");
	            os.flush();
	            //retval = true;
	            
	            try
	            {
	               int suProcessRetval = suProcess.waitFor();
	               if (255 != suProcessRetval)
	               {
	                  // Root access granted
	                  retval = true;
	               }
	               else
	               {
	                  // Root access denied
	                  retval = false;
	               }
	            }
	            catch (Exception ex)
	            {
	               Log.e("ROOT", "Error executing root action", ex);
	            }
	            
	         }
	      }
	      catch (IOException ex)
	      {
	         Log.w("ROOT", "Can't get root access", ex);
	         retval = false;
	      }
	      catch (SecurityException ex)
	      {
	         Log.w("ROOT", "Can't get root access", ex);
	         retval = false;
	      }
	      catch (Exception ex)
	      {
	         Log.w("ROOT", "Error executing internal operation", ex);
	         retval = false;
	      }
	      
	      return retval;
	   }
	
	public static void createFreqFile(){
		//String cmd = "echo "
	}
	
	public static boolean setGovernor(String governor){
		String cmd = "echo "+governor+" > "+cpufreq_path+"/scaling_governor ";
		return execute(cmd);
	}

	public static boolean setMinFreq(String selected) {
		int mhz = Integer.parseInt(selected) * 1000;
		String freq = ""+mhz;
		String cmd = "echo "+freq+" > "+cpufreq_path+"/scaling_min_freq ";
		return execute(cmd);
	}
	
	public static boolean setMaxFreq(String selected) {
		int mhz = Integer.parseInt(selected) * 1000;
		String freq = ""+mhz;
		String cmd = "echo "+freq+" > "+cpufreq_path+"/scaling_max_freq ";
		return execute(cmd);
	}
	
	public static String getCurrentGovernor(){
		Process process;
		try {
			process = Runtime.getRuntime().exec("cat "+cpufreq_path+"/scaling_governor");
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			return in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return "";
	}
	
	public static String getCurrentMinFreq(){
		Process process;
		try {
			process = Runtime.getRuntime().exec("cat "+cpufreq_path+"/scaling_min_freq");
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int mhz = Integer.parseInt(in.readLine())/1000;
			return ""+mhz;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return "";
	}
	public static String getCurrentMaxFreq(){
		Process process;
		try {
			process = Runtime.getRuntime().exec("cat "+cpufreq_path+"/scaling_max_freq");
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int mhz = Integer.parseInt(in.readLine())/1000;
			return ""+mhz;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return "";
	}
	
	public static String[] getAvailableGovernors(){
		Process process;
		try {
			process = Runtime.getRuntime().exec("cat "+cpufreq_path+"/scaling_available_governors");
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String governors = in.readLine();
			String[] available_governors = governors.split("\\s+");
			
			return available_governors;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
	public static String getCurrentFreq(){
		Process process;
		try {
			process = Runtime.getRuntime().exec("cat "+cpufreq_path+"/scaling_cur_freq");
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int mhz = Integer.parseInt(in.readLine())/1000;
			return ""+mhz;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return "";
	}

	public static ArrayList<GovParameter> createListPar(){
		String governor = ShInterface.getCurrentGovernor();
		if (governor.equalsIgnoreCase("smartassv2")) governor = "smartass";
		
		String propPath=governor_parameters_path+"/"+governor;
		
		Process process;
		Process process2;
		ArrayList<GovParameter> listPar = new ArrayList<GovParameter>();
		try{
			String key = null;
			String val = null;
			process2 = Runtime.getRuntime().exec("ls "+propPath);
			
			BufferedReader keyReader = new BufferedReader(new InputStreamReader(process2.getInputStream()));
			
			while ((key = keyReader.readLine())!=null){
				process = Runtime.getRuntime().exec("cat "+propPath+"/"+key);
				BufferedReader valReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				listPar.add(new GovParameter(key,valReader.readLine()));
			}

		}catch(IOException e){}
		return listPar;
	}

	public static boolean setGovParam(String key, String val) {
		String governor = getCurrentGovernor();
		if(governor.equalsIgnoreCase("smartassv2")){
			governor = "smartass";
		}
		String propPath=governor_parameters_path+"/"+governor;
		String cmd = "echo "+val+" > "+propPath+"/"+key; 
		return execute(cmd);
	}
	
	public static String cpuInfoMaxFreq(){
		Process process;
		try {
			process = Runtime.getRuntime().exec("cat "+cpufreq_path+"/cpuinfo_max_freq");
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int mhz = Integer.parseInt(in.readLine())/1000;
			return ""+mhz;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return "";
	}

	public static String cpuInfoMinFreq(){
		Process process;
		try {
			process = Runtime.getRuntime().exec("cat "+cpufreq_path+"/cpuinfo_min_freq");
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int mhz = Integer.parseInt(in.readLine())/1000;
			return ""+mhz;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return "";
	}
	
}
	

