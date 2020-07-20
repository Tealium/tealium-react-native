#import <Foundation/Foundation.h>
#import "TealiumModule.h"
#import <React/RCTConvert.h>
#import <AdSupport/AdSupport.h>


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
                                      @"vdata" : @(TEALCollectURLVdata),
                                      @"collect" : @(TEALCollectURLEvent),
                                      }), TEALCollectURLEvent, integerValue)
@end

@implementation TealiumModule
RCT_EXPORT_MODULE();

NSString *tealiumCurrentInstanceName;
NSString *tealiumSingleInstanceName = @"MAIN";

// MARK: - Remote Command Emitter
NSString *remoteCommandEventName = @"RemoteCommandEvent";
- (NSArray<NSString *> *)supportedEvents {
    return @[remoteCommandEventName];
}

- (NSDictionary *) getIdentifiers {
    
    NSString* idfv = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    NSString* idfa = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    BOOL adTrackingEnabled = [[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled];
    NSString* adTracking = adTrackingEnabled == YES ? @"true" : @"false";
    
    NSDictionary* dict = @{
                           @"device_advertising_id": idfa,
                           @"device_advertising_vendor_id": idfv,
                           @"device_advertising_enabled": adTracking
                           };

    return dict;
}

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
    
    tealiumCurrentInstanceName = instance == nil ? tealiumSingleInstanceName : instance;
    
    // Initialize with a unique key for this instance
    [Tealium newInstanceForKey:tealiumCurrentInstanceName configuration:configuration];
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
    
    tealiumCurrentInstanceName = instance == nil ? tealiumSingleInstanceName : instance;
    
    // Initialize with a unique key for this instance
    [Tealium newInstanceForKey:tealiumCurrentInstanceName configuration:configuration];
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
                  overrideCollectDispatchURL:(NSString *)overrideCollectDispatchURL
                  enableAdIdentifierCollection:(BOOL)enableAdIdentifierCollection
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
    if (overrideCollectDispatchURL) {
        configuration.overrideCollectDispatchURL = overrideCollectDispatchURL;
    }
    if (!enableCollectURL) {
        configuration.collectURL = TEALCollectURLVdata;
    }
    configuration.enableConsentManager = enableConsentManager;
    
    tealiumCurrentInstanceName = instance == nil ? tealiumSingleInstanceName : instance;
    
    [Tealium newInstanceForKey:tealiumCurrentInstanceName configuration:configuration];
    if (enableAdIdentifierCollection) {
        [[Tealium instanceForKey:tealiumCurrentInstanceName] addPersistentDataSources: [self getIdentifiers]];
    }
}

// MARK: - Tracking
RCT_EXPORT_METHOD(trackEvent:(NSString *)eventName data:(NSDictionary *)data) {
    [self trackEventForInstance:tealiumCurrentInstanceName event:eventName data:data];
}

RCT_EXPORT_METHOD(trackEventForInstance:(NSString *)instanceName event:(NSString *)eventName data:(NSDictionary *)data) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to call trackEvent, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [tealium trackEventWithTitle:eventName dataSources:data];
}

RCT_EXPORT_METHOD(trackView:(NSString *)viewName data:(NSDictionary *)data) {
    [self trackViewForInstance:tealiumCurrentInstanceName view:viewName data:data];
}

RCT_EXPORT_METHOD(trackViewForInstance:(NSString *)instanceName view:(NSString *)viewName data:(NSDictionary *)data) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to call trackView, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [tealium trackViewWithTitle:viewName dataSources:data];
}

RCT_EXPORT_METHOD(setVolatileData:(NSDictionary *)data) {
    [self setVolatileDataForInstance:tealiumCurrentInstanceName data:data];
}

RCT_EXPORT_METHOD(setVolatileDataForInstance:(NSString *)instanceName data:(NSDictionary *)data) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to set volatile data, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [tealium addVolatileDataSources:data];
}

RCT_EXPORT_METHOD(setPersistentData:(NSDictionary *)data) {
    [self setPersistentDataForInstance:tealiumCurrentInstanceName data:data];
}

RCT_EXPORT_METHOD(setPersistentDataForInstance:(NSString *)instanceName data:(NSDictionary *)data) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to set persistent data, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [tealium addPersistentDataSources:data];
}

RCT_EXPORT_METHOD(removeVolatileData:(NSArray<NSString *> *)keys) {
    [self removeVolatileDataForInstance:tealiumCurrentInstanceName keys:keys];
}

RCT_EXPORT_METHOD(removeVolatileDataForInstance:(NSString *)instanceName keys:(NSArray<NSString *> *)keys) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to remove volatile data, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [tealium removeVolatileDataSourcesForKeys:keys];
}

RCT_EXPORT_METHOD(removePersistentData:(NSArray<NSString *> *)keys) {
    [self removePersistentDataForInstance:tealiumCurrentInstanceName keys:keys];
}

RCT_EXPORT_METHOD(removePersistentDataForInstance:(NSString *)instanceName keys:(NSArray<NSString *> *)keys) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to remove persistent data, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [tealium removePersistentDataSourcesForKeys:keys];
}

RCT_EXPORT_METHOD(getVolatileData:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    [self getVolatileDataForInstance:tealiumCurrentInstanceName key:key callback:callback];
}

RCT_EXPORT_METHOD(getVolatileDataForInstance:(NSString *)instanceName key:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to get volatile data, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    id value = [tealium volatileDataSourcesCopy][key];
    if (!value) {
        RCTLogInfo(@"Value for key: %@ is nil.", key);
    } else {
        callback(@[value]);
    }
}

RCT_EXPORT_METHOD(getPersistentData:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    [self getPersistentDataForInstance:tealiumCurrentInstanceName key:key callback:callback];
}

RCT_EXPORT_METHOD(getPersistentDataForInstance:(NSString *)instanceName key:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to get persistent data, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    id value = [tealium persistentDataSourcesCopy][key];
    if (!value) {
        RCTLogInfo(@"Value for key: %@ is nil.", key);
    } else {
        callback(@[value]);
    }
}

// MARK: Visitor
RCT_EXPORT_METHOD(getVisitorID:(RCTResponseSenderBlock)callback) {
    [self getVisitorIDForInstance:tealiumCurrentInstanceName callback:callback];
}

RCT_EXPORT_METHOD(getVisitorIDForInstance:(NSString *)instanceName callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to get the Visitor ID, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    callback(@[[tealium visitorIDCopy]]);
}

// MARK: Consent Manager
RCT_EXPORT_METHOD(getUserConsentStatus:(RCTResponseSenderBlock)callback) {
    [self getUserConsentStatusForInstance:tealiumCurrentInstanceName callback:callback];
}

RCT_EXPORT_METHOD(getUserConsentStatusForInstance:(NSString *)instanceName callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to get user consent status, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [TEALConsentManager consentStatusString:[[tealium consentManager] userConsentStatus]];
    callback(@[[TEALConsentManager consentStatusString:[[tealium consentManager] userConsentStatus]]]);
}

RCT_EXPORT_METHOD(setUserConsentStatus:(TEALConsentStatus)userConsentStatus) {
    [self setUserConsentStatusForInstance:tealiumCurrentInstanceName userConsentStatus:userConsentStatus];
}

RCT_EXPORT_METHOD(setUserConsentStatusForInstance:(NSString *)instanceName userConsentStatus:(TEALConsentStatus)userConsentStatus) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to set user consent status, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [[tealium consentManager] setUserConsentStatus:userConsentStatus];
}

RCT_EXPORT_METHOD(getUserConsentCategories:(RCTResponseSenderBlock)callback) {
    [self getUserConsentCategoriesForInstance:tealiumCurrentInstanceName callback:callback];
}

RCT_EXPORT_METHOD(getUserConsentCategoriesForInstance:(NSString *)instanceName callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to get user consent categories, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    NSArray *userConsentCategories = [[tealium consentManager] userConsentCategories];
    if ([userConsentCategories count] > 0) {
        callback(@[userConsentCategories]);
    } else {
        RCTLogInfo(@"No consent categories.");
    }
}

RCT_EXPORT_METHOD(setUserConsentCategories:(NSArray *)categories) {
    [self setUserConsentCategoriesForInstance:tealiumCurrentInstanceName categories:categories];
}

RCT_EXPORT_METHOD(setUserConsentCategoriesForInstance:(NSString *)instanceName categories:(NSArray *)categories) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to set user consent categories, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [[tealium consentManager] setUserConsentCategories:categories];
}

RCT_EXPORT_METHOD(resetUserConsentPreferences) {
    [self resetUserConsentPreferencesForInstance:tealiumCurrentInstanceName];
}

RCT_EXPORT_METHOD(resetUserConsentPreferencesForInstance:(NSString *)instanceName) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to reset user consent preferences, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [[tealium consentManager] resetUserConsentPreferences];
}

// Note: Waiting for next Android release to have this method public
//RCT_EXPORT_METHOD(allCategories:(RCTResponseSenderBlock)callback) {
//    Tealium *tealium = [Tealium instanceForKey:tealiumCurrentInstanceName];
//    callback(@[[[tealium consentManager] allCategories]]);
//}

RCT_EXPORT_METHOD(setConsentLoggingEnabled:(BOOL)enable) {
    [self setConsentLoggingEnabledForInstance:tealiumCurrentInstanceName enable:enable];
}

RCT_EXPORT_METHOD(setConsentLoggingEnabledForInstance:(NSString *)instanceName enable:(BOOL)enable) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to toggle consent loggine, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [[tealium consentManager] setConsentLoggingEnabled:enable];
}

RCT_EXPORT_METHOD(isConsentLoggingEnabled:(RCTResponseSenderBlock)callback) {
    [self isConsentLoggingEnabledForInstanceName:tealiumCurrentInstanceName callback:callback];
}

RCT_EXPORT_METHOD(isConsentLoggingEnabledForInstanceName:(NSString *)instanceName callback:(RCTResponseSenderBlock)callback) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to check if consent logging is enabled, \
                           but Tealium not enabled for instance name: %@", instanceName);
    }
    [[tealium consentManager] isConsentLoggingEnabled];
}

RCT_EXPORT_METHOD(addRemoteCommand:(NSString *_Nonnull)commandID
                  description:(NSString *)description) {
    [self addRemoteCommandForInstanceName:tealiumCurrentInstanceName commandID:commandID description:description];
}



RCT_EXPORT_METHOD(addRemoteCommandForInstanceName:(NSString *)instanceName
                  commandID:(NSString *)commandID
                  description:(NSString *)description) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to add remote command %@, \
                           but Tealium not enabled for instance name: %@", commandID, instanceName);
    }
    
    [tealium addRemoteCommandID:commandID description:description targetQueue:dispatch_get_main_queue() responseBlock:^(TEALRemoteCommandResponse *_Nullable response) {
        
        if ([[response requestPayload] count] > 0) {
            [self sendEventWithName: remoteCommandEventName body:[response requestPayload]];
        } else {
            RCTLogInfo(@"No response.");
        }
    }];
}

RCT_EXPORT_METHOD(removeRemoteCommand:(NSString *)commandID) {
    [self removeRemoteCommandForInstanceName:tealiumCurrentInstanceName commandID:commandID];
}

RCT_EXPORT_METHOD(removeRemoteCommandForInstanceName:(NSString *)instanceName
                  commandID:(NSString *)commandID) {
    Tealium *tealium = [Tealium instanceForKey:instanceName];
    if (tealium == nil) {
        return RCTLogError(@"Attempted to remove remote command %@, \
                           but Tealium not enabled for instance name: %@", commandID, instanceName);
    }
    [tealium removeRemoteCommandID:commandID];
}

@end
