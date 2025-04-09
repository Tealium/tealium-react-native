import Foundation
import tealium_react_native
import TealiumSwift

@objc(TealiumReactMomentsApi)
class TealiumReactMomentsApi: NSObject, RCTBridgeModule {
    
    static func moduleName() -> String! {
        return "TealiumReactMomentsApi"
    }
    
    private let KEY_MOMENTS_API_REGION = "momentsApiRegion"
    private let KEY_MOMENTS_API_REFERRER = "momentsApiReferrer"
    
    let module = TealiumReactMomentsAPIModule()
    
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
        if let region = config[KEY_MOMENTS_API_REGION] as? String {
            module.setMomentsRegion(region: region)
        }
        
        if let referrer = config[KEY_MOMENTS_API_REFERRER] as? String {
            module.setMomentsReferrer(referrer: referrer)
        }
    }
    
    @objc(fetchEngineResponse:callback:)
    func fetchEngineResponse(engineId: String, callback: @escaping RCTResponseSenderBlock) {
        module.fetchEngineResponse(engineId: engineId, callback: callback)
    }
    
}

@objc class TealiumReactMomentsAPIModule: NSObject, OptionalModule {
    private var momentsRegion: String? = nil
    private var momentsReferrer: String? = nil
    
    func configure(config: TealiumConfig) {
        config.collectors?.append(Collectors.MomentsAPI)
        
        if let region = momentsRegion {
            config.momentsAPIRegion = .regionFrom(region: region)
        }
        
        if let referrer = momentsReferrer {
            config.momentsAPIReferrer = referrer
        }
    }
    
    func fetchEngineResponse(engineId: String, callback: @escaping RCTResponseSenderBlock) {
        guard let instance = TealiumReactNative.instance, let momentsAPI = instance.momentsAPI else {
            callback(["Unable to retrieve MomentsAPI module. Please check your configuration."])
            return
        }
        
        momentsAPI.fetchEngineResponse(engineID: engineId) { engineResponse in
            switch engineResponse {
                case .success(let response):
                    callback([response.asDictionary()])
                case .failure(let error):
                    callback(["Failed to fetch engine response with error code: \(error.localizedDescription)"])
            }
        }
    }
    
    func setMomentsRegion(region: String) {
        momentsRegion = region
    }
    
    func setMomentsReferrer(referrer: String) {
        momentsReferrer = referrer
    }
}
