package com.n3p7un3.computerurllauncher;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.computerurllauncher.R;

public class Settings extends PreferenceFragment {

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//addPreferencesFromReso
		this.getPreferenceManager().setSharedPreferencesName("com.n3p7un3.computerurllauncher.prefs");
		addPreferencesFromResource(R.xml.preferences);
		
	}
}
