package com.vektor.ncpu;

import android.os.AsyncTask;

public class ExecuteCommandTask extends AsyncTask<String, Void, Boolean> {

	@Override
	protected Boolean doInBackground(String... cmds) {
		for(String cmd : cmds){
			if(!ShInterface.doCmd(cmd)) return false;		
		}
		return true;
	}

	protected boolean onPostExecute(boolean result) {
	      return result;
	}

}
