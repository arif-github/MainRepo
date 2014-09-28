package com.example.alarmmanager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmActivity extends Activity {

	TextView mAddAlarm;
	ListView AlarmListView;
	List<AlarmObject> list;
	AlarmDatabase database;
	Context mContext = this;
	AlarmListAdapter adapter;

	int MAX = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		database = new AlarmDatabase(mContext);
		database.open();

		list = database.getAllAlarm();
		adapter = new AlarmListAdapter(mContext, list);

		AlarmListView = (ListView) findViewById(R.id.listview);
		AlarmListView.setAdapter(adapter);

		mAddAlarm = (TextView) findViewById(R.id.add_alerm);
		mAddAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onAddAlarmClicked();

			}
		});

	}

	public void onAddAlarmClicked() {

		if (list.size() == MAX) {

			Toast.makeText(this, "Maximum alarm reached ", Toast.LENGTH_SHORT)
					.show();

			return;
		}

		AlarmObject obj = new AlarmObject();
		database.insert(obj);

		list.clear();
		list.addAll(database.getAllAlarm());

		adapter.notifyDataSetChanged();

	}

	public void onExpandClicked() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		database.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
