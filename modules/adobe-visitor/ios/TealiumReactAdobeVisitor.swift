import Foundation
import tealium_react_native
import TealiumAdobeVisitorAPI
import TealiumSwift

@objc(TealiumReactAdobeVisitor)
class TealiumReactAdobeVisitor: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        return "TealiumReactAdobeVisitor"
    }
    private let KEY_ADOBE_VISITOR_ORG_ID = "adobeVisitorOrgId"
    private let KEY_ADOBE_VISITOR_EXISTING_ECID = "adobeVisitorExistingEcid"
    private let KEY_ADOBE_VISITOR_RETRIES = "adobeVisitorRetries"
    private let KEY_ADOBE_VISITOR_AUTH_STATE = "adobeVisitorAuthState"
    private let KEY_ADOBE_VISITOR_DATA_PROVIDER_ID = "adobeVisitorDataProviderId"
    private let KEY_ADOBE_VISITOR_CUSTOM_VISITOR_ID = "adobeVisitorCustomVisitorId"
    let module = TealiumReactAdobeVisitorModule()

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
        if let adobeVisitorOrgId = config[KEY_ADOBE_VISITOR_ORG_ID] as? String {
            module.setOrgId(orgId: adobeVisitorOrgId)
        }
        if let adobeExistingEcid = config[KEY_ADOBE_VISITOR_EXISTING_ECID] as? String {
            module.setExistingEcid(ecid: adobeExistingEcid)
        }
        if let adobeVisitorRetries = config[KEY_ADOBE_VISITOR_RETRIES] as? Int {
            module.setRetries(retries: adobeVisitorRetries)
        }
        if let adobeVisitorAuthState = config[KEY_ADOBE_VISITOR_AUTH_STATE] as? Int {
            module.setAuthState(state: adobeVisitorAuthState)
        }
        if let adobeVisitorDataProviderId = config[KEY_ADOBE_VISITOR_DATA_PROVIDER_ID] as? String {
            module.setDataProviderId(dataProviderId: adobeVisitorDataProviderId)
        }
        if let adobeVisitorCustomVisitorId = config[KEY_ADOBE_VISITOR_CUSTOM_VISITOR_ID] as? String {
            module.setCustomVisitorId(customId: adobeVisitorCustomVisitorId)
        }
    }
    
    @objc(linkEcidToKnownIdentifier:adobeDataProviderId:authState:callback:)
    public func linkEcidToKnownIdentifier(knownId: String, adobeDataProviderId: String, authState: NSNumber? = nil, callback: RCTResponseSenderBlock? = nil) {
        let intAuthState = authState?.intValue
        module.linkEcidToKnownIdentifier(knownId: knownId, adobeDataProviderId: adobeDataProviderId, authState: intAuthState, callback: callback)
    }
    
    @objc(resetVisitor)
    public func resetVisitor() {
        module.resetVisitor()
    }
    
    @objc(decorateUrl:callback:)
    public func decorateUrl(url: String, callback: @escaping RCTResponseSenderBlock) {
        if let url = URL(string: url) {
            module.decorateUrl(url: url, completion: callback)
        }
    }

    @objc(getUrlParameters:)
    public func getUrlParameters(callback: @escaping RCTResponseSenderBlock) {
        module.getUrlParameters(completion: callback)
    }
    
    @objc(getCurrentAdobeVisitor:)
    public func getCurrentAdobeVisitor(callback: RCTResponseSenderBlock) {
        module.getCurrentAdobeVisitor(callback: callback)
    }
}

@objc class TealiumReactAdobeVisitorModule: NSObject, OptionalModule {
    
    private var adobeOrgId: String? = nil
    private var adobeExistingECID: String? = nil
    private var adobeRetries: Int? = nil
    private var adobeAuthState: Int? = nil
    private var adobeDataProviderId: String? = nil
    private var adobeCustomVisitorId: String? = nil
    
    func configure(config: TealiumConfig) {
        config.collectors?.append(Collectors.AdobeVisitor)
        if let adobeOrgId = adobeOrgId {
            config.adobeVisitorOrgId = adobeOrgId
        }
        if let adobeExistingECID = adobeExistingECID {
            config.adobeVisitorExistingEcid = adobeExistingECID
        }
        if let adobeRetries = adobeRetries {
            config.adobeVisitorRetries = adobeRetries
        }
        if let adobeAuthState = adobeAuthState {
            config.adobeVisitorAuthState = AdobeVisitorAuthState(rawValue: adobeAuthState)
        }
        if let adobeDataProviderId = adobeDataProviderId {
            config.adobeVisitorDataProviderId = adobeDataProviderId
        }
        if let adobeCustomVisitorId = adobeCustomVisitorId {
            config.adobeVisitorCustomVisitorId = adobeCustomVisitorId
        }
    }
    
    @objc
    public func getCurrentAdobeVisitor(callback: RCTResponseSenderBlock) {
        guard let adobeVisitorData = TealiumReactNative.instance?.adobeVisitorApi?.visitor else {
            callback(nil)
            return
        }
        callback([adobeVisitorData.asDictionary()])
    }
    
    public func linkEcidToKnownIdentifier(knownId: String, adobeDataProviderId: String, authState: Int? = nil, callback: RCTResponseSenderBlock? = nil) {
        var newAuthState: AdobeVisitorAuthState? = nil
        if let authState = authState {
            newAuthState = AdobeVisitorAuthState(rawValue: authState)
        }

        TealiumReactNative.instance?.adobeVisitorApi?.linkECIDToKnownIdentifier(knownId, adobeDataProviderId: adobeDataProviderId, authState: newAuthState, completion: { visitorResult in
            if let callback = callback {
                switch visitorResult {
                case .success(let visitor):
                    callback([visitor.asDictionary()])
                case .failure(let error):
                    callback(["Failed to link existing Ecid with error code: $errorCode and exception \(error.localizedDescription)"])
                }
            }
        })
    }
    
    public func resetVisitor() {
        TealiumReactNative.instance?.adobeVisitorApi?.resetVisitor()
    }
    
    
    public func decorateUrl(url: URL, completion:  @escaping RCTResponseSenderBlock) {
        TealiumReactNative.instance?.adobeVisitorApi?.decorateUrl(url, completion: { url in
            completion([url.absoluteString])
        })
    }

    public func getUrlParameters(completion:  @escaping RCTResponseSenderBlock) {
        TealiumReactNative.instance?.adobeVisitorApi?.getURLParameters { params in
            guard let params = params else {
                completion(nil)
                return
            }
            completion([[params.name:  params.value]])
        }
    }

    func setOrgId(orgId: String) {
        adobeOrgId = orgId
    }

    func setExistingEcid(ecid: String) {
        adobeExistingECID = ecid
    }

    func setRetries(retries: Int) {
        adobeRetries = retries
    }

    func setAuthState(state: Int) {
        adobeAuthState = state
    }

    func setDataProviderId(dataProviderId: String) {
        adobeDataProviderId = dataProviderId
    }

    func setCustomVisitorId(customId: String) {
        adobeCustomVisitorId = customId
    }
}
