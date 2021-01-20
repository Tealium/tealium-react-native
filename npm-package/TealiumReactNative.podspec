require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "tealium-react-native"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  Tealium React Native Plugin
                   DESC
  s.homepage     = "https://github.com/tealium/tealium-react-native"
  s.license    = { :type => "Commercial", :file => "LICENSE.txt" }
  s.authors      = { "Christina Sund" => "christina.sund@tealium.com", "James Keith" => "james.keith@tealium.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/tealium/tealium-react-native.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "tealium-swift/Core"
  s.dependency "tealium-swift/TagManagement"
  s.dependency "tealium-swift/Collect"
  s.dependency "tealium-swift/Lifecycle"
  s.dependency "tealium-swift/RemoteCommands"
  s.dependency "tealium-swift/VisitorService"

end

