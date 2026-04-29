package nademkhan.example.jnistringbridge;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static {
        // 1. Nyalakan mesin Go
        System.loadLibrary("bigo_engine");
        // 2. Nyalakan jembatan C++
        System.loadLibrary("native-lib");
    }

    // Deklarasi fungsi native
    public native void StartBigoEngine(String id, String ffmpegPath);
    public native void StopBigoEngine(String id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // OTOMATIS MINTA IZIN FILE (Khusus Android 11 ke atas)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            }
        }

        WebView myWebView = findViewById(R.id.webview_dashboard);
        WebSettings webSettings = myWebView.getSettings();
        
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); 

        myWebView.addJavascriptInterface(new WebAppInterface(), "AndroidBridge");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("file:///android_asset/index.html");
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void startRecording(String userID) {
            Toast.makeText(MainActivity.this, "Mulai merekam ID: " + userID, Toast.LENGTH_SHORT).show();
            StartBigoEngine(userID, "ffmpeg");
        }

        @JavascriptInterface
        public void stopRecording(String userID) {
            Toast.makeText(MainActivity.this, "Stop rekaman ID: " + userID, Toast.LENGTH_SHORT).show();
            StopBigoEngine(userID);
        }
    }
}
