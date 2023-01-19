import { NativeModules } from 'react-native';
const { TealiumReactAdobeVisitor } = NativeModules;

const ios = "ios"
const android = "android"

export default class TealiumAdobeVisitor {

    static configure(config) {
        TealiumReactAdobeVisitor.configure(config)
    }

    static linkExistingEcidToKnownIdentifier(knownId, adobeDataProviderId, authState, callback) {
        TealiumReactAdobeVisitor.linkEcidToKnownIdentifier(knownId, adobeDataProviderId, authState, callback)
    }

    static getAdobeVisitor(callback) {
        TealiumReactAdobeVisitor.getCurrentAdobeVisitor(callback)
    }

    static resetVisitor() {
        TealiumReactAdobeVisitor.resetVisitor()
    }

    static decorateUrl(url, callback) {
        TealiumReactAdobeVisitor.decorateUrl(url, callback)
    }
}