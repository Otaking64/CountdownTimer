package com.ota.countdowntimer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class timer_service extends Service {
    public timer_service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
