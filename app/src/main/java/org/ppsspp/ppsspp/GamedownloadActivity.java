package org.ppsspp.ppsspp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.webkit.WebView;
import android.webkit.WebSettings;
import androidx.cardview.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.content.ClipData;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import me.ibrahimsn.particle.*;
import org.jetbrains.kotlin.*;
import arabware.libs.getThumbnail.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class GamedownloadActivity extends AppCompatActivity {
	
	public final int REQ_CD_FILEMAKER = 101;
	
	private String filetime = "";
	private String filename = "";
	
	private ArrayList<HashMap<String, Object>> download_complete = new ArrayList<>();
	
	private LinearLayout linear9;
	private LinearLayout linear10;
	private LinearLayout linear12;
	private WebView webview1;
	private CardView cardview6;
	private LinearLayout linear11;
	private ImageView imageview2;
	private LinearLayout linear13;
	private LinearLayout low;
	private CardView cardview7;
	private ImageView imageview3;
	private LinearLayout linear15;
	private LinearLayout linear16;
	private LinearLayout linear17;
	private ImageView imageview4;
	private TextView textview7;
	private TextView name;
	private TextView textview9;
	private TextView id;
	private TextView textview11;
	private TextView size;
	
	private Intent i = new Intent();
	private RequestNetwork Net;
	private RequestNetwork.RequestListener _Net_request_listener;
	private Intent Filemaker = new Intent(Intent.ACTION_GET_CONTENT);
	private Calendar Time = Calendar.getInstance();
	private SharedPreferences download_data;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.gamedownload);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear9 = findViewById(R.id.linear9);
		linear10 = findViewById(R.id.linear10);
		linear12 = findViewById(R.id.linear12);
		webview1 = findViewById(R.id.webview1);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setSupportZoom(true);
		cardview6 = findViewById(R.id.cardview6);
		linear11 = findViewById(R.id.linear11);
		imageview2 = findViewById(R.id.imageview2);
		linear13 = findViewById(R.id.linear13);
		low = findViewById(R.id.low);
		cardview7 = findViewById(R.id.cardview7);
		imageview3 = findViewById(R.id.imageview3);
		linear15 = findViewById(R.id.linear15);
		linear16 = findViewById(R.id.linear16);
		linear17 = findViewById(R.id.linear17);
		imageview4 = findViewById(R.id.imageview4);
		textview7 = findViewById(R.id.textview7);
		name = findViewById(R.id.name);
		textview9 = findViewById(R.id.textview9);
		id = findViewById(R.id.id);
		textview11 = findViewById(R.id.textview11);
		size = findViewById(R.id.size);
		Net = new RequestNetwork(this);
		Filemaker.setType("*/*");
		Filemaker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		download_data = getSharedPreferences("download_data", Activity.MODE_PRIVATE);
		
		//webviewOnProgressChanged
		webview1.setWebChromeClient(new WebChromeClient() {
				@Override public void onProgressChanged(WebView view, int _newProgress) {
					
				}
		});
		
		//OnDownloadStarted
		webview1.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				DownloadManager.Request webview1a = new DownloadManager.Request(Uri.parse(url));
				String webview1b = CookieManager.getInstance().getCookie(url);
				webview1a.addRequestHeader("cookie", webview1b);
				webview1a.addRequestHeader("User-Agent", userAgent);
				webview1a.setDescription("Downloading file...");
				webview1a.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
				webview1a.allowScanningByMediaScanner(); webview1a.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); webview1a.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
				
				DownloadManager webview1c = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				webview1c.enqueue(webview1a);
				showMessage("Downloading File....");
				BroadcastReceiver onComplete = new BroadcastReceiver() {
					public void onReceive(Context ctxt, Intent intent) {
						showMessage("Download Complete!");
						unregisterReceiver(this);
						
					}};
				registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			}
		});
		
		webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				
				super.onPageFinished(_param1, _param2);
			}
		});
		
		imageview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				webview1.loadUrl(getIntent().getStringExtra("link"));
			}
		});
		
		_Net_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		name.setText(getIntent().getStringExtra("name"));
		id.setText(getIntent().getStringExtra("id"));
		size.setText(getIntent().getStringExtra("Size"));
		Glide.with(getApplicationContext()).load(Uri.parse(getIntent().getStringExtra("img"))).into(imageview3);
		webview1.setDownloadListener(new DownloadListener() {
				public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
						DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
						String cookies = CookieManager.getInstance().getCookie(url);
						request.addRequestHeader("cookie", cookies);
						request.addRequestHeader("User-Agent", userAgent);
						request.setDescription("دانلود کردن فایل...");
						request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
						request.allowScanningByMediaScanner(); request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
						java.io.File aatv = new java.io.File(Environment.getExternalStorageDirectory().getPath() + "/Gta go/Download");if(!aatv.exists()){if (!aatv.mkdirs()){ Log.e("TravellerLog ::","Problem creating Image folder");}} request.setDestinationInExternalPublicDir("/psp/game", URLUtil.guessFileName(url, contentDisposition, mimetype));
						DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
						manager.enqueue(request);
						showMessage("دانلود کردن مود....");
						BroadcastReceiver onComplete = new BroadcastReceiver() {
								public void onReceive(Context ctxt, Intent intent) {
										showMessage("دانلود پایان!");
										unregisterReceiver(this);
								}};
						registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
				}
		});
		
		
		imageview4.setImageResource(R.drawable.downloadcircle);
	}
	
	public void _download_manager(final WebView _view, final String _path) {
		_view.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
				String cookies = CookieManager.getInstance().getCookie(url);
				request.addRequestHeader("cookie", cookies);
				request.addRequestHeader("User-Agent", userAgent);
				request.setDescription("Downloading file...");
				request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
				 filename = URLUtil.guessFileName(url,contentDisposition,mimetype);
				Time = Calendar.getInstance();
				filetime = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(Time.getTime());
				request.allowScanningByMediaScanner(); request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				java.io.File aatv = new java.io.File(Environment.getExternalStorageDirectory().getPath() + _path);
				
				if(!aatv.exists()){
					if (!aatv.mkdirs()){
						Log.e("TravellerLog ::","Problem creating Image folder");}} request.setDestinationInExternalPublicDir(_path, URLUtil.guessFileName(url, contentDisposition, mimetype));
				DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				manager.enqueue(request);
				showMessage("Downloading File....");
				BroadcastReceiver onComplete = new BroadcastReceiver() {
					public void onReceive(Context ctxt, Intent intent) {
						if (filename.trim().equals("")) {
							SketchwareUtil.showMessage(getApplicationContext(), "obs bir hata meydana geldi");
						}
						else {
							{
								HashMap<String, Object> _item = new HashMap<>();
								_item.put("name", filename);
								_item.put("time", filetime);
								_item.put("path", _path);
								download_complete.add(_item);
							}
							download_data.edit().putString("download", new Gson().toJson(download_complete)).commit();
						}
						showMessage("Download Complete!");
						unregisterReceiver(this);
					}};
				registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			}
		});
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}