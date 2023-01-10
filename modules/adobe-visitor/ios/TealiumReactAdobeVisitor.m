#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(TealiumReactAdobeVisitor, NSObject)
    RCT_EXTERN_METHOD(configure:(NSDictionary *)config)
    RCT_EXTERN_METHOD(linkExistingEcidToKnownIdentifier:(NSString)knownId adobeDataProviderId: (NSString))
    RCT_EXTERN_METHOD(resetVisitor)
    RCT_EXTERN_METHOD(decorateUrl: (NSString)url completion: RCTResponseSenderBlock)
@end
