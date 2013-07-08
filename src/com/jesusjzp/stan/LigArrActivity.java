package com.jesusjzp.stan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LigArrActivity extends Activity {

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.setClass(LigArrActivity.this, LigneActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	ListView list;
	ListAdapter list_adapter;
	DBManager dbManager;
	Cursor cur;
	EditText et_arret;
	
	String ligne_id;
	String ligne_name;
	int arrCode;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ligarrlist);
        
        et_arret = (EditText)findViewById(R.id.et_station);
        list = (ListView)findViewById(R.id.arr_list);
        list.setCacheColorHint(0);
        dbManager = new DBManager(this);
        
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        ligne_id = bundle.getString("ligne_id");
        ligne_name = bundle.getString("ligne_name");
        
        dbManager.openDatabase();
        cur = dbManager.fetchLigArrName(ligne_id, "");
        
        if(cur != null && cur.getCount() >= 0) {
			list_adapter = new SimpleCursorAdapter(LigArrActivity.this,
					R.layout.labellistitem,
					cur,
					new String[] {"s_name"},
					new int[] {R.id.LabelText});
			list.setAdapter(list_adapter);
		}
		dbManager.closeDatabase();
		
		et_arret.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String input = et_arret.getText().toString().trim();
				
				dbManager.openDatabase();
				cur = dbManager.fetchLigArrName(ligne_id, input);
				cur.moveToFirst();
				
				if(cur != null && cur.getCount() >= 0) {
					list_adapter = new SimpleCursorAdapter(LigArrActivity.this,
							R.layout.labellistitem,
							cur,
							new String[] {"s_name"},
							new int[] {R.id.LabelText});
					list.setAdapter(list_adapter);
				}
				dbManager.closeDatabase();
			}           
        });  
        
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        		dbManager.openDatabase();
				cur = dbManager.fetchLigArrName(ligne_id, et_arret.getText().toString().trim());
        		cur.moveToFirst();
        		dbManager.closeDatabase();
        		for(int k = 0; k <  arg2; k ++) {
        			cur.moveToNext();
        		}
        		
        		String s_id = cur.getString(1);
        		String name = cur.getString(5);
        		String value = cur.getString(2);
        		        		
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		bundle.putString("s_id", s_id);
        		bundle.putString("name", name);
        		
        		dbManager.openDatabase();
        		cur = dbManager.fetchArretName(s_id);
        		cur.moveToFirst();
        		dbManager.closeDatabase();
        		
        		if(Integer.parseInt(cur.getString(3)) == 1) {
	        		intent.setClass(LigArrActivity.this, DirectionActivity.class);
        		} else {
        			bundle.putString("value", value);
	        		intent.setClass(LigArrActivity.this, TimeActivity.class);
        		}
	        	
        		intent.putExtras(bundle);
        		startActivity(intent);
        		finish();
        	}
		});
    }
	
}
