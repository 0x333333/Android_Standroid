package com.jesusjzp.stan;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SplashOne extends Activity {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	RelativeLayout ll;
	
	Button btn_next;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.splashone);
        
        ll = (RelativeLayout)findViewById(R.id.splash);
        btn_next = (Button)findViewById(R.id.button2);
        
        sharedPreferences = this.getSharedPreferences("test",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();
        
        int isFirst = sharedPreferences.getInt("First", 1);
        
        if(isFirst == 1) {
        	Log.v("For the first time", "true");
        	ll.setBackgroundResource(R.drawable.z1);
        	editor.putInt("First", 0);
        	editor.commit();
//        	Handler x = new Handler();
//            x.postDelayed(new StartThread(), 3000);
        } else {
        	startActivity(new Intent(getApplication(),MainActivity.class));
			this.finish();
        }
        
        btn_next.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplication(),SplashTwo.class));
				SplashOne.this.finish();				
			}
        });
        
	};
	
//	class StartThread implements Runnable{
//
//		public void run() {
//			// TODO Auto-generated method stub
//			startActivity(new Intent(getApplication(),SplashTwo.class));
//			SplashOne.this.finish();
//		}
//    	
//    }
}
