package com.example.appmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	DevicePolicyManager mDPM;

	private Context mContext;

	/*
	 * View
	 */
	private ListView listView;

	/*
	 * Objects
	 */
	// private MedicineDatabase databse;
	public AppListAdapter adapter;

	PackageManager mp;

	/*
	 * Data
	 */
	private List<AppObject> list;
	List<ApplicationInfo> allapp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mp = getPackageManager();

		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

		listView = (ListView) findViewById(R.id.listView1);

		/*
		 * Initialize variables
		 */
		mContext = this;

		list = new ArrayList<AppObject>();

		allapp = mp.getInstalledApplications(0);

		for (ApplicationInfo packageInfo : allapp) {

			try {
				PackageInfo i = mp.getPackageInfo(packageInfo.packageName, 0);

				if (isSystemPackage(i)) {
					continue;
				}

			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			AppObject obj = new AppObject();

			obj.intent = mp.getLaunchIntentForPackage(packageInfo.packageName);
			obj.PackageName = packageInfo.packageName;
			obj.lable = mp.getApplicationLabel(packageInfo);
			obj.icon = packageInfo.loadIcon(mp);
			obj.enableState = packageInfo.enabled;

			list.add(obj);

		}
		
		//mp.setApplicationEnabledSetting(packageName, newState, flags);

		adapter = new AppListAdapter(mContext, list);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(onListItemClick);
	}

	private boolean isSystemPackage(PackageInfo pkgInfo) {
		return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
				: false;
	}

	OnItemClickListener onListItemClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

//			Intent i = list.get(position).intent;
//			i.setAction(Intent.ACTION_VIEW);
//			startActivity(i);

		}
	};

	// private boolean isActiveAdmin() {
	// return mDPM.isAdminActive(mDeviceAdminSample);
	// }
}
