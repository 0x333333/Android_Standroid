package com.jesusjzp.stan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

public class DBManager {
	private final int BUFFER_SIZE = 400000;
	public static final String DB_NAME = "stan2.db"; 
	public static final String PACKAGE_NAME = "com.jesusjzp.stan";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;
    private SQLiteDatabase database;
    private Context context;
    
    public DBManager(Context context) {
    	this.context = context;
    }
    
    public void openDatabase() {
    	this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }
    
    private SQLiteDatabase openDatabase(String dbfile) {
    	try {
    		if (!(new File(dbfile).exists())) {
    			// import
    			InputStream is = this.context.getResources().openRawResource(R.raw.stan2);
    			FileOutputStream fos = new FileOutputStream(dbfile);
    			byte[] buffer = new byte[BUFFER_SIZE];
    			int count = 0;
    			while ((count = is.read(buffer)) > 0) {
    				fos.write(buffer, 0, count);
    			}
    			fos.close();
    			is.close();
    		}
	    	SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
	    	Log.v("open database", "successfully!");
	    	return db;
	    } catch (FileNotFoundException e) {
	        Log.e("Database", "File not found");
	        e.printStackTrace();
	    } catch (IOException e) {
	        Log.e("Database", "IO exception");
	        e.printStackTrace();
	    }
	    return null;
    }
    
    public void closeDatabase() {
    	this.database.close();
    }
    
    public Cursor fetchAllData() {
    	return this.database.query("station", null, null, null, null, null, null);
    }
    
    public Cursor fetchArretList() throws SQLException {
    	String[] columns = new String[] {"name"};
    	Cursor mCursor = this.database.query("station", columns, null, null, null, null, null);
    	if(mCursor != null) {
			mCursor.moveToFirst();
			Log.v("fetchData", "mCursor is not null"+mCursor.getCount());
		}
    	return mCursor;
    }
    
    public Cursor fetchArretData(String arr) throws SQLException {
    	Cursor mCursor = this.database.query("station", null, 
    			"name" + " like '" + arr + "%'", null, null, null, null);
    	if(mCursor != null) {
			mCursor.moveToFirst();
			Log.v("fetchData", "mCursor is not null"+mCursor.getCount());
		}
		return mCursor;
    }
    
    public Cursor fetchArretName(String s_id) throws SQLException {
    	Cursor mCursor = this.database.rawQuery("select * from station where _id="+s_id, null);
    	return mCursor;
    }
    
    public Cursor fetchArretVlue(String s_id) throws SQLException {
    	Cursor mCursor = this.database.rawQuery("select * from direction where s_id="+s_id, null);
    	return mCursor;
    }
    
    public Cursor fetchLigneData(String ligne) throws SQLException {
    	Cursor mCursor = this.database.query("line", null, 
    			"name" + " like '%" + ligne + "%'", null, null, null, "name");
    	if(mCursor != null) {
			mCursor.moveToFirst();
			Log.v("fetchData", "mCursor is not null"+mCursor.getCount());
		}
		return mCursor;
    }
    
    public Cursor fetchDirecData(String _id) throws SQLException {
    	Log.v("Direction id:", _id);
    	Cursor mCursor = this.database.rawQuery("select * from direction where s_id = "+_id, null);
    	if(mCursor != null) {
			mCursor.moveToFirst();
			Log.v("fetchData", "mCursor is not null "+mCursor.getCount());
		}
		return mCursor;
    }
    
    public Cursor fetchBMData() throws SQLException {
    	Cursor mCursor = this.database.query("bookmark", null, null, null, null, null, null);
    	if(mCursor != null) {
    		mCursor.moveToFirst();
    		Log.v("fetchData", "mCursor is not null "+mCursor.getCount());
    	}
    	return mCursor;
    }
    
    public Cursor fetchLigneData() throws SQLException {
    	Cursor mCursor = this.database.query("line", null, null, null, null, null, "name");
    	if(mCursor != null) {
    		mCursor.moveToFirst();
    		Log.v("fetchData", "mCursor is not null "+mCursor.getCount());
    	}
    	return mCursor;
    }
    
    public Cursor fetchLigArrName(String ligId, String ligName) throws SQLException {
    	Log.v("fetchLigArrname:", ligId);
    	Cursor mCursor = this.database.rawQuery("select * from direction" +
    			" where direction.l_id = " + ligId +
    			" and s_name like \"" + ligName + "%\" group by s_name", null);
    	if(mCursor != null) {
    		mCursor.moveToFirst();
    		Log.v("fetchData", "mCursor is not null "+mCursor.getCount());
    	}
    	return mCursor;
    }
    
    // to be edited
    public int insertBM(int index, String s_id, String name, int two_dir) {
    	String sql_check = "select * from bookmark where s_id = \"" + s_id + "\"";
    	Cursor mCursor = this.database.rawQuery(sql_check, null);
    	if(mCursor.getCount() != 0) {
    		Log.v("check existed", "mCursor is not null "+mCursor.getCount());
    		return 0;
    	} else {
    		String sql = "insert into bookmark values(" + index + ", "
        			+ s_id + ", \""
        			+ name + "\", \""
        			+ getTime() + "\", "
        			+ two_dir + ")";
        	Log.v("sql:", sql);
        	this.database.execSQL(sql);
        	return 1;
    	}
    }
    
    public String getTime() {
    	Time t = new Time();
		t.setToNow();
		int year = t.year;
		int month = t.month;
		int day = t.monthDay;
		return day+"/"+month+"/"+year;
    }

	public void delBM(String id) throws SQLException{
		this.database.execSQL("delete from bookmark where s_id = "+id);
		
	}
    
}








