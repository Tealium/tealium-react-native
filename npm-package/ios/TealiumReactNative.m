
#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"

@interface RCT_EXTERN_MODULE(TealiumWrapper, NSObject)
  
    RCT_EXTERN_METHOD(initialize:(NSDictionary *)config callback:(RCTResponseSenderBlock)callback)

    RCT_EXTERN_METHOD(track:(NSDictionary *)dispatch)

    RCT_EXTERN_METHOD(terminateInstance)

    RCT_EXTERN_METHOD(getConsentStatus:(RCTResponseSenderBlock)callback)

    RCT_EXTERN_METHOD(setConsentStatus:(NSString *)status)

    RCT_EXTERN_METHOD(getConsentCategories:(RCTResponseSenderBlock)callback)

    RCT_EXTERN_METHOD(setConsentCategories:(NSArray *)status)

    RCT_EXTERN_METHOD(addToDataLayer:(NSDictionary *)data expiry:(NSString *)expiry)

    RCT_EXTERN_METHOD(getFromDataLayer:(NSString *)key callback:(RCTResponseSenderBlock)callback)

    RCT_EXTERN_METHOD(removeFromDataLayer:(NSArray *)keys)

    RCT_EXTERN_METHOD(deleteFromDataLayer:(NSArray *)keys)

    RCT_EXTERN_METHOD(addRemoteCommand:(NSString *)commandId)

    RCT_EXTERN_METHOD(removeRemoteCommand:(NSString *)commandId)

    RCT_EXTERN_METHOD(joinTrace:(NSString *)traceId)

    RCT_EXTERN_METHOD(leaveTrace)

    RCT_EXTERN_METHOD(getVisitorId:(RCTResponseSenderBlock)callback)

    RCT_EXTERN_METHOD(getSessionId:(RCTResponseSenderBlock)callback)

@end

@interface RCT_EXTERN_MODULE(TealiumReactNative, RCTEventEmitter)

    RCT_EXTERN_METHOD(supportedEvents)

@end
