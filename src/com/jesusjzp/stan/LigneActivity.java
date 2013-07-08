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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LigneActivity extends Activity {
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.setClass(LigneActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	ListView list;
	ListAdapter list_adapter;
	DBManager dbManager;
	Cursor cur;
	EditText et_ligne;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lignelist);

        et_ligne = (EditText)findViewById(R.id.lignename);
        list = (ListView)findViewById(R.id.ligne_list);
        list.setCacheColorHint(0);
        dbManager = new DBManager(this);
        
        dbManager.openDatabase();
		cur = dbManager.fetchLigneData(et_ligne.getText().toString().trim());
		cur.moveToFirst();
		
		if(cur != null && cur.getCount() >= 0) {
			list_adapter = new SimpleCursorAdapter(LigneActivity.this,
					R.layout.labellistitem,
					cur,
					new String[] {"name"},
					new int[] {R.id.LabelText});
			list.setAdapter(list_adapter);
		}
		dbManager.closeDatabase();
		
		et_ligne.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String input = et_ligne.getText().toString().trim();
				
				dbManager.openDatabase();
				cur = dbManager.fetchLigneData(input);
				cur.moveToFirst();
				
				if(cur != null && cur.getCount() >= 0) {
					list_adapter = new SimpleCursorAdapter(LigneActivity.this,
							R.layout.labellistitem,
							cur,
							new String[] {"name"},
							new int[] {R.id.LabelText});
					list.setAdapter(list_adapter);
				}
				dbManager.closeDatabase();
			}           
        });
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        		dbManager.openDatabase();
				cur = dbManager.fetchLigneData(et_ligne.getText().toString().trim());
        		cur.moveToFirst();
        		dbManager.closeDatabase();
        		for(int k = 0; k <  arg2; k ++) {
        			cur.moveToNext();
        		}
        		
        		String ligne_id = cur.getString(2);
        		String ligne_name = cur.getString(1);
        		
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		bundle.putString("ligne_id", ligne_id);
        		bundle.putString("ligne_name", ligne_name);
        		
        		Log.v("ligne_id:", ligne_id);
        		
	        	intent.setClass(LigneActivity.this, LigArrActivity.class);
	        	
        		intent.putExtras(bundle);
        		startActivity(intent);
        		finish();
        	}
		});
	}
}
