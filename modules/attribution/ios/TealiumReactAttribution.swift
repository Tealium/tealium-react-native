import Foundation
import tealium_react_native_swift
import TealiumSwift

@objc(TealiumReactAttribution)
class TealiumReactAttribution: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        return "TealiumReactAttribution"
    }
    
    private let KEY_ATTRIBUTION_SEARCH_ADS_ENABLED = "iosSearchAdsEnabled"
    private let KEY_ATTRIBUTION_SKAD_ATTRIBUTION_ENABLED = "iosSkAdAttributionEnabled"
    private let KEY_ATTRIBUTION_SKAD_CONVERSION_KEYS = "iosSkAdConversionKeys"
    let module = TealiumReactAttributionModule()
    
    @objc
    static func requiresMainQueueSetup() -> Bool {
      return false
    }
    
    override init() {
        super.init()
        TealiumReactNative.registerOptionalModule(module)
    }
    
    @objc(configure:)
    public func configure(_ config: [String: Any]) {
        if let searchAds = config[KEY_ATTRIBUTION_SEARCH_ADS_ENABLED] as? Bool {
            module.setSearchAdsEnabled(enabled: searchAds)
        }

        if let skAdAttribution = config[KEY_ATTRIBUTION_SKAD_ATTRIBUTION_ENABLED] as? Bool {
            module.setSkAdAttributionEnabled(enabled: skAdAttribution)
        }

        if let skAdKeys = config[KEY_ATTRIBUTION_SKAD_CONVERSION_KEYS] as? [String: String] {
            module.setSkAdConversionKeys(keys: skAdKeys)
        }
    }
}

@objc class TealiumReactAttributionModule: NSObject, OptionalModule {
    private var searchAdsEnabled: Bool? = nil
    private var skAdAttributionEnabled: Bool? = nil
    private var skAdConversionKeys: [String: String]? = nil
    
    func configure(config: TealiumConfig) {
        config.collectors?.append(Collectors.Attribution)
        
        if let searchAds = searchAdsEnabled {
            config.searchAdsEnabled = searchAds
        }
        
        if let skAdAttrubution = skAdAttributionEnabled {
            config.skAdAttributionEnabled = skAdAttrubution
        }
        
        if let conversionKeys = skAdConversionKeys {
            config.skAdConversionKeys = conversionKeys
        }
    }

    func setSearchAdsEnabled(enabled: Bool) {
        searchAdsEnabled = enabled
    }

    func setSkAdAttributionEnabled(enabled: Bool) {
        skAdAttributionEnabled = enabled
    }

    func setSkAdConversionKeys(keys: [String: String]) {
        skAdConversionKeys = keys
    }
}
