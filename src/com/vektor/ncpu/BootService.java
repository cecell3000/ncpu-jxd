package com.vektor.ncpu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BootService extends Service {


public BootService(){}

@Override
public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
}


@Override   
public void onCreate() {
	
	if(FileSystemInterface.getGovernorBoot(getApplicationContext())){
   		FileSystemInterface.applyGovernor(getApplicationContext());
 
   		if(FileSystemInterface.getParametersBoot(getApplicationContext())) {
   			FileSystemInterface.applyParameters(getApplicationContext());
   		}
	}
	this.stopSelf();
}   

@Override   
public void onDestroy() {   

}

}
