package nademkhan.example.jnistringbridge;

import android.content.Intent;
// ... import lainnya ...

public class MainActivity extends AppCompatActivity {
    // ... isi onCreate ...

    public class WebAppInterface {
        @JavascriptInterface
        public void startRecording(String userID) {
            Intent intent = new Intent(MainActivity.this, RecorderService.class);
            intent.setAction("START");
            intent.putExtra("userID", userID);
            // ... dst ...
        }

        @JavascriptInterface
        public void stopRecording(String userID) {
            Intent intent = new Intent(MainActivity.this, RecorderService.class);
            intent.setAction("STOP");
            // ... dst ...
        }
    }
}
// JANGAN ADA KELAS LAIN DI LUAR KURUNG KURAWAL INI
