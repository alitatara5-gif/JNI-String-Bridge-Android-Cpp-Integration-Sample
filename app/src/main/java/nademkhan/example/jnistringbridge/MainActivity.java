package nademkhan.example.jnistringbridge;

// --- BAGIAN IMPORT (WAJIB ADA SEMUA) ---
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

        // Meminta izin akses file untuk Android 11 ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        // Setup WebView
        WebView myWebView = findViewById(R.id.webview_dashboard);
        WebSettings ws = myWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setAllowContentAccess(true);

        // Menghubungkan HTML dengan Java (AndroidBridge)
        myWebView.addJavascriptInterface(new WebAppInterface(), "AndroidBridge");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("file:///android_asset/index.html");
    }

    // Kelas jembatan untuk JavaScript
    public class WebAppInterface {
        
        @JavascriptInterface
        public void startRecording(String userID) {
            // Memanggil Service untuk mulai rekam
            Intent intent = new Intent(MainActivity.this, RecorderService.class);
            intent.setAction("START");
            intent.putExtra("userID", userID);
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }

        @JavascriptInterface
        public void stopRecording(String userID) {
            // Memanggil Service untuk stop rekam
            Intent intent = new Intent(MainActivity.this, RecorderService.class);
            intent.setAction("STOP");
            intent.putExtra("userID", userID);
            startService(intent);
        }
    }
}
