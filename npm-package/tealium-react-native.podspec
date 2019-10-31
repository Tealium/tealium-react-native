require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "tealium-react-native"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-tealium-module
                   DESC
  s.homepage     = "https://github.com/Tealium/tealium-react-native"
  s.license      = "SEE LICENSE IN LICENSE.txt"
  s.authors      = { "Tealium" => "https://tealium.com/" }
  s.platforms    = { :ios => "9.0", :tvos => "10.0" }
  s.source       = { :git => "https://github.com/Tealium/tealium-react-native.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "TealiumIOS", "5.6.2"
  s.dependency "TealiumIOSLifecycle", "5.6.2"
end