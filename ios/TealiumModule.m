#import <Foundation/Foundation.h>
#import "TealiumModule.h"
#import <React/RCTConvert.h>

@import TealiumIOSLifecycle;

@implementation TealiumModule
RCT_EXPORT_MODULE();

NSString *tealiumInternalInstanceName;

RCT_EXPORT_METHOD(genMsg:(NSString *)name callback:(RCTResponseSenderBlock)callback)
{
    callback(@[[NSString stringWithFormat: @"Hello from Tealium's native module, %@!", tealiumInternalInstanceName]]);
}

RCT_EXPORT_METHOD(initialize:(NSString *)account
                  profile:(NSString *)profile
                  environment:(NSString *)environment
                  iosDatasource:(NSString *)iosDatasource
                  androidDatasource:(NSString *)androidDatasource
                  instance:(NSString *)instance
                  isLifeCycleEnabled:(BOOL)isLifeCycleEnabled)
{
    // Set your account, profile, and environment
    TEALConfiguration *tealConfig = [TEALConfiguration configurationWithAccount:account
                                                                        profile:profile
                                                                    environment:environment
                                                                     datasource:iosDatasource];
    [tealConfig setAutotrackingLifecycleEnabled:isLifeCycleEnabled];
    
    tealiumInternalInstanceName = instance;
    
    // Initialize with a unique key for this instance
    [Tealium newInstanceForKey:tealiumInternalInstanceName configuration:tealConfig];
}

RCT_EXPORT_METHOD(trackEvent:(NSString *)eventName data:(NSDictionary *)data)
{
    if(tealiumInternalInstanceName != nil)
    {
        Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
        [tealium trackEventWithTitle:eventName dataSources:data];
    }
}

RCT_EXPORT_METHOD(trackView:(NSString *)viewName data:(NSDictionary *)data)
{
    if(tealiumInternalInstanceName != nil)
    {
        Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
        [tealium trackViewWithTitle:viewName dataSources:data];
    }
}

RCT_EXPORT_METHOD(setVolatileData:(NSDictionary *)data)
{
    if(tealiumInternalInstanceName != nil)
    {
        Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
        [tealium addVolatileDataSources:data];
    }
}

RCT_EXPORT_METHOD(setPersistentData:(NSDictionary *)data)
{
    if(tealiumInternalInstanceName != nil)
    {
        Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
        [tealium addPersistentDataSources:data];
    }
}

RCT_EXPORT_METHOD(removeVolatileData:(NSArray<NSString *> *)keys)
{
    if(tealiumInternalInstanceName != nil)
    {
        Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
        [tealium removeVolatileDataSourcesForKeys:keys];
    }
}

RCT_EXPORT_METHOD(removePersistentData:(NSArray<NSString *> *)keys)
{
    if(tealiumInternalInstanceName != nil)
    {
        Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
        [tealium removePersistentDataSourcesForKeys:keys];
    }
}

RCT_EXPORT_METHOD(getVolatileData:(NSString *)key callback:(RCTResponseSenderBlock)callback)
{
    if(tealiumInternalInstanceName != nil)
    {
        Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
        callback(@[[tealium volatileDataSourcesCopy][key]]);
    }
}

RCT_EXPORT_METHOD(getPersistentData:(NSString *)key callback:(RCTResponseSenderBlock)callback)
{
    if(tealiumInternalInstanceName != nil)
    {
        Tealium *tealium = [Tealium instanceForKey:tealiumInternalInstanceName];
        callback(@[[tealium persistentDataSourcesCopy][key]]);
    }
}

@end
