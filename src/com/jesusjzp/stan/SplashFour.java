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

public class SplashFour extends Activity {
	RelativeLayout ll;
	Button btn_next;
	Button btn_last;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.splashfour);
        
        ll = (RelativeLayout)findViewById(R.id.splash);
        btn_last = (Button)findViewById(R.id.button1);
        btn_next = (Button)findViewById(R.id.button2);
        
    	ll.setBackgroundResource(R.drawable.z4);
//    	Handler x = new Handler();
//        x.postDelayed(new StartThread(), 3000);
    	
    	btn_last.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplication(),SplashThree.class));
				SplashFour.this.finish();				
			}
        });
    	
    	btn_next.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplication(),MainActivity.class));
				SplashFour.this.finish();				
			}
        });
	};
	
//	class StartThread implements Runnable{
//
//		public void run() {
//			// TODO Auto-generated method stub
//			startActivity(new Intent(getApplication(),MainActivity.class));
//			SplashFour.this.finish();
//		}
//    	
//    }
}
