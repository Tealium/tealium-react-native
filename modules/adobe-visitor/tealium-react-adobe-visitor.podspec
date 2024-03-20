require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name = "tealium-react-adobe-visitor"
  s.version = package["version"]
  s.summary = package["description"]
  s.description = <<-DESC
                  Tealium React Native Adobe Plugin
                   DESC
  s.homepage = "https://github.com/Tealium/tealium-react-native/modules/adobe-visitor"
  s.license = { :type => "Commercial", :file => "LICENSE" }
  s.authors = { "Tyler Rister" => "tyler.rister@tealium.com" }
  s.platforms = { :ios => "12.0" }
  s.source = { :git => "https://github.com/Tealium/tealium-react-native.git", :tag => "#{s.version}" }
  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true
  s.swift_version = "5.0"
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES' }

  s.dependency "React-Core"
  s.dependency "tealium-react-native", "~> 2.4"
  s.dependency "tealium-react-native-swift", "~> 2.4"
  s.dependency "TealiumAdobeVisitorAPI", "~> 1.2"
  s.dependency "tealium-swift/Core", "~> 2.12"

end
