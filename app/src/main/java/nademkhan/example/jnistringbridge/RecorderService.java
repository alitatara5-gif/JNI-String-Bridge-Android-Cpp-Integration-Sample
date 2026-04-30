package nademkhan.example.jnistringbridge;

// IMPORT UNTUK SERVICE
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class RecorderService extends Service {
    public static final String CHANNEL_ID = "RecorderServiceChannel";

    static {
        System.loadLibrary("bigo_engine");
        System.loadLibrary("native-lib");
    }

    public native void StartBigoEngine(String id, String ffmpegPath);
    public native void StopBigoEngine(String id);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_NOT_STICKY;
        
        String userID = intent.getStringExtra("userID");
        String action = intent.getAction();

        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Recording Active")
                .setContentText("User: " + userID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();

        startForeground(1, notification);

        if ("START".equals(action)) {
            new Thread(() -> StartBigoEngine(userID, "")).start();
        } else if ("STOP".equals(action)) {
            StopBigoEngine(userID);
            stopForeground(true);
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID, "Recorder Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
