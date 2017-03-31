package com.weituotian.video.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.weituotian.video.R;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.utils.ColorUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cookie;

/**
 * 显示页面的activity
 * Created by ange on 2017/3/18.
 */

public class BrowserActivity extends AppCompatActivity {

    private final String DEFAULT_URL = "";
    private final String TAG = "BrowserActivity";

    public static final int TYPE_SYSTEM = 2;
    public static final int TYPE_CUSTOM = 3;

    public static final String BASE_SYSTEM_URL = RetrofitFactory.BASE_SERVER_URL + "system/index";

    @BindView(R.id.wv_browser)
    WebView mBrowser;

    @BindView(R.id.toolbar_simple)
    Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);

        initToolBar();
        initBroswer();
    }

    public void initToolBar() {
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initBroswer() {

        Intent intent = getIntent();
        String url = "";
        int type = intent.getIntExtra("type", TYPE_SYSTEM);

        switch (type) {
            case TYPE_SYSTEM:
                url = BASE_SYSTEM_URL;
                break;
            case TYPE_CUSTOM:
                url = intent.getStringExtra("url");
                break;
            default:
                url = BASE_SYSTEM_URL;
        }


        WebSettings webSettings = mBrowser.getSettings();

        // enable javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
//        webSettings.setBuiltInZoomControls(true);

        if(Build.VERSION.SDK_INT >= 21){
            webSettings.setMixedContentMode(0);
            mBrowser.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >= 19){
            mBrowser.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19){
            mBrowser.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mBrowser.setWebViewClient(new Callback());
        mBrowser.setWebChromeClient(chromeClient);

        //设置cookies保存登录状态
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除

        for (Cookie cookie : LoginContext.setCookieCache) {
            cookieManager.setCookie(RetrofitFactory.BASE_SERVER_URL, cookie.name() + "=" + cookie.value());
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

        mBrowser.loadUrl(url);

    }

    /**
     * 处理返回键为返回上一页
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "keyCode=" + keyCode);
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mBrowser.canGoBack()) {
            mBrowser.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.webbroswer, menu);

        //菜单图标着色
        MenuItem menu_refresh = menu.findItem(R.id.menu_refresh);
        MenuItem menu_forward = menu.findItem(R.id.menu_forward);
        if (menu_refresh != null) {
            ColorUtil.tintMenuIcon(this, menu_refresh, android.R.color.holo_red_light);
        }
        if (menu_forward != null) {
            ColorUtil.tintMenuIcon(this, menu_forward, android.R.color.holo_red_light);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_forward:
                break;
            case R.id.menu_refresh:
                mBrowser.reload();
                break;
            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBrowser.destroy();
        mBrowser = null;
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    private String mCM;
    private final static int FCR=1;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;

    /*webview的文件上传功能，未完善*/
    private WebChromeClient chromeClient = new WebChromeClient(){

        //For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg){
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            BrowserActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FCR);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            BrowserActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FCR);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            BrowserActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
        }

        //For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams){
            if(mUMA != null){
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(BrowserActivity.this.getPackageManager()) != null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCM);
                }catch(IOException ex){
                    Log.e(TAG, "Image file creation failed", ex);
                }
                if(photoFile != null){
                    mCM = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                }else{
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("*/*");
            Intent[] intentArray;
            if(takePictureIntent != null){
                intentArray = new Intent[]{takePictureIntent};
            }else{
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, FCR);
            return true;
        }
    };

    public class Callback extends WebViewClient{

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }

        /*public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }*/
    }

    // Create an image file
    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;
            //Check if response is positive
            if(resultCode== Activity.RESULT_OK){
                if(requestCode == FCR){
                    if(null == mUMA){
                        return;
                    }
                    if(intent == null){
                        //Capture Photo if no image available
                        if(mCM != null){
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    }else{
                        String dataString = intent.getDataString();
                        if(dataString != null){
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        }else{
            if(requestCode == FCR){
                if(null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

}
