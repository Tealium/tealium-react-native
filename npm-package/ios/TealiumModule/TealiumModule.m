#import <Foundation/Foundation.h>
#import "TealiumModule.h"
#import <React/RCTConvert.h>

@import TealiumIOSLifecycle;

@implementation RCTConvert (ConsentStatus)
RCT_ENUM_CONVERTER(TEALConsentStatus, (@{
                                         @"consentStatusUnknown" : @(TEALConsentStatusUnknown),
                                         @"consentStatusConsented" : @(TEALConsentStatusConsented),
                                         @"consentStatusNotConsented" : @(TEALConsentStatusNotConsented),
                                         @"consentStatusDisabled" : @(TEALConsentStatusDisabled)
                                         }),
                   TEALConsentStatusUnknown, integerValue)
@end

@implementation RCTConvert (CollectURL)
RCT_ENUM_CONVERTER(TEALCollectURL, (@{
                                      @"vdata" : @(Vdata),
                                      @"collect" : @(Event),
                                      }), Event, integerValue)
@end

@implementation TealiumModule
RCT_EXPORT_MODULE();

NSString *tealiumInternalInstanceName;

// MARK: - Init
RCT_EXPORT_METHOD(initialize:(NSString *)account
                  profile:(NSString *)profile
                  environment:(NSString *)environment
                  iosDatasource:(NSString *)iosDatasource
                  androidDatasource:(NSString *)androidDatasource
                  instance:(NSString *)instance
                  isLifeCycleEnabled:(BOOL)isLifeCycleEnabled) {
    // Set your account, profile, and environment
    TEALConfiguration *configuration = [TEALConfiguration configurationWithAccount:account
                                                                           profile:profile
                                                                       environment:environment
                                                                        datasource:iosDatasource];
    [configuration setAutotrackingLifecycleEnabled:isLifeCycleEnabled];
    
    tealiumInternalInstanceName = instance;
    
    // Initialize with a unique key for this instance
    [Tealium newInstanceForKey:tealiumInternalInstanceName configuration:configuration];
}

RCT_EXPORT_METHOD(initializeWithConsentManager:(NSString *)account
                  profile:(NSString *)profile
                  environment:(NSString *)environment
                  iosDatasource:(NSString *)iosDatasource
                  androidDatasource:(NSString *)androidDatasource
                  instance:(NSString *)instance
                  isLifeCycleEnabled:(BOOL)isLifeCycleEnabled) {
    // Set your account, profile, and environment
    TEALConfiguration *configuration = [TEALConfiguration configurationWithAccount:account
                                                                           profile:profile
                                                                       environment:environment
                                                                        datasource:iosDatasource];
    [configuration setAutotrackingLifecycleEnabled:isLifeCycleEnabled];
    configuration.enableConsentManager = YES;
    tealiumInternalInstanceName = instance;
    
    // Initialize with a unique key for this instance
    [Tealium newInstanceForKey:tealiumInternalInstanceName configuration:configuration];
}

RCT_EXPORT_METHOD(initializeCustom:(NSString *)account
                  profile:(NSString *)profile
                  environment:(NSString *)environment
                  iosDatasource:(NSString *)iosDatasource
                  androidDatasource:(NSString *)androidDatasource
                  instance:(NSString *)instance
                  isLifeCycleEnabled:(BOOL)isLifeCycleEnabled
                  overridePublisthSettingsURL:(NSString *)overridePublisthSettingsURL
                  overrideTagManagementURL:(NSString *)overrideTagManagementURL
                  collectURL:(BOOL)enableCollectURL
                  enableConsentManager:(BOOL)enableConsentManager
                  ) {
    // Set your account, profile, and environment
    TEALConfiguration *configuration = [TEALConfiguration configurationWithAccount:account
                                                                           profile:profile
                                                                       environment:environment
                                                                        datasource:iosDatasource];
    [configuration setAutotrackingLifecycleEnabled:isLifeCycleEnabled];
    
    if (overridePublisthSettingsURL) {
        configuration.overridePublishSettingsURL = overridePublisthSettingsURL;
    }
    if (overrideTagManagementURL) {
        configuration.overrideTagManagementURL = overrideTagManagementURL;
    }
    if (!enableCollectURL) {
        configuration.collectURL = Vdata;
    }
    configuration.enableConsentManager = enableConsentManager;
    
    [Tealium newInstanceForKey:instance configuration:configuration];
}

// MARK: - Tracking
RCT_EXPORT_METHOD(trackEvent:(NSString *)eventName data:(NSDictionary *)data) {
    if (tealiumInternalInstanceName == nil) {
        return RCTLogError(@"Tealium is not initialized");
    }
    [self trackEventForInstance:tealiumInternalInstanceName event:eventName data:data];
}

RCT_EXPORT_METHOD(trackEventForInstance:(NSString *)instanceName event:(NSString *)eventName data:(NSDictionary *)data) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [tealium trackEventWithTitle:eventName dataSources:data];
}

RCT_EXPORT_METHOD(trackView:(NSString *)viewName data:(NSDictionary *)data) {
    if (tealiumInternalInstanceName == nil) {
        return RCTLogError(@"Tealium is not initialized");
    }
    [self trackViewForInstance:tealiumInternalInstanceName view:viewName data:data];
}

RCT_EXPORT_METHOD(trackViewForInstance:(NSString *)instanceName view:(NSString *)viewName data:(NSDictionary *)data) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [tealium trackViewWithTitle:viewName dataSources:data];
}

RCT_EXPORT_METHOD(setVolatileData:(NSDictionary *)data) {
    [self setVolatileDataForInstance:tealiumInternalInstanceName data:data];
}

RCT_EXPORT_METHOD(setVolatileDataForInstance:(NSString *)instanceName data:(NSDictionary *)data) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [tealium addVolatileDataSources:data];
}

RCT_EXPORT_METHOD(setPersistentData:(NSDictionary *)data) {
    [self setPersistentDataForInstance:tealiumInternalInstanceName data:data];
}

RCT_EXPORT_METHOD(setPersistentDataForInstance:(NSString *)instanceName data:(NSDictionary *)data) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [tealium addPersistentDataSources:data];
}

RCT_EXPORT_METHOD(removeVolatileData:(NSArray<NSString *> *)keys) {
    [self removeVolatileDataForInstance:tealiumInternalInstanceName keys:keys];
}

RCT_EXPORT_METHOD(removeVolatileDataForInstance:(NSString *)instanceName keys:(NSArray<NSString *> *)keys) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [tealium removeVolatileDataSourcesForKeys:keys];
}

RCT_EXPORT_METHOD(removePersistentData:(NSArray<NSString *> *)keys) {
    [self removePersistentDataForInstance:tealiumInternalInstanceName keys:keys];
}

RCT_EXPORT_METHOD(removePersistentDataForInstance:(NSString *)instanceName keys:(NSArray<NSString *> *)keys) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [tealium removePersistentDataSourcesForKeys:keys];
}

RCT_EXPORT_METHOD(getVolatileData:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    [self getVolatileDataForInstance:tealiumInternalInstanceName key:key callback:callback];
}

RCT_EXPORT_METHOD(getVolatileDataForInstance:(NSString *)instanceName key:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    id value = [tealium volatileDataSourcesCopy][key];
    if (!value) {
        RCTLogInfo(@"Value for key: %@ is nil.", key);
    } else {
        callback(@[value]);
    }
}

RCT_EXPORT_METHOD(getPersistentData:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    [self getPersistentDataForInstance:tealiumInternalInstanceName key:key callback:callback];
}

RCT_EXPORT_METHOD(getPersistentDataForInstance:(NSString *)instanceName key:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    id value = [tealium persistentDataSourcesCopy][key];
    if (!value) {
        RCTLogInfo(@"Value for key: %@ is nil.", key);
    } else {
        callback(@[value]);
    }
}

// MARK: Visitor
RCT_EXPORT_METHOD(getVisitorID:(RCTResponseSenderBlock)callback) {
    [self getVisitorIDForInstance:tealiumInternalInstanceName callback:callback];
}

RCT_EXPORT_METHOD(getVisitorIDForInstance:(NSString *)instanceName callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    callback(@[[tealium visitorIDCopy]]);
}

// MARK: Consent Manager
RCT_EXPORT_METHOD(getUserConsentStatus:(RCTResponseSenderBlock)callback) {
    [self getUserConsentStatusForInstance:tealiumInternalInstanceName callback:callback];
}

RCT_EXPORT_METHOD(getUserConsentStatusForInstance:(NSString *)instanceName callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [TEALConsentManager consentStatusString:[[tealium consentManager] userConsentStatus]];
    callback(@[[TEALConsentManager consentStatusString:[[tealium consentManager] userConsentStatus]]]);
}

RCT_EXPORT_METHOD(setUserConsentStatus:(TEALConsentStatus)userConsentStatus) {
    [self setUserConsentStatusForInstance:tealiumInternalInstanceName userConsentStatus:userConsentStatus];
}

RCT_EXPORT_METHOD(setUserConsentStatusForInstance:(NSString *)instanceName userConsentStatus:(TEALConsentStatus)userConsentStatus) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [[tealium consentManager] setUserConsentStatus:userConsentStatus];
}

RCT_EXPORT_METHOD(getUserConsentCategories:(RCTResponseSenderBlock)callback) {
    [self getUserConsentCategoriesForInstance:tealiumInternalInstanceName callback:callback];
}

RCT_EXPORT_METHOD(getUserConsentCategoriesForInstance:(NSString *)instanceName callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    NSArray *userConsentCategories = [[tealium consentManager] userConsentCategories];
    if ([userConsentCategories count] > 0) {
        callback(@[userConsentCategories]);
    } else {
        RCTLogInfo(@"No consent categories.");
    }
}

RCT_EXPORT_METHOD(setUserConsentCategories:(NSArray *)categories) {
    [self setUserConsentCategoriesForInstance:tealiumInternalInstanceName categories:categories];
}

RCT_EXPORT_METHOD(setUserConsentCategoriesForInstance:(NSString *)instanceName categories:(NSArray *)categories) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [[tealium consentManager] setUserConsentCategories:categories];
}

RCT_EXPORT_METHOD(resetUserConsentPreferences) {
    [self resetUserConsentPreferencesForInstance:tealiumInternalInstanceName];
}

RCT_EXPORT_METHOD(resetUserConsentPreferencesForInstance:(NSString *)instanceName) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [[tealium consentManager] resetUserConsentPreferences];
}

// Note: Waiting for next Android release to have this method public
//RCT_EXPORT_METHOD(allCategories:(RCTResponseSenderBlock)callback) {
//    Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
//    callback(@[[[tealium consentManager] allCategories]]);
//}

RCT_EXPORT_METHOD(setConsentLoggingEnabled:(BOOL)enable) {
    [self setConsentLoggingEnabledForInstance:tealiumInternalInstanceName enable:enable];
}

RCT_EXPORT_METHOD(setConsentLoggingEnabledForInstance:(NSString *)instanceName enable:(BOOL)enable) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [[tealium consentManager] setConsentLoggingEnabled:enable];
}

RCT_EXPORT_METHOD(isConsentLoggingEnabled:(RCTResponseSenderBlock)callback) {
    [self isConsentLoggingEnabledForInstance:tealiumInternalInstanceName callback:callback];
}

RCT_EXPORT_METHOD(isConsentLoggingEnabledForInstance:(NSString *)instanceName callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    [[tealium consentManager] isConsentLoggingEnabled];
}

@end
