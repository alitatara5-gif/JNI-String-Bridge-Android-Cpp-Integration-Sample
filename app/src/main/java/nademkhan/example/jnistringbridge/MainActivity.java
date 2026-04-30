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
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        // Jalankan mesin Go saat aplikasi dibuka
        Intent engineIntent = new Intent(this, RecorderService.class);
        engineIntent.setAction("START_ENGINE");
        startService(engineIntent);

        WebView myWebView = findViewById(R.id.webview_dashboard);
        WebSettings ws = myWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true);

        myWebView.addJavascriptInterface(new WebAppInterface(), "AndroidBridge");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("file:///android_asset/index.html");
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void startRecording(String userID) {
            Intent intent = new Intent(MainActivity.this, RecorderService.class);
            intent.setAction("ADD_ID");
            intent.putExtra("userID", userID);
            startService(intent);
        }

        @JavascriptInterface
        public void stopRecording(String userID) {
            Intent intent = new Intent(MainActivity.this, RecorderService.class);
            intent.setAction("STOP_ID");
            intent.putExtra("userID", userID);
            startService(intent);
        }
    }
}
