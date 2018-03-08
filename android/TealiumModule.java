package com.tealiumreactnative;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.tealium.library.*;
import com.tealium.library.BuildConfig;
import com.tealium.lifecycle.LifeCycle;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by karentamayo on 1/17/18.
 */

public class TealiumModule extends ReactContextBaseJavaModule {

    private static ReactApplicationContext mReactApplicationContext;

    private static String mTealiumInstanceName;
    private static boolean mIsLifecycleAutotracking = false;
    private boolean mDidTrackInitialLaunch = false;

    public TealiumModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactApplicationContext = reactContext;

    }

    @Override
    public String getName() {
        return "TealiumModule";
    }

    @ReactMethod
    public void initialize(String account,
                           String profile,
                           String environment,
                           String iosDatasource,
                           String androidDatasource,
                           String instance,
                           boolean isLifecycleEnabled) {

        if (account == null || profile == null || environment == null) {
            throw new IllegalArgumentException("Account, profile, and environment parameters must be provided and non-null");
        }

        final Tealium.Config config = Tealium.Config.create(mReactApplicationContext.getCurrentActivity().getApplication(), account, profile, environment);
        if (androidDatasource != null) {
            config.setDatasourceId(androidDatasource);
        }

        mTealiumInstanceName = instance;
        if (isLifecycleEnabled) {
            final boolean isAutoTracking = false;
            LifeCycle.setupInstance(mTealiumInstanceName, config, isAutoTracking);
            mIsLifecycleAutotracking = isLifecycleEnabled;
            mReactApplicationContext.addLifecycleEventListener(createLifecycleEventListener());
        }

        Tealium.createInstance(instance, config);
    }

    @ReactMethod
    public void trackEvent(String eventName, ReadableMap data) {
        final Tealium instance = Tealium.getInstance(mTealiumInstanceName);

        if (instance == null) {
            Log.e(BuildConfig.TAG, "TrackEvent attempted, but Tealium not enabled for instance name: " + mTealiumInstanceName);
            return;
        }

        if (data != null) {
            instance.trackView(eventName, data.toHashMap());
        } else {
            instance.trackView(eventName, null);
        }
    }

    @ReactMethod
    public void trackView(String viewName, ReadableMap data) {
        final Tealium instance = Tealium.getInstance(mTealiumInstanceName);

        if (instance == null) {
            Log.e(BuildConfig.TAG, "TrackView attempted, but Tealium not enabled for instance name: " + mTealiumInstanceName);
            return;
        }

        if (data != null) {
            instance.trackView(viewName, data.toHashMap());
        } else {
            instance.trackView(viewName, null);
        }
    }

    @ReactMethod
    public void setVolatileData(ReadableMap data) {
        final Tealium instance = Tealium.getInstance(mTealiumInstanceName);

        if (instance == null) {
            Log.e(BuildConfig.TAG, "SetVolatileData attempted, but Tealium not enabled for instance name: " + mTealiumInstanceName);
            return;
        }

        ReadableMapKeySetIterator iterator = data.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = data.getType(key);
            switch (type) {
                case String:
                    instance.getDataSources().getVolatileDataSources().put(key, data.getString(key));
                    break;
                case Array:
                    instance.getDataSources().getVolatileDataSources().put(key, data.getArray(key).toArrayList());
                    break;
                default:
                    throw new IllegalArgumentException("Could not set volatile data for key: " + key);
            }
        }
    }

    @ReactMethod
    public void setPersistentData(ReadableMap data) {
        final Tealium instance = Tealium.getInstance(mTealiumInstanceName);
        if (instance == null) {
            Log.e(BuildConfig.TAG, "SetPersistentData attempted, but Tealium not enabled for instance name: " + mTealiumInstanceName);
            return;
        }
        SharedPreferences sp = instance.getDataSources().getPersistentDataSources();

        Map<String, Object> obj = data.toHashMap();

        Iterator<String> keyIterator = obj.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            if (obj.get(key) instanceof String) {
                sp.edit().putString(key, (String) obj.get(key)).apply();

            } else if (obj.get(key) instanceof List) {
                Set<String> set = jsonArrayToStringSet(new JSONArray((List) obj.get(key)));
                sp.edit().putStringSet(key, set).apply();

            } else if (obj.get(key) instanceof JSONArray) {
                Set<String> set = jsonArrayToStringSet((JSONArray) obj.get(key));
                sp.edit().putStringSet(key, set).apply();

            } else {
                throw new IllegalArgumentException("Could not set persistent data for key: " + key);
            }
        }
    }

    @ReactMethod
    public void removeVolatileData(ReadableArray keyArray) {
        final Tealium instance = Tealium.getInstance(mTealiumInstanceName);

        for (int i = 0; i < keyArray.size(); i++) {
            ReadableType type = keyArray.getType(i);
            switch (type) {
                case String:
                    instance.getDataSources().getVolatileDataSources()
                            .remove(keyArray.getString(i));
                    break;
                default:
                    Log.e(BuildConfig.TAG, "Invalid key type. Use array of strings");
                    break;
            }
        }
    }

    @ReactMethod
    public void removePersistentData(ReadableArray keyArray) {
        final Tealium instance = Tealium.getInstance(mTealiumInstanceName);

        if (instance == null) {
            Log.e(BuildConfig.TAG, "RemovePersistentData attempted, but Tealium not enabled for instance name: " + mTealiumInstanceName);
            return;
        }

        for (int i = 0; i < keyArray.size(); i++) {
            ReadableType type = keyArray.getType(i);
            switch (type) {
                case String:
                    instance.getDataSources().getPersistentDataSources()
                            .edit()
                            .remove(keyArray.getString(i))
                            .apply();
                    break;
                default:
                    Log.e(BuildConfig.TAG, "Invalid key type. Use array of strings");
            }
        }
    }

    @ReactMethod
    public void getVolatileData(String key, Callback callback) {
        final Tealium instance = Tealium.getInstance(mTealiumInstanceName);
        if (instance == null) {
            Log.e(BuildConfig.TAG, "Attempt to get volatile data, but Tealium not enabled for instance name: " + mTealiumInstanceName);
            return;
        }

        callback.invoke(instance.getDataSources().getVolatileDataSources().get(key));
    }

    @ReactMethod
    public void getPersistentData(String key, Callback callback) {
        final Tealium instance = Tealium.getInstance(mTealiumInstanceName);
        if (instance == null) {
            Log.e(BuildConfig.TAG, "Attempt to get persistent data, but Tealium not enabled for instance name: " + mTealiumInstanceName);
            return;
        }

        Map<String, ?> allPersistentData = instance.getDataSources().getPersistentDataSources().getAll();
        callback.invoke(allPersistentData.get(key));
    }

    private LifecycleEventListener createLifecycleEventListener() {

        return new LifecycleEventListener() {
            @Override
            public void onHostResume() {
                if (!mDidTrackInitialLaunch) {
                    final Handler handler = new Handler();
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (mIsLifecycleAutotracking) {
                                        LifeCycle lf = LifeCycle.getInstance(mTealiumInstanceName);
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("autotracked", "true");
                                        lf.trackLaunchEvent(data);
                                        mDidTrackInitialLaunch = true;
                                    }
                                }
                            },
                            700);
                } else {
                    if (mIsLifecycleAutotracking) {
                        LifeCycle lf = LifeCycle.getInstance(mTealiumInstanceName);
                        Map<String, Object> data = new HashMap<>();
                        data.put("autotracked", "true");
                        lf.trackWakeEvent(data);
                    }
                }
            }

            @Override
            public void onHostPause() {
                if (mIsLifecycleAutotracking) {
                    LifeCycle lf = LifeCycle.getInstance(mTealiumInstanceName);
                    Map<String, Object> data = new HashMap<>();
                    data.put("autotracked", "true");
                    lf.trackSleepEvent(data);
                }
            }

            @Override
            public void onHostDestroy() {
            }
        };
    }

    private Set<String> jsonArrayToStringSet(JSONArray json) {
        Set<String> strSet = new HashSet<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                strSet.add(json.getString(i));
            } catch (JSONException e) {
                Log.e(BuildConfig.TAG, e.toString());
            }
        }
        return strSet;
    }

    JSONArray stringSetToJsonArray(Set<String> set) {
        JSONArray array = new JSONArray();

        for (Object item : set) {
            if (item instanceof String) {
                array.put(item);
            }
        }

        return array;
    }

}