package com.example.alarmmanager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlarmListAdapter extends ArrayAdapter<AlarmObject> {
	Context mContext;
	List<AlarmObject> values;
	AlarmDatabase database;

	public AlarmListAdapter(Context context, List<AlarmObject> objects) {

		super(context, R.layout.list_row_alarm, objects);

		values = objects;
		mContext = context;
		// TODO Auto-generated constructor stub
		database = new AlarmDatabase(mContext);
		database.open();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		final AlarmObject object = values.get(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(R.layout.list_row_alarm, parent, false);
		}

		if (position % 2 == 0) {
			row.setBackgroundColor(0x9984a3bc);
		} else {
			row.setBackgroundColor(0x995882A4);
		}

		RelativeLayout expand = (RelativeLayout) row.findViewById(R.id.bottom);
		final RelativeLayout middle = (RelativeLayout) row
				.findViewById(R.id.middle);

		final LinearLayout dayslayout = (LinearLayout) row
				.findViewById(R.id.dayslaout);

		final TextView mArrow = (TextView) row.findViewById(R.id.arrow);

		Button buttonSave = (Button) row.findViewById(R.id.save);
		Button buttonDelete = (Button) row.findViewById(R.id.delete);
		Button buttonCancel = (Button) row.findViewById(R.id.cancel);

		buttonDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				database.delete(object.alarmId);
				values.clear();
				values.addAll(database.getAllAlarm());
				notifyDataSetChanged();

			}
		});

		CheckBox mRepeat = (CheckBox) row.findViewById(R.id.repaet);
		mRepeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (!isChecked) {
					dayslayout.setVisibility(View.GONE);
				} else {
					dayslayout.setVisibility(View.VISIBLE);
				}

			}
		});

		expand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int vis = middle.getVisibility();

				if (vis == View.VISIBLE) {

					middle.setVisibility(View.GONE);
					mArrow.setBackgroundResource(R.drawable.down_arrow);

				} else if (vis == View.GONE) {
					middle.setVisibility(View.VISIBLE);
					mArrow.setBackgroundResource(R.drawable.arrow_up);
				}

			}
		});

		// TextView dateTv = (TextView) row.findViewById(R.id.add_alerm);
		//
		// dateTv.setText(object.toString());

		return row;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		database.close();
	}
}
