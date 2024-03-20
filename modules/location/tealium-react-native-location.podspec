require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name = "tealium-react-native-location"
  s.version = package["version"]
  s.summary = package["description"]
  s.description = <<-DESC
                  Tealium React Native Location Plugin
                   DESC
  s.homepage = "https://github.com/Tealium/tealium-react-native/modules/location"
  s.license = { :type => "Commercial", :file => "LICENSE" }
  s.authors = { "James Keith" => "james.keith@tealium.com" }
  s.platforms = { :ios => "12.0" }
  s.source = { :git => "https://github.com/Tealium/tealium-react-native.git", :tag => "#{s.version}" }
  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true
  s.swift_version = "5.0"
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES' }

  s.dependency "React-Core"
  s.dependency "tealium-react-native", "~> 2.4"
  s.dependency "tealium-react-native-swift", "~> 2.4"
  s.dependency "tealium-swift/Core", "~> 2.12"
  s.dependency "tealium-swift/Location", "~> 2.12"

end

