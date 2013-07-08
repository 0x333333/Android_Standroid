package com.jesusjzp.stan;
import com.jesusjzp.stan.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class TimeActivity extends Activity {
	
	String s_id;
	String value;
	
	TextView tv_arrname;
	TextView tv_time;
	Button btn_refresh;
	Button btn_msg;
	
	String res = "";
	String sms = "";
	
	GetData getData;
	Handler handler;
	
	DBManager dbManager;
	Cursor cur;
	
	private static final int MESSAGETYPE_01 = 0x0001;
    private ProgressDialog progressDialog = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //
        setContentView(R.layout.time);
        
        tv_arrname = (TextView)findViewById(R.id.arrname);
        tv_time = (TextView)findViewById(R.id.tv_time);
        btn_refresh = (Button)findViewById(R.id.refresh);
        btn_msg = (Button)findViewById(R.id.send_msg);
        
        // get bundle
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        s_id = bundle.getString("s_id");
        value = bundle.getString("value");
        
        dbManager = new DBManager(this);
        dbManager.openDatabase();
        cur = dbManager.fetchArretName(s_id);
        cur.moveToFirst();
        tv_arrname.setText(cur.getString(1));
        dbManager.closeDatabase();
        
        loadContent();
        
        // button click
        btn_refresh.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		loadContent();
        	}
        });
        
        // msg btn click
        btn_msg.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		Uri smsToUri = Uri.parse("smsto:");
            	Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
            	sms = "Arrêt: "+tv_arrname.getText().toString()+"\n"+sms;
            	sms = sms.replace("\n\n", "\n");
            	int first = sms.indexOf("ligne");
            	if(first >= 0) {
            		String tmp = sms.substring(first+6);
                	first = tmp.indexOf("ligne");
                	tmp = tmp.substring(first+6);
                	first = tmp.indexOf("ligne");
                	tmp = tmp.substring(first);
                	sms = sms.substring(0, sms.indexOf(tmp));
            	}
            	
            	mIntent.putExtra("sms_body", sms + "[ Par StanDroid ]");
            	startActivity( mIntent );
        	}
        });
       	
	}
	
	public String handleStr(String res) {
		String temp;
		
		int start = res.indexOf("<!-- horaire temps réel -->");
		Log.v("start:", start+"");
		int end = res.indexOf("<!-- carto -->");
		Log.v("end:", end+"");
		int pas = res.indexOf("Pas d'horaire.");
		if(start != -1 && end != -1 && pas == -1) {
			temp = res.substring(start+117, end-26);
			temp = temp.replaceAll("<li>", "<br> • ");
			temp = temp.replaceAll("<span>", "<b>");
			temp = temp.replaceAll("</span>", "</b>");
			temp = temp.replaceAll("</li>", "<br>");
			temp = temp.replaceAll("</ul>*", "<br><br><b> ");
			temp = temp.replaceAll("</div>", "<b>");
			temp = temp.replaceAll(" direction", ", direction");
			
		} else {
			temp = "<p><b>" + getResources().getString(R.string.tard) + "</b></p>" +
					"<p>Ce problème peut avoir différentes causes, notamment:</p>" +
					"•	La connexion Internet a été perdue.<br>" +
					"•	Il est trop tôt ou trop tard, il n'y a donc pas de prochaine passage.<br>" +
					"•	Le site Web (http://www.reseau-stan.com/) est temporairement indisponible.<br>";
		}
		
		return temp;
	}
	
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	public void loadContent() {
		getData = new GetData();        
        progressDialog = ProgressDialog.show(TimeActivity.this, 
        									 getResources().getString(R.string.connect), 
        									 getResources().getString(R.string.wait));
        new Thread() {
        	public void run() {
        		try {
        			res = getData.getConnect(value);
        		} catch(Exception e) {
        		}
        		Message msg_listData = new Message();
        		msg_listData.what = MESSAGETYPE_01;
        		handler.sendMessage(msg_listData);
        	}
        }.start();
        
        handler = new Handler() {               
			public void handleMessage(Message message) {
				switch (message.what) {
			    case MESSAGETYPE_01:                                        
			    //刷新UI，显示数据，并关闭进度条          
		    	if(res == "") {
    	        	tv_time.setText(getResources().getString(R.string.tard));
    	        }
    	        Spanned wordspan = Html.fromHtml(handleStr(res));
    	        tv_time.setText(wordspan);
    			sms = tv_time.getText().toString();
				progressDialog.dismiss(); //关闭进度条
			        break;
			    }
			}
	    };
	}
	
	/*
	 * menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.timemenu, menu);
    	return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();
    	switch(item_id) {
    	case R.id.send:
    		Uri smsToUri = Uri.parse("smsto:");
        	Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
        	sms = sms.replace("\n\n\n", "\n\n");
        	int first = sms.indexOf("ligne");
        	String tmp = sms.substring(first+6);
        	first = tmp.indexOf("ligne");
        	tmp = tmp.substring(first+6);
        	first = tmp.indexOf("ligne");
        	tmp = tmp.substring(first);
        	sms = sms.substring(0, sms.indexOf(tmp));
        	mIntent.putExtra("sms_body", sms + "[ Par StanDroid ]");
        	startActivity( mIntent );
    		break;
    	}
		return true;
	}
}
