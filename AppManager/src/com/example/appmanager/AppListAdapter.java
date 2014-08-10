package com.example.appmanager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class AppListAdapter extends ArrayAdapter<AppObject> {

	private Context mContext;
	private List<AppObject> values;
	PackageManager mp;

	// private String TAG = "MedicineListAdapter";

	public AppListAdapter(Context context, List<AppObject> objects) {

		super(context, R.layout.list_row_app, objects);

		values = objects;
		mContext = context;
		mp = context.getPackageManager();
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(R.layout.list_row_app, parent, false);
		}

		/*
		 * collect data
		 */
		final AppObject object = values.get(position);
		// Log.i("Medicine", "ID=" + object.medicineId);

		/*
		 * set medicine name
		 */
		TextView medicine_name_tv = (TextView) row
				.findViewById(R.id.medicin_name);

		ImageView v = (ImageView) row.findViewById(R.id.imageView1);

		v.setImageDrawable(object.icon);

		String name = (String) object.lable;
		medicine_name_tv.setText(name);
		// medicine_name_tv.setCompoundDrawables(object.icon, null, null, null);

		/*
		 * set daysOfContinuty
		 */

		Switch s = (Switch) row.findViewById(R.id.switch1);

		s.setChecked(object.enableState);

		s.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mp.setApplicationEnabledSetting(object.PackageName,
							PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
							PackageManager.DONT_KILL_APP);
				}
				else{
					mp.setApplicationEnabledSetting(object.PackageName,
							PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER,
							PackageManager.DONT_KILL_APP);
				}

			}
		});

		return row;
	}
}
