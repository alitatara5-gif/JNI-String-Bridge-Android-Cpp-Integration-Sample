package nademkhan.example.jnistringbridge;

import android.app.Service;
// ... import lainnya ...

public class RecorderService extends Service {
    static {
        System.loadLibrary("bigo_engine");
        System.loadLibrary("native-lib");
    }

    public native void StartBigoEngine(String id, String ffmpegPath);
    public native void StopBigoEngine(String id);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ... isi logic foreground service ...
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
