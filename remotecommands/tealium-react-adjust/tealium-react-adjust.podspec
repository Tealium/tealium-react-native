require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "tealium-react-adjust"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  tealium-react-adjust
                   DESC
  s.homepage     = "https://github.com/github_account/tealium-react-adjust"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "Tealium" => "mobile-team@tealium.com" }
  s.platforms    = { :ios => "11.0" }
  s.source       = { :git => "https://github.com/github_account/tealium-react-adjust.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true
  s.static_framework = true

  s.dependency "React-Core"
  s.dependency "tealium-react-native", "~> 2.2"
  s.dependency "tealium-react-native-swift", "~> 2.2"
  s.dependency "tealium-swift/Core", "~> 2.6"
  s.dependency "TealiumAdjust", "~> 1.1.0"
end

