package com.google.android.gms.tagmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataLayer {
    public static final String EVENT_KEY = "event";
    public static final Object OBJECT_NOT_PRESENT = new Object();
    static final String[] zzbir = "gtm.lifetime".toString().split("\\.");
    private static final Pattern zzbis = Pattern.compile("(\\d+)\\s*([smhd]?)");
    private final ConcurrentHashMap<zzb, Integer> zzbit;
    private final Map<String, Object> zzbiu;
    private final ReentrantLock zzbiv;
    private final LinkedList<Map<String, Object>> zzbiw;
    private final zzc zzbix;
    /* access modifiers changed from: private */
    public final CountDownLatch zzbiy;

    static final class zza {
        public final Object zzNc;
        public final String zzvs;

        zza(String str, Object obj) {
            this.zzvs = str;
            this.zzNc = obj;
        }

        public boolean equals(Object o) {
            if (!(o instanceof zza)) {
                return false;
            }
            zza zza = (zza) o;
            return this.zzvs.equals(zza.zzvs) && this.zzNc.equals(zza.zzNc);
        }

        public int hashCode() {
            return Arrays.hashCode(new Integer[]{Integer.valueOf(this.zzvs.hashCode()), Integer.valueOf(this.zzNc.hashCode())});
        }

        public String toString() {
            return "Key: " + this.zzvs + " value: " + this.zzNc.toString();
        }
    }

    interface zzb {
        void zzQ(Map<String, Object> map);
    }

    interface zzc {

        public interface zza {
            void zzB(List<zza> list);
        }

        void zza(zza zza2);

        void zza(List<zza> list, long j);

        void zzfZ(String str);
    }

    DataLayer() {
        this(new zzc() {
            public void zza(zzc.zza zza) {
                zza.zzB(new ArrayList());
            }

            public void zza(List<zza> list, long j) {
            }

            public void zzfZ(String str) {
            }
        });
    }

    DataLayer(zzc persistentStore) {
        this.zzbix = persistentStore;
        this.zzbit = new ConcurrentHashMap<>();
        this.zzbiu = new HashMap();
        this.zzbiv = new ReentrantLock();
        this.zzbiw = new LinkedList<>();
        this.zzbiy = new CountDownLatch(1);
        zzGn();
    }

    public static List<Object> listOf(Object... objects) {
        ArrayList arrayList = new ArrayList();
        for (Object add : objects) {
            arrayList.add(add);
        }
        return arrayList;
    }

    public static Map<String, Object> mapOf(Object... objects) {
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("expected even number of key-value pairs");
        }
        HashMap hashMap = new HashMap();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= objects.length) {
                return hashMap;
            }
            if (!(objects[i2] instanceof String)) {
                throw new IllegalArgumentException("key is not a string: " + objects[i2]);
            }
            hashMap.put(objects[i2], objects[i2 + 1]);
            i = i2 + 2;
        }
    }

    private void zzGn() {
        this.zzbix.zza(new zzc.zza() {
            public void zzB(List<zza> list) {
                for (zza next : list) {
                    DataLayer.this.zzS(DataLayer.this.zzn(next.zzvs, next.zzNc));
                }
                DataLayer.this.zzbiy.countDown();
            }
        });
    }

    private void zzGo() {
        int i = 0;
        while (true) {
            int i2 = i;
            Map poll = this.zzbiw.poll();
            if (poll != null) {
                zzX(poll);
                i = i2 + 1;
                if (i > 500) {
                    this.zzbiw.clear();
                    throw new RuntimeException("Seems like an infinite loop of pushing to the data layer");
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void zzS(Map<String, Object> map) {
        this.zzbiv.lock();
        try {
            this.zzbiw.offer(map);
            if (this.zzbiv.getHoldCount() == 1) {
                zzGo();
            }
            zzT(map);
        } finally {
            this.zzbiv.unlock();
        }
    }

    private void zzT(Map<String, Object> map) {
        Long zzU = zzU(map);
        if (zzU != null) {
            List<zza> zzW = zzW(map);
            zzW.remove("gtm.lifetime");
            this.zzbix.zza(zzW, zzU.longValue());
        }
    }

    private Long zzU(Map<String, Object> map) {
        Object zzV = zzV(map);
        if (zzV == null) {
            return null;
        }
        return zzfY(zzV.toString());
    }

    private Object zzV(Map<String, Object> map) {
        String[] strArr = zzbir;
        int length = strArr.length;
        int i = 0;
        Object obj = map;
        while (i < length) {
            String str = strArr[i];
            if (!(obj instanceof Map)) {
                return null;
            }
            i++;
            obj = ((Map) obj).get(str);
        }
        return obj;
    }

    private List<zza> zzW(Map<String, Object> map) {
        ArrayList arrayList = new ArrayList();
        zza(map, "", arrayList);
        return arrayList;
    }

    private void zzX(Map<String, Object> map) {
        synchronized (this.zzbiu) {
            for (String next : map.keySet()) {
                zzd(zzn(next, map.get(next)), this.zzbiu);
            }
        }
        zzY(map);
    }

    private void zzY(Map<String, Object> map) {
        for (zzb zzQ : this.zzbit.keySet()) {
            zzQ.zzQ(map);
        }
    }

    private void zza(Map<String, Object> map, String str, Collection<zza> collection) {
        for (Map.Entry next : map.entrySet()) {
            String str2 = str + (str.length() == 0 ? "" : ".") + ((String) next.getKey());
            if (next.getValue() instanceof Map) {
                zza((Map) next.getValue(), str2, collection);
            } else if (!str2.equals("gtm.lifetime")) {
                collection.add(new zza(str2, next.getValue()));
            }
        }
    }

    static Long zzfY(String str) {
        long j;
        Matcher matcher = zzbis.matcher(str);
        if (!matcher.matches()) {
            zzbg.zzaJ("unknown _lifetime: " + str);
            return null;
        }
        try {
            j = Long.parseLong(matcher.group(1));
        } catch (NumberFormatException e) {
            zzbg.zzaK("illegal number in _lifetime value: " + str);
            j = 0;
        }
        if (j <= 0) {
            zzbg.zzaJ("non-positive _lifetime: " + str);
            return null;
        }
        String group = matcher.group(2);
        if (group.length() == 0) {
            return Long.valueOf(j);
        }
        switch (group.charAt(0)) {
            case 'd':
                return Long.valueOf(j * 1000 * 60 * 60 * 24);
            case 'h':
                return Long.valueOf(j * 1000 * 60 * 60);
            case 'm':
                return Long.valueOf(j * 1000 * 60);
            case 's':
                return Long.valueOf(j * 1000);
            default:
                zzbg.zzaK("unknown units in _lifetime: " + str);
                return null;
        }
    }

    public Object get(String key) {
        synchronized (this.zzbiu) {
            Object obj = this.zzbiu;
            String[] split = key.split("\\.");
            int length = split.length;
            Object obj2 = obj;
            int i = 0;
            while (i < length) {
                String str = split[i];
                if (!(obj2 instanceof Map)) {
                    return null;
                }
                Object obj3 = ((Map) obj2).get(str);
                if (obj3 == null) {
                    return null;
                }
                i++;
                obj2 = obj3;
            }
            return obj2;
        }
    }

    public void push(String key, Object value) {
        push(zzn(key, value));
    }

    public void push(Map<String, Object> update) {
        try {
            this.zzbiy.await();
        } catch (InterruptedException e) {
            zzbg.zzaK("DataLayer.push: unexpected InterruptedException");
        }
        zzS(update);
    }

    public void pushEvent(String eventName, Map<String, Object> update) {
        HashMap hashMap = new HashMap(update);
        hashMap.put("event", eventName);
        push(hashMap);
    }

    public String toString() {
        String sb;
        synchronized (this.zzbiu) {
            StringBuilder sb2 = new StringBuilder();
            for (Map.Entry next : this.zzbiu.entrySet()) {
                sb2.append(String.format("{\n\tKey: %s\n\tValue: %s\n}\n", new Object[]{next.getKey(), next.getValue()}));
            }
            sb = sb2.toString();
        }
        return sb;
    }

    /* access modifiers changed from: package-private */
    public void zza(zzb zzb2) {
        this.zzbit.put(zzb2, 0);
    }

    /* access modifiers changed from: package-private */
    public void zzb(List<Object> list, List<Object> list2) {
        while (list2.size() < list.size()) {
            list2.add((Object) null);
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < list.size()) {
                Object obj = list.get(i2);
                if (obj instanceof List) {
                    if (!(list2.get(i2) instanceof List)) {
                        list2.set(i2, new ArrayList());
                    }
                    zzb((List) obj, (List) list2.get(i2));
                } else if (obj instanceof Map) {
                    if (!(list2.get(i2) instanceof Map)) {
                        list2.set(i2, new HashMap());
                    }
                    zzd((Map) obj, (Map) list2.get(i2));
                } else if (obj != OBJECT_NOT_PRESENT) {
                    list2.set(i2, obj);
                }
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void zzd(Map<String, Object> map, Map<String, Object> map2) {
        for (String next : map.keySet()) {
            Object obj = map.get(next);
            if (obj instanceof List) {
                if (!(map2.get(next) instanceof List)) {
                    map2.put(next, new ArrayList());
                }
                zzb((List) obj, (List) map2.get(next));
            } else if (obj instanceof Map) {
                if (!(map2.get(next) instanceof Map)) {
                    map2.put(next, new HashMap());
                }
                zzd((Map) obj, (Map) map2.get(next));
            } else {
                map2.put(next, obj);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void zzfX(String str) {
        push(str, (Object) null);
        this.zzbix.zzfZ(str);
    }

    /* access modifiers changed from: package-private */
    public Map<String, Object> zzn(String str, Object obj) {
        HashMap hashMap = new HashMap();
        String[] split = str.toString().split("\\.");
        int i = 0;
        HashMap hashMap2 = hashMap;
        while (i < split.length - 1) {
            HashMap hashMap3 = new HashMap();
            hashMap2.put(split[i], hashMap3);
            i++;
            hashMap2 = hashMap3;
        }
        hashMap2.put(split[split.length - 1], obj);
        return hashMap;
    }
}
