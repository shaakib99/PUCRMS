package com.example.wahid.pucrms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.widget.Toast;

public class myalarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
        Toast.makeText(context,"alarm triggered",Toast.LENGTH_LONG).show();
    }
}
