#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(TealiumReactMomentsApi, NSObject)

RCT_EXTERN_METHOD(configure:(NSDictionary *)config)

RCT_EXTERN_METHOD(fetchEngineResponse:(NSString)engineId callback: (RCTResponseSenderBlock*)callback)

@end
