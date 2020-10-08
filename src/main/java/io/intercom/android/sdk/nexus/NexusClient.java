package io.intercom.android.sdk.nexus;

import android.annotation.TargetApi;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.LruCache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@TargetApi(14)
public class NexusClient {
    private Handler backgroundHandler;
    /* access modifiers changed from: private */
    public final LruCache<String, Boolean> cache = new LruCache<>(100);
    /* access modifiers changed from: private */
    public final List<NexusListener> listeners = new CopyOnWriteArrayList();
    private long presenceInterval;
    private final List<NexusSocket> sockets = new ArrayList();

    public NexusClient() {
        HandlerThread bgThread = new HandlerThread("background-handler");
        bgThread.start();
        this.backgroundHandler = new Handler(bgThread.getLooper());
    }

    public void connect(NexusConfig config, boolean shouldSendPresence) {
        if (config.getEndpoints().isEmpty()) {
            NexusLogger.errorLog("No endpoints present");
        }
        for (String url : config.getEndpoints()) {
            NexusLogger.d("adding socket");
            this.sockets.add(new NexusSocket(url, config.getConnectionTimeout(), shouldSendPresence, new SocketListener()));
        }
        this.presenceInterval = TimeUnit.SECONDS.toMillis((long) config.getPresenceHeartbeatInterval());
        if (shouldSendPresence) {
            schedulePresence();
        }
    }

    public synchronized void disconnect() {
        if (!this.sockets.isEmpty()) {
            for (NexusSocket nexusSocket : this.sockets) {
                NexusLogger.d("disconnecting socket");
                nexusSocket.disconnect();
            }
            this.sockets.clear();
            NexusLogger.d("client disconnected");
        }
        this.backgroundHandler.removeCallbacksAndMessages((Object) null);
    }

    public synchronized void fire(NexusEvent event) {
        this.cache.put(event.getGuid(), true);
        String data = event.toJsonFormattedString();
        if (!data.isEmpty()) {
            for (NexusSocket nexusSocket : this.sockets) {
                nexusSocket.fire(data);
            }
        }
    }

    public synchronized void localUpdate(NexusEvent event) {
        for (NexusListener nexusListener : this.listeners) {
            nexusListener.notifyEvent(event);
        }
    }

    public synchronized boolean isConnected() {
        boolean z;
        Iterator<NexusSocket> it = this.sockets.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().isConnected()) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        return z;
    }

    public void addEventListener(NexusListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    public void removeEventListener(NexusListener listener) {
        this.listeners.remove(listener);
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        NexusLogger.setLoggingEnabled(loggingEnabled);
    }

    public void setPresenceHeartbeatEnabled(boolean presenceHeartbeatEnabled) {
        this.backgroundHandler.removeCallbacksAndMessages((Object) null);
        if (presenceHeartbeatEnabled) {
            schedulePresence();
        }
    }

    /* access modifiers changed from: private */
    public void schedulePresence() {
        if (this.presenceInterval > 0) {
            this.backgroundHandler.postDelayed(new Runnable() {
                public void run() {
                    NexusClient.this.fire(NexusEvent.UserPresence);
                    NexusClient.this.schedulePresence();
                }
            }, this.presenceInterval);
        }
    }

    private class SocketListener implements NexusListener {
        private SocketListener() {
        }

        public void notifyEvent(NexusEvent event) {
            if (event != NexusEvent.UNKNOWN) {
                synchronized (NexusClient.this.cache) {
                    if (NexusClient.this.cache.get(event.getGuid()) == null) {
                        NexusClient.this.cache.put(event.getGuid(), true);
                        NexusLogger.d("notifying listeners of event: " + event);
                        for (NexusListener listener : NexusClient.this.listeners) {
                            listener.notifyEvent(event);
                        }
                    } else {
                        NexusLogger.v("dropping event, already in cache:" + event.toJsonFormattedString());
                    }
                }
            }
        }

        public void onConnect() {
            NexusLogger.d("notifying listeners that a connection opened");
            for (NexusListener listener : NexusClient.this.listeners) {
                listener.onConnect();
            }
        }

        public void onConnectFailed() {
            NexusLogger.d("notifying listeners that a connection failed to open");
            for (NexusListener listener : NexusClient.this.listeners) {
                listener.onConnectFailed();
            }
        }
    }
}
