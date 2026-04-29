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
        // Urutan ini harga mati, Bang!
        System.loadLibrary("bigo_engine");
        System.loadLibrary("native-lib");
    }

    // Deklarasi ini harus sinkron sama nader.cpp
    public native void StartBigoEngine(String id, String ffmpegPath);
    public native void StopBigoEngine(String id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Minta Izin "Akses Semua File" (Wajib biar Go bisa nulis MP4)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        WebView myWebView = findViewById(R.id.webview_dashboard);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); 

        // Jembatan HTML -> Java
        myWebView.addJavascriptInterface(new WebAppInterface(), "AndroidBridge");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("file:///android_asset/index.html");
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void startRecording(String userID) {
            // Kita kirim "" (teks kosong) karena mesin Go sudah bawa FFmpeg sendiri
            StartBigoEngine(userID, "");
        }

        @JavascriptInterface
        public void stopRecording(String userID) {
            StopBigoEngine(userID);
        }
    }
}
