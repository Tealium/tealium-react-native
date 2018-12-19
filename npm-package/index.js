import { NativeModules } from 'react-native';

const { TealiumModule } = NativeModules;

export default class Tealium {
    static initialize(
        account, 
        profile, 
        environment, 
        iosDatasource,
        androidDatasource, 
        instanceName = 'MAIN', 
        isLifecycleEnabled = true,
    ) {
        TealiumModule.initialize(
            account, 
            profile, 
            environment, 
            iosDatasource, 
            androidDatasource,
            instanceName, 
            isLifecycleEnabled,
        );
    }

    static initializeWithConsentManager(
        account, 
        profile, 
        environment, 
        iosDatasource,
        androidDatasource, 
        instanceName = 'MAIN', 
        isLifecycleEnabled = true,
    ) {
        TealiumModule.initializeWithConsentManager(
            account, 
            profile, 
            environment, 
            iosDatasource, 
            androidDatasource,
            instanceName, 
            isLifecycleEnabled,
        );
    }

    static initializeCustom(
        account, 
        profile, 
        environment, 
        iosDatasource,
        androidDatasource, 
        instanceName, 
        isLifecycleEnabled,
        overridePublishSettingsURL,
        overrideTagManagementURL,
        collectURL,
        enableConsentManager
    ) {
        TealiumModule.initializeCustom(
            account, 
            profile, 
            environment, 
            iosDatasource, 
            androidDatasource,
            instanceName, 
            isLifecycleEnabled,
            overridePublishSettingsURL,
            overrideTagManagementURL,
            collectURL,
            enableConsentManager
        );
    }

    static trackEvent(stringTitle, data) {
        TealiumModule.trackEvent(stringTitle, data);
    }

    static trackView(stringTitle, data) {
        TealiumModule.trackView(stringTitle, data);
    }
    
    static trackViewForInstanceName(name, stringTitle, data) {
        TealiumModule.trackViewForInstance(name, stringTitle, data);
    }

    static trackEventForInstanceName(name, stringTitle, data) {
        TealiumModule.trackEventForInstance(name, stringTitle, data);
    }

    static setVolatileData(data) {
        TealiumModule.setVolatileData(data);
    }

    static getVolatileData(key, value) {
        TealiumModule.getVolatileData(key, value);
    }

    static getVolatileDataForInstanceName(name, key, data) {
        TealiumModule.getVolatileDataForInstance(name, key, data);
    }

    static setVolatileDataForInstanceName(name, data) {
        TealiumModule.setVolatileDataForInstance(name, data);
    }

    static setPersistentData(data) {
        TealiumModule.setPersistentData(data);
    }

    static setPersistentDataForInstanceName(name, data) {
        TealiumModule.setPersistentDataForInstance(name, data);
    }

    static getPersistentData(data) {
        TealiumModule.getPersistentData(data);
    }

    static getPersistentDataForInstanceName(name, key, data) {
        TealiumModule.getPersistentDataForInstance(name, key, data);
    }

    static removeVolatileData(keys) {
        TealiumModule.removeVolatileData(keys);
    }

    static removeVolatileDataForInstanceName(name, keys) {
        TealiumModule.removeVolatileDataForInstance(name, keys);
    }

    static removePersistentData(keys) {
        TealiumModule.removePersistentData(keys);
    }

    static removePersistentDataForInstanceName(name, keys) {
        TealiumModule.removePersistentDataForInstance(name, keys);
    }

    static getVisitorID(visitorID) {
        TealiumModule.getVisitorID(visitorID);
    }

    static getVisitorIDForInstanceName(name, visitorID) {
        TealiumModule.getVisitorIDForInstance(name, visitorID);
    }

    static getUserConsentStatus(userConsentStatus) {
        TealiumModule.getUserConsentStatus(userConsentStatus);
    }

    static getUserConsentStatusForInstanceName(name, userConsentStatus) {
        TealiumModule.getUserConsentStatusForInstance(name, userConsentStatus);
    }

    static setUserConsentStatus(userConsentStatus) {
        TealiumModule.setUserConsentStatus(userConsentStatus);
    }

    static setUserConsentStatusForInstanceName(name, userConsentStatus) {
        TealiumModule.setUserConsentStatusForInstance(name, userConsentStatus);
    }

    static getUserConsentCategories(userConsentCategories) {
        TealiumModule.getUserConsentCategories(userConsentCategories);
    }

    static getUserConsentCategoriesForInstanceName(name, userConsentCategories) {
        TealiumModule.getUserConsentCategoriesForInstance(name, userConsentCategories);
    }

    static setUserConsentCategories(userConsentCategories) {
        TealiumModule.setUserConsentCategories(userConsentCategories);
    }

    static setUserConsentCategoriesForInstanceName(name, userConsentCategories) {
        TealiumModule.setUserConsentCategoriesForInstance(name, userConsentCategories);
    } 

    static resetUserConsentPreferences() {
        TealiumModule.resetUserConsentPreferences();
    }

    static resetUserConsentPreferencesForInstanceName(name) {
        TealiumModule.resetUserConsentPreferencesForInstance(name);
    }

    // Note: Waiting for next Android release
    // static allCategories(userConsentCategories) {
    //     TealiumModule.allCategories(userConsentCategories);
    // }

    static setConsentLoggingEnabled(enabled) {
        TealiumModule.setConsentLoggingEnabled(enabled);
    }

    static setConsentLoggingEnabledForInstanceName(name, enabled) {
        TealiumModule.setConsentLoggingEnabledForInstance(name, enabled);
    }

    static isConsentLoggingEnabled(enabled) {
        TealiumModule.isConsentLoggingEnabled(enabled);
    }

    static isConsentLoggingEnabledForInstanceName(name, enabled) {
        TealiumModule.isConsentLoggingEnabledForInstance(name, enabled);
    }
}
