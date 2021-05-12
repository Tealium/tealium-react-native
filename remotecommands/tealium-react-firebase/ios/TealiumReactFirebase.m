//#import "TealiumReactFirebase.h"
#import "React/RCTBridgeModule.h"
//
//@implementation TealiumReactFirebase
//@synthesize bridge = _bridge;
//RCT_EXPORT_MODULE()
//
////RCT_EXPORT_METHOD(sampleMethod:(NSString *)stringArgument numberParameter:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)
////{
////    // TODO: Implement some actually useful functionality
////    callback(@[[NSString stringWithFormat: @"numberArgument: %@ stringArgument: %@", numberArgument, stringArgument]]);
////}
////
//@end

 @interface RCT_EXTERN_MODULE(TealiumReactFirebase, NSObject)

     RCT_EXTERN_METHOD(initialize)

     RCT_EXTERN_METHOD(configure:(NSDictionary *)options)

 @end
