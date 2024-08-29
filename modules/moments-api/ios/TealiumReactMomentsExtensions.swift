import Foundation
import TealiumSwift


extension EngineResponse {
    func asDictionary() -> [String: Any] {
        var dict = [String: Any]()
        if let attributes = self.strings {
            dict["attributes"] = attributes
        }
        if let booleans = self.booleans {
            dict["booleans"] = booleans
        }
        if let audiences = self.audiences {
            dict["audiences"] = audiences
        }
        if let dateAttributes = self.dates {
            dict["dates"] = dateAttributes
        }
        if let badges = self.badges {
            dict["badges"] = badges
        }
        if let numbers = self.numbers {
            dict["numbers"] = numbers
        }
        
        return dict
    }
}

extension MomentsAPIRegion {
    static func regionFrom(region: String) -> MomentsAPIRegion {
        switch region.lowercased() {
        case "germany":
            return .germany
        case "us_east":
            return .us_east
        case "sydney":
            return .sydney
        case "oregon":
            return .oregon
        case "tokyo":
            return .tokyo
        case "hong_kong":
            return .hong_kong
        default:
            return .custom(region)
        }
    }
}
