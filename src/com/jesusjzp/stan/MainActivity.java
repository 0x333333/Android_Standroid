package com.jesusjzp.stan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	ListView list;
	ListAdapter list_adapter;
	DBManager dbManager;
	Cursor cur;
	
	Button map;
	Button stop;
	
	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main2);
        
        map = (Button)findViewById(R.id.search_ligne);
        stop = (Button)findViewById(R.id.search_arret);
        
        // search by line
        map.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		intent.setClass(MainActivity.this, LigneActivity.class);
        		intent.putExtras(bundle);
        		startActivity(intent);
        		finish();
        	}
        });
        
        // search by stop
        stop.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		intent.setClass(MainActivity.this, StanActivity.class);
        		intent.putExtras(bundle);
        		startActivity(intent);
        		finish();
        	}
        });
        
        // show my bookmarks
        list = (ListView)findViewById(R.id.search_list);
        list.setCacheColorHint(0);
        dbManager = new DBManager(this);
        
        dbManager.openDatabase();
        cur = dbManager.fetchBMData();
        cur.moveToFirst();
        
        if(cur != null && cur.getCount() >= 0) {
			list_adapter = new SimpleCursorAdapter(MainActivity.this,
					R.layout.labellistitem,
					cur,
					new String[] {"name"},
					new int[] {R.id.LabelText});
			list.setAdapter(list_adapter);
		}
		dbManager.closeDatabase();
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        		dbManager.openDatabase();
				cur = dbManager.fetchBMData();
        		cur.moveToFirst();
        		dbManager.closeDatabase();
        		for(int k = 0; k <  arg2; k ++) {
        			cur.moveToNext();
        		}
        		
        		String s_id = cur.getString(1);
        		String name = cur.getString(2);
        		String two_dir = cur.getString(4);
        		
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		bundle.putString("s_id", s_id);
        		bundle.putString("name", name);
        		        		
        		if(Integer.parseInt(two_dir) == 1) {
	        		intent.setClass(MainActivity.this, DirectionActivity.class);
        		} else {
        			dbManager.openDatabase();
        			cur = dbManager.fetchArretVlue(s_id);
        			cur.moveToFirst();
        			dbManager.closeDatabase();
        			bundle.putString("value", cur.getString(2));
	        		intent.setClass(MainActivity.this, TimeActivity.class);
        		}
        		
        		intent.putExtras(bundle);
        		startActivity(intent);
        		finish();
			}
		});
		
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int tmp2 = arg2;
				AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle(getResources().getString(R.string.del_title))
				.setMessage(getResources().getString(R.string.add_msg))
				.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dbManager.openDatabase();
						cur = dbManager.fetchBMData();
		        		cur.moveToFirst();
		        		for(int k = 0; k <  tmp2; k ++) {
		        			cur.moveToNext();
		        		}
		        		String id = cur.getString(1);
		        		dbManager.delBM(id);
		        		cur = dbManager.fetchBMData();
		        		list_adapter = new SimpleCursorAdapter(MainActivity.this,
		    					R.layout.labellistitem,
		    					cur,
		    					new String[] {"name"},
		    					new int[] {R.id.LabelText});
		    			list.setAdapter(list_adapter);
		    			dbManager.closeDatabase();
					}
				})
				.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();									
                    }
                })
				.create();
        		dialog.show();
        		return true;
        		
			}
		});
	}
	
	/*
     * menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.mainmenu, menu);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	int item_id = item.getItemId();
    	switch(item_id) {
    	case R.id.update:
    		GetData getData = new GetData();
    		// get current version
    		int current_version = Integer.parseInt(getString(R.string.version_code).toString());
    		final int online_version = getData.needUpdate();
    		if(online_version > current_version) {
    			Dialog dialog = new AlertDialog.Builder(this)
        		.setIcon(R.drawable.update32)
    			.setTitle("Télécharger?")
    			.setMessage(R.string.telecharger)
    			.setPositiveButton(R.string.oui, 
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int which) {
    							Intent intent = new Intent();
    				    		intent.setAction("android.intent.action.VIEW");
    				    		Uri content_uri_browsers = Uri.parse("http://jesusjzp.webege.com/downloads/stan.apk");
    				    		intent.setData(content_uri_browsers);
    				    		intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
    				    		startActivity(intent);
    				    		dialog.dismiss();
    						}
    					})
    			.setNegativeButton(R.string.non, 
    					new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
					    		dialog.dismiss();
							}
					})
    			.create();
        		// background blur
        		Window window = dialog.getWindow();
    			window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
    			WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        		dialog.show();
    		} else {
    			Toast.makeText(getApplicationContext(), "Vous avez la dernière version.",
    					Toast.LENGTH_SHORT).show();
    		}
    		break;
    	case R.id.score:
    		Intent intent = new Intent(Intent.ACTION_VIEW);
    		intent.setData(Uri.parse("market://details?id=" + getPackageName()));
    		startActivity(intent);
    		break;
    	case R.id.payer:
    		Intent intent3 = new Intent(Intent.ACTION_VIEW);
    		intent3.setData(Uri.parse("market://details?id=" + "com.jesusjzp.standroid"));
    		startActivity(intent3);
    		break;
    	case R.id.guide:
    		preferences = this.getSharedPreferences("test",MODE_WORLD_READABLE);  
            editor = preferences.edit();
            editor.putInt("First", 1);
        	editor.commit();
        	startActivity(new Intent(getApplication(),SplashOne.class));
        	this.finish();
        	break;
    	case R.id.aide:
    		Intent intent2 = new Intent();
    		intent2.setClass(MainActivity.this, AideActivity.class);
    		startActivity(intent2);
    		break;
    	case R.id.quitter:
    		finish();
    		break;
    	}
    	return true;
    }
}
