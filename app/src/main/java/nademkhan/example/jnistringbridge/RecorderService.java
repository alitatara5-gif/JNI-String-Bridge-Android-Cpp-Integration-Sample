package nademkhan.example.jnistringbridge;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.io.*;

public class RecorderService extends Service {
    private static Process engineProcess;
    private static BufferedWriter engineInput;
    public static final String CHANNEL_ID = "RecorderServiceChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY;
        String action = intent.getAction();
        String userID = intent.getStringExtra("userID");

        if ("START_ENGINE".equals(action)) {
            startNativeEngine();
        } else if ("ADD_ID".equals(action)) {
            sendCommand(userID);
        } else if ("STOP_ID".equals(action)) {
            sendCommand("stop " + userID);
        }

        updateNotification("Bigo Engine", action != null ? action : "Running");
        return START_STICKY;
    }

    private void startNativeEngine() {
        if (engineProcess != null) return;
        new Thread(() -> {
            try {
                File binary = new File(getFilesDir(), "bigo_multi");
                if (!binary.exists()) {
                    copyFile(getAssets().open("bigo_multi"), new FileOutputStream(binary));
                    binary.setExecutable(true);
                }

                engineProcess = Runtime.getRuntime().exec(binary.getAbsolutePath());
                engineInput = new BufferedWriter(new OutputStreamWriter(engineProcess.getOutputStream()));

                BufferedReader reader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("BIGO_GO", line);
                }
            } catch (Exception e) {
                Log.e("BIGO_ERROR", "Gagal load engine", e);
            }
        }).start();
    }

    private void sendCommand(String cmd) {
        new Thread(() -> {
            try {
                if (engineInput != null) {
                    engineInput.write(cmd);
                    engineInput.newLine();
                    engineInput.flush();
                }
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
        in.close(); out.close();
    }

    private void updateNotification(String title, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Recorder", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title).setContentText(text)
                .setSmallIcon(android.R.drawable.ic_media_play).build();
        startForeground(1, notification);
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
