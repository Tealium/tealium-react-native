#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(TealiumReactCrashReporter, NSObject)
    RCT_EXTERN_METHOD(initialize)
    RCT_EXTERN_METHOD(configure:(NSDictionary *)options)
@end
