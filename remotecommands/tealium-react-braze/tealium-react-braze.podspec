# tealium-react-braze.podspec

require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "tealium-react-braze"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  tealium-react-braze
                   DESC
  s.homepage     = "https://github.com/Tealium/tealium-react-native/remotecommands/tealium-react-braze"
  s.license      = { :type => "Commercial", :file => "LICENSE" }
  s.authors      = { "Tyler Rister" => "tyler.rister@tealium.com" }
  s.platforms    = { :ios => "12.0" }
  s.source       = { :git => "https://github.com/Tealium/tealium-react-native.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,cc,cpp,m,mm,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "tealium-react-native", "~> 2.6"
  s.dependency "tealium-swift/Core", "~> 2.18"
  s.dependency "TealiumBraze", "~> 3.5"
end

