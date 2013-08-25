package com.jesusjzp.stan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.text.Html;

public class MobActivity extends Activity {
	WebView wv;
	ProgressDialog pd;
	Handler handler;

	String address = "http://mob.reseau-stan.com/";
	String msg = "<b>Chère Madame, cher Monsieur,</b><br><br>"+
	"Les nouvelles lignes du réseau Stan sont opérationnelles depuis le 24 août 2013. Cependant, cela a entraîné un changement dans la façon d'accéder à leur base de données contenant les horaires. Cette modification explique pourquoi Standroid ne fonctionne plus pour l'instant.<br><br>" 
+"Veuillez m'excuser pour ce désagrément et je vous promets que cela sera corrigé dans les plus brefs délais.<br><br>"
+"En vous remerciant pour votre compréhension,<br><br>"
+"<b>Zhipeng JIANG</b>";

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mob);

		preferences = this.getSharedPreferences("user_first_open", 0);
		editor = preferences.edit();
		if (preferences.getInt("first", 0) == 0) {
			AlertDialog.Builder builder = new Builder(MobActivity.this);
			builder.setMessage(Html.fromHtml(msg));
			builder.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// set preference
					editor.putInt("first", 1);
					editor.commit();
					dialog.dismiss();
					init();
					wv.loadUrl(address);
				}
			});
			builder.setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MobActivity.this.finish();
				}
			});
			builder.create().show();
		} else {
			init();
			wv.loadUrl(address);
		}
		
	}

	public void init() {
		pd = new ProgressDialog(MobActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("Loading...");

		wv = (WebView) findViewById(R.id.webview);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setDefaultTextEncodingName("UTF-8");
		wv.getSettings().setSupportZoom(true);
		wv.getSettings().supportMultipleWindows();
		wv.setScrollBarStyle(0);
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				view.loadUrl(url);
				return true;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pd.show();
			}

			public void onPageFinished(WebView view, String url) {
				pd.dismiss();
			}

		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
			wv.goBack();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void ConfirmExit() {
		AlertDialog.Builder ad = new AlertDialog.Builder(MobActivity.this);
		ad.setTitle("Quitter");
		ad.setMessage("");
		ad.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub
				MobActivity.this.finish();
			}
		});
		ad.setNegativeButton("Non", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
			}
		});
		ad.show();
	}

	/*
	 * menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mobmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();
		switch (item_id) {
		case R.id.aide:
			Toast.makeText(
					getApplicationContext(),
					"Le site Stan ayant changé, la nouvelle version de StanDroid est à venir!",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.quitter:
			finish();
			break;
		}
		return true;
	}
}
