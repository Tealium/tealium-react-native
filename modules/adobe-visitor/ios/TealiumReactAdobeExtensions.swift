import Foundation
import TealiumAdobeVisitorAPI


extension AdobeVisitor {
    func asDictionary() -> [String: Any]? {
        guard let idSyncTTL = self.idSyncTTL, let idSyncTTL = Int(idSyncTTL),
              let dcsRegion = self.dcsRegion, let dcsRegion = Int(dcsRegion),
              let blob = self.blob,
              let nextRefresh = self.nextRefresh, let nextRefresh = Int(nextRefresh.unixTimeMilliseconds)
        else {
            return nil
        }
        return [
            "experienceCloudId": self.experienceCloudID,
            "idSyncTtl": idSyncTTL,
            "region": dcsRegion,
            "blob": blob,
            "nextRefresh": nextRefresh,
        ]
    }
}