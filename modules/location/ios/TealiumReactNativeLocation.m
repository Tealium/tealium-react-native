#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(TealiumReactLocation, NSObject)
  
    RCT_EXTERN_METHOD(configure:(NSDictionary *)config)
    RCT_EXTERN_METHOD(setAccuracyBoolean:(Bool *)isHighAccuracy)
    RCT_EXTERN_METHOD(setAccuracyString:(String *)isHighAccuracy)
    RCT_EXTERN_METHOD(setGeofenceUrl:(String *)url)
    RCT_EXTERN_METHOD(setGeofenceFile:(String *)path)
    RCT_EXTERN_METHOD(setGeofenceTrackingEnabled:(Bool *)enabled)
    RCT_EXTERN_METHOD(setUpdateDistance:(Double *)distance)
    RCT_EXTERN_METHOD(setDesiredAccuracy:(String *)accuracy)

    RCT_EXTERN_METHOD(lastLocation:(RCTResponseSenderBlock)callback)
    RCT_EXTERN_METHOD(startLocationTracking)
    RCT_EXTERN_METHOD(stopLocationTracking)
@end
