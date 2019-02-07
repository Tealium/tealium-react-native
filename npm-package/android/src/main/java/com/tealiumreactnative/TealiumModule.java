package com.tealiumreactnative;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.tealium.library.BuildConfig;
import com.tealium.library.ConsentManager;
import com.tealium.library.Tealium;
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
            mReactApplicationContext.addLifecycleEventListener(createLifecycleEventListener(mTealiumInstanceName));
        }

        Tealium.createInstance(instance, config);
    }

    @ReactMethod
    public void initializeWithConsentManager(String account,
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
        config.enableConsentManager(mTealiumInstanceName);

        mTealiumInstanceName = instance;
        if (isLifecycleEnabled) {
            final boolean isAutoTracking = false;
            LifeCycle.setupInstance(mTealiumInstanceName, config, isAutoTracking);
            mIsLifecycleAutotracking = isLifecycleEnabled;
            mReactApplicationContext.addLifecycleEventListener(createLifecycleEventListener(mTealiumInstanceName));
        }

        Tealium.createInstance(instance, config);
    }

    @ReactMethod
    public void initializeCustom(String account,
                                 String profile,
                                 String environment,
                                 String iosDatasource,
                                 String androidDatasource,
                                 String instance,
                                 boolean isLifecycleEnabled,
                                 String overridePublishSettingsUrl,
                                 String overrideTagManagementUrl,
                                 boolean enableCollectUrl,
                                 boolean enableConsentManager) {

        if (account == null || profile == null || environment == null) {
            throw new IllegalArgumentException("Account, profile, and environment parameters must be provided and non-null");
        }

        final Tealium.Config config = Tealium.Config.create(mReactApplicationContext.getCurrentActivity().getApplication(), account, profile, environment);
        if (androidDatasource != null) {
            config.setDatasourceId(androidDatasource);
        }
        if (overridePublishSettingsUrl != null) {
            config.setOverridePublishSettingsUrl(overridePublishSettingsUrl);
        }
        if (overrideTagManagementUrl != null) {
            config.setOverrideTagManagementUrl(overrideTagManagementUrl);
        }
        if (!enableCollectUrl) {
            config.setVdataCollectEndpointEnabled(true);
        }
        if (enableConsentManager) {
            config.enableConsentManager(instance);
        }

        if (isLifecycleEnabled) {
            final boolean isAutoTracking = false;
            LifeCycle.setupInstance(instance, config, isAutoTracking);
            mIsLifecycleAutotracking = isLifecycleEnabled;
            mReactApplicationContext.addLifecycleEventListener(createLifecycleEventListener(instance));
        }
        Tealium.createInstance(instance, config);
    }

    @ReactMethod
    public void trackEvent(String eventName, ReadableMap data) {
        trackEventForInstance(mTealiumInstanceName, eventName, data);
    }

    @ReactMethod
    public void trackEventForInstance(String instanceName, String eventName, ReadableMap data) {
        final Tealium instance = Tealium.getInstance(instanceName);
        if (instance == null) {
            Log.e(BuildConfig.TAG, "TrackEvent attempted, but Tealium not enabled for instance name: " + instanceName);
            return;
        }

        if (data != null) {
            instance.trackEvent(eventName, data.toHashMap());
        } else {
            instance.trackEvent(eventName, null);
        }
    }

    @ReactMethod
    public void trackView(String viewName, ReadableMap data) {
        trackViewForInstance(mTealiumInstanceName, viewName, data);
    }

    @ReactMethod
    public void trackViewForInstance(String instanceName, String viewName, ReadableMap data) {
        final Tealium instance = Tealium.getInstance(instanceName);

        if (instance == null) {
            Log.e(BuildConfig.TAG, "TrackView attempted, but Tealium not enabled for instance name: " + instanceName);
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
        setVolatileDataForInstance(mTealiumInstanceName, data);
    }

    @ReactMethod
    public void setVolatileDataForInstance(String instanceName, ReadableMap data) {
        final Tealium instance = Tealium.getInstance(instanceName);

        if (instance == null) {
            Log.e(BuildConfig.TAG, "SetVolatileData attempted, but Tealium not enabled for instance name: " + instanceName);
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
        setPersistentDataForInstance(mTealiumInstanceName, data);
    }

    @ReactMethod
    public void setPersistentDataForInstance(String instanceName, ReadableMap data) {
        final Tealium instance = Tealium.getInstance(instanceName);
        if (instance == null) {
            Log.e(BuildConfig.TAG, "SetPersistentData attempted, but Tealium not enabled for instance name: " + instanceName);
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
        removeVolatileDataForInstance(mTealiumInstanceName, keyArray);
    }

    @ReactMethod
    public void removeVolatileDataForInstance(String instanceName, ReadableArray keyArray) {
        final Tealium instance = Tealium.getInstance(instanceName);

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
        removePersistentDataForInstance(mTealiumInstanceName, keyArray);
    }

    @ReactMethod
    public void removePersistentDataForInstance(String instanceName, ReadableArray keyArray) {
        final Tealium instance = Tealium.getInstance(instanceName);

        if (instance == null) {
            Log.e(BuildConfig.TAG, "RemovePersistentData attempted, but Tealium not enabled for instance name: " + instanceName);
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
        getVolatileDataForInstance(mTealiumInstanceName, key, callback);
    }

    @ReactMethod
    public void getVolatileDataForInstance(String instanceName, String key, Callback callback) {
        final Tealium instance = Tealium.getInstance(instanceName);
        if (instance == null) {
            Log.e(BuildConfig.TAG, "Attempt to get volatile data, but Tealium not enabled for instance name: " + instanceName);
            return;
        }

        callback.invoke(instance.getDataSources().getVolatileDataSources().get(key));
    }

    @ReactMethod
    public void getPersistentData(String key, Callback callback) {
        getPersistentDataForInstance(mTealiumInstanceName, key, callback);
    }

    @ReactMethod
    public void getPersistentDataForInstance(String instanceName, String key, Callback callback) {
        final Tealium instance = Tealium.getInstance(instanceName);
        if (instance == null) {
            Log.e(BuildConfig.TAG, "Attempt to get persistent data, but Tealium not enabled for instance name: " + instanceName);
            return;
        }

        Map<String, ?> allPersistentData = instance.getDataSources().getPersistentDataSources().getAll();
        callback.invoke(allPersistentData.get(key));
    }

    private LifecycleEventListener createLifecycleEventListener(final String instanceName) {

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
                                        LifeCycle lf = LifeCycle.getInstance(instanceName);
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
                        LifeCycle lf = LifeCycle.getInstance(instanceName);
                        Map<String, Object> data = new HashMap<>();
                        data.put("autotracked", "true");
                        lf.trackWakeEvent(data);
                    }
                }
            }

            @Override
            public void onHostPause() {
                if (mIsLifecycleAutotracking) {
                    LifeCycle lf = LifeCycle.getInstance(instanceName);
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

    @ReactMethod
    public void getVisitorID(Callback callback) {
        getVisitorIDForInstance(mTealiumInstanceName, callback);
    }

    @ReactMethod
    public void getVisitorIDForInstance(String instanceName, Callback callback) {
        callback.invoke(Tealium.getInstance(instanceName).getDataSources().getVisitorId());
    }

    @ReactMethod
    public void getUserConsentStatus(Callback callback) {
        getUserConsentStatusForInstance(mTealiumInstanceName, callback);
    }

    @ReactMethod
    public void getUserConsentStatusForInstance(String instanceName, Callback callback) {
        Tealium tealium = Tealium.getInstance(instanceName);
        if (tealium.getConsentManager() != null) {
            tealium.getConsentManager().getUserConsentStatus();
        }
    }

    @ReactMethod
    public void setUserConsentStatus(int userConsentStatus) {
        setUserConsentStatusForInstance(mTealiumInstanceName, userConsentStatus);
    }

    @ReactMethod
    public void setUserConsentStatusForInstance(String instanceName, int userConsentStatus) {
        String consentStatus = mapUserConsentStatus(userConsentStatus);
        Tealium tealium = Tealium.getInstance(instanceName);
        if (tealium.getConsentManager() != null) {
            tealium.getConsentManager().setUserConsentStatus(consentStatus);
        }
    }

    @ReactMethod
    public void getUserConsentCategories(Callback callback) {
        getUserConsentCategoriesForInstance(mTealiumInstanceName, callback);
    }

    @ReactMethod
    public void getUserConsentCategoriesForInstance(String instanceName, Callback callback) {
        Tealium tealium = Tealium.getInstance(instanceName);
        if (tealium.getConsentManager() != null) {
            callback.invoke(tealium.getConsentManager().getUserConsentCategories());
        }
    }

    @ReactMethod
    public void setUserConsentCategories(ReadableArray categories) {
        setUserConsentCategoriesForInstance(mTealiumInstanceName, categories);
    }

    @ReactMethod
    public void setUserConsentCategoriesForInstance(String instanceName, ReadableArray categories) {
        Tealium tealium = Tealium.getInstance(instanceName);
        if (tealium.getConsentManager() != null) {
            String[] userConsentCategories = new String[categories.toArrayList().size()];
            for (int i = 0; i < categories.size(); i++) {
                ReadableType type = categories.getType(i);
                switch (type) {
                    case String:
                        userConsentCategories[i] = categories.getString(i);
                        break;
                    default:
                        Log.e(BuildConfig.TAG, "Invalid key type. Use array of strings");
                        break;
                }
            }
            tealium.getConsentManager().setUserConsentCategories(userConsentCategories);
        }
    }

    @ReactMethod
    public void resetUserConsentPreferences() {
        resetUserConsentPreferencesForInstance(mTealiumInstanceName);
    }

    @ReactMethod
    public void resetUserConsentPreferencesForInstance(String instanceName) {
        Tealium tealium = Tealium.getInstance(instanceName);
        if (tealium.getConsentManager() != null) {
            tealium.getConsentManager().getUserConsentCategories();
        }
    }

    @ReactMethod
    public void setConsentLoggingEnabled(boolean isLogging) {
        setConsentLoggingEnabledForInstance(mTealiumInstanceName, isLogging);
    }

    @ReactMethod
    public void setConsentLoggingEnabledForInstance(String instanceName, boolean isLogging) {
        Tealium tealium = Tealium.getInstance(instanceName);
        if (tealium.getConsentManager() != null) {
            tealium.getConsentManager().setConsentLoggingEnabled(isLogging);
        }
    }

    @ReactMethod
    public void isConsentLoggingEnabled(Callback callback) {
        isConsentLoggingEnabledForInstance(mTealiumInstanceName, callback);
    }

    @ReactMethod
    public void isConsentLoggingEnabledForInstance(String instanceName, Callback callback) {
        Tealium tealium = Tealium.getInstance(instanceName);
        if (tealium.getConsentManager() != null) {
            callback.invoke(tealium.getConsentManager().isConsentLogging());
        }
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

    private String mapUserConsentStatus(int userConsentStatus) {
        switch (userConsentStatus) {
            case 0:
                return ConsentManager.ConsentStatus.UNKNOWN;
            case 1:
                return ConsentManager.ConsentStatus.CONSENTED;
            case 2:
                return ConsentManager.ConsentStatus.NOT_CONSENTED;
            default:
                return ConsentManager.ConsentStatus.UNKNOWN;
        }
    }

}