#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(TealiumReactAdobeVisitor, NSObject)
    RCT_EXTERN_METHOD(configure:(NSDictionary *)config)
    RCT_EXTERN_METHOD(getCurrentAdobeVisitor:(RCTResponseSenderBlock)callback)
    RCT_EXTERN_METHOD(linkEcidToKnownIdentifier:(NSString)knownId adobeDataProviderId: (NSString)adobeDataProviderId authState: (nonnull NSNumber*)authState callback: (RCTResponseSenderBlock*)callback)
    RCT_EXTERN_METHOD(resetVisitor)
    RCT_EXTERN_METHOD(decorateUrl:(NSString)url callback: (RCTResponseSenderBlock))
@end
