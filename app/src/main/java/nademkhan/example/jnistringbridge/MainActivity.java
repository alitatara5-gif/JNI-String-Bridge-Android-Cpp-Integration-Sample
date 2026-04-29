package nademkhan.example.jnistringbridge;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static {
        // Load library JNI/C++ kita
        System.loadLibrary("native-lib");
    }

    // Deklarasi fungsi native yang ada di Go (lewat native-lib.cpp)
    public native void StartBigoEngine(String id, String ffmpegPath);
    public native void StopBigoEngine(String id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = findViewById(R.id.webview_dashboard);
        WebSettings webSettings = myWebView.getSettings();
        
        // Aktifkan JavaScript supaya tombol di HTML jalan
        webSettings.setJavaScriptEnabled(true);
        // Penting buat nyimpen data lokal di HTML
        webSettings.setDomStorageEnabled(true); 

        // Buat jembatan bernama "AndroidBridge" yang bisa dipanggil dari JS HTML
        myWebView.addJavascriptInterface(new WebAppInterface(), "AndroidBridge");

        // Pastikan link gak ngebuka browser luar
        myWebView.setWebViewClient(new WebViewClient());

        // Load file HTML yang kita buat di assets tadi
        myWebView.loadUrl("file:///android_asset/index.html");
    }

    // Kelas Jembatan HTML -> Java
    public class WebAppInterface {
        @JavascriptInterface // Wajib ada agar JS bisa manggil fungsi ini
        public void startRecording(String userID) {
            // Tampilkan toast buat konfirmasi tombol ditekan
            Toast.makeText(MainActivity.this, "Mulai merekam ID: " + userID, Toast.LENGTH_SHORT).show();
            
            // !!! INI EKSEKUSI MESIN GO !!!
            // Path "ffmpeg" nanti bakal dihandle sama FFmpeg-Kit otomatis
            StartBigoEngine(userID, "ffmpeg");
        }

        @JavascriptInterface
        public void stopRecording(String userID) {
            Toast.makeText(MainActivity.this, "Stop rekaman ID: " + userID, Toast.LENGTH_SHORT).show();
            
            // !!! INI KONFIRMASI STOP KE GO !!!
            StopBigoEngine(userID);
        }
    }
}
