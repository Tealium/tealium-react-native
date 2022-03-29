// TealiumReactBraze.m

#import "React/RCTBridgeModule.h"


@interface RCT_EXTERN_MODULE(TealiumReactBraze, NSObject)

    RCT_EXTERN_METHOD(initialize)

    RCT_EXTERN_METHOD(configure:(NSDictionary *)options)

@end

