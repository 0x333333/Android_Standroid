package com.jesusjzp.stan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class DirectionActivity extends Activity {
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.setClass(DirectionActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	String s_id;
	String name;
	String two_dir;
	
	TextView tv_arrname;
	
	ListView list;
	ListAdapter list_adapter;
	DBManager dbManager;
	Cursor cur;
	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;
	
	Button add;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.direction);
        
        tv_arrname = (TextView)findViewById(R.id.arrname);
        list = (ListView)findViewById(R.id.direc_list);
        add = (Button)findViewById(R.id.search);
        list.setCacheColorHint(0);
        dbManager = new DBManager(this);
        
        // get bundle
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        s_id = bundle.getString("s_id");
        name = bundle.getString("name");
        
        // listen to add button click
        add.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		AlertDialog dialog = new AlertDialog.Builder(DirectionActivity.this)
				.setTitle(getResources().getString(R.string.add_title))
				.setMessage(getResources().getString(R.string.add_msg))
				.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dbManager.openDatabase();
		        		preferences = getSharedPreferences("index_bookmarks", MODE_PRIVATE);
		        		editor = preferences.edit();
		        		int index = preferences.getInt("index", 0);
		        		if(dbManager.insertBM(index, s_id, name, 1) == 0) {
		        			Toast toast = Toast.makeText(getApplicationContext(),
		        				     "Vous avez ajouté " + name + " dans vos listes.", Toast.LENGTH_SHORT);
		        			toast.show();
		        		}
		        		dbManager.closeDatabase();
		        		index++;
		        		editor.putInt("index", index);
		        		editor.commit();
						dialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(DirectionActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				})
				.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();									
                    }
                })
				.create();
        		dialog.show();
        		
        		
        	}
        });
        
        
        tv_arrname.setText(name);
        
        dbManager.openDatabase();
        cur = dbManager.fetchDirecData(s_id);
        cur.moveToFirst();
        
        if(cur != null && cur.getCount() >= 0) {
        	list_adapter = new HtmlCursorAdapter(DirectionActivity.this,
					R.layout.labellistitem,
					cur,
					new String[] {"dir_text"},
					new int[] {R.id.LabelText});
			list.setAdapter(list_adapter);
        }
        dbManager.closeDatabase();
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				dbManager.openDatabase();
                cur = dbManager.fetchDirecData(s_id);
                cur.moveToFirst();
                dbManager.closeDatabase();
                for(int k = 0; k <  arg2; k ++) {
        			cur.moveToNext();
        		}
                
                Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		bundle.putString("s_id", s_id);
        		String value = cur.getString(2);
        		Log.v("value:", value);
        		value = value.replace("$", "%24");
        		Log.v("value:", value);
        		bundle.putString("value", value);
        		
        		intent.setClass(DirectionActivity.this, TimeActivity.class);
        		intent.putExtras(bundle);
        		startActivity(intent);

			}
		});
    }
    
    private class HtmlCursorAdapter extends SimpleCursorAdapter {
	    public HtmlCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
	        super(context, layout, c, from, to);
	    }
	    @Override
	    public void setViewText (TextView view, String text) {
	    	Log.v("text before:", text);
	    	text = text.replaceAll("Ligne", "Ligne<b>");
	    	text = text.replaceAll("Direction", "</b>Direction<b>");
	    	text = text+"</b>";
	    	Log.v("text middle:", text);
	        view.setText(Html.fromHtml(text),BufferType.SPANNABLE);
	    }
    }
    
    
}
