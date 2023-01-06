import { NativeModules } from 'react-native';

const { TealiumReactAdobeVisitor } = NativeModules;

const ios = "ios"
const android = "android"

export default class TealiumAdobeVisitor {

    static configure(config) {
        TealiumReactAdobeVisitor.configure(config)
    }

    static linkExistingEcidToKnownIdentifier() {
        TealiumReactAdobeVisitor.linkExistingEcidToKnownIdentifier()
    }

    static resetVisitor() {
        TealiumReactAdobeVisitor.resetVisitor()
    }

    static decorateUrl() {
        TealiumReactAdobeVisitor.decorateUrl()
    }
}