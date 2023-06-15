# tealium-react-appsflyer.podspec

require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "tealium-react-appsflyer"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  tealium-react-appsflyer
                   DESC
  s.homepage     = "https://github.com/Tealium/tealium-react-native/remotecommands/tealium-react-appsflyer"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "Tealium" => "mobile-team@tealium.com" }
  s.platforms    = { :ios => "11.0" }
  s.source       = { :git => "https://github.com/tealium-react-native.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,cc,cpp,m,mm,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "tealium-react-native", "~> 2.2"
  s.dependency "tealium-react-native-swift", "~> 2.2"
  s.dependency "tealium-swift/Core", "~> 2.9"
  s.dependency "TealiumAppsFlyer", "~> 2.1"
end

