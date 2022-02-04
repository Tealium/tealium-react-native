require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name = "tealium-react-native"
  s.version = package["version"]
  s.summary = package["description"]
  s.description = <<-DESC
                  Tealium React Native Plugin
                   DESC
  s.homepage = "https://github.com/tealium/tealium-react-native"
  s.license = { :type => "Commercial", :file => "LICENSE.txt" }
  s.authors = { "Christina Sund" => "christina.sund@tealium.com", "James Keith" => "james.keith@tealium.com" }
  s.platforms = { :ios => "11.0" }
  s.source = { :git => "https://github.com/tealium/tealium-react-native.git", :tag => "#{s.version}" }
  s.source_files = "ios/**/*.{h,c,m}"
  s.requires_arc = true
  s.swift_version = "5.0"
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES' }

  s.dependency "React-Core"
  s.dependency "tealium-swift/Core", "~> 2.6"
  s.dependency "tealium-swift/TagManagement", "~> 2.6"
  s.dependency "tealium-swift/Collect", "~> 2.6"
  s.dependency "tealium-swift/Lifecycle", "~> 2.6"
  s.dependency "tealium-swift/RemoteCommands", "~> 2.6"
  s.dependency "tealium-swift/VisitorService", "~> 2.6"

end

