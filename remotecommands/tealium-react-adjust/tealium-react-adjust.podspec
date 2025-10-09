require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "tealium-react-adjust"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  tealium-react-adjust
                   DESC
  s.homepage     = "https://github.com/Tealium/tealium-react-native/remotecommands/tealium-react-adjust"
  s.license      = { :type => "Commercial", :file => "LICENSE" }
  s.authors      = { "Tealium" => "mobile-team@tealium.com" }
  s.platforms    = { :ios => "12.0" }
  s.source       = { :git => "https://github.com/Tealium/tealium-react-native.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true
  s.static_framework = true

  s.dependency "React-Core"
  s.dependency "tealium-react-native", "~> 2.6"
  s.dependency "tealium-swift/Core", "~> 2.18"
  s.dependency "TealiumAdjust", "~> 1.4"
end
