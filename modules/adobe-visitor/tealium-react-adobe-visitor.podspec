require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))
folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32'

Pod::Spec.new do |s|
  s.name         = "tealium-react-adobe-visitor"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "11.0" }
  s.source       = { :git => "https://github.com/tealium-bot/tealium-react-adobe-visitor.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm,swift}"

  s.dependency "React-Core"

  # Don't install the dependencies when we run `pod install` in the old architecture.
  if ENV['RCT_NEW_ARCH_ENABLED'] == '1' then
    s.compiler_flags = folly_compiler_flags + " -DRCT_NEW_ARCH_ENABLED=1"
    s.pod_target_xcconfig    = {
        "HEADER_SEARCH_PATHS" => "\"$(PODS_ROOT)/boost\"",
        "OTHER_CPLUSPLUSFLAGS" => "-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1",
        "CLANG_CXX_LANGUAGE_STANDARD" => "c++17"
    }
  end
end

require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name = "tealium-react-adobe-visitor"
  s.version = package["version"]
  s.summary = package["description"]
  s.description = <<-DESC
                  Tealium React Native Adobe Plugin
                   DESC
  s.homepage = "https://github.com/tealium/tealium-react-native"
  s.license = { :type => "Commercial", :file => "LICENSE" }
  s.authors = { "Tyler Rister" => "tyler.rister@tealium.com" }
  s.platforms = { :ios => "11.0" }
  s.source = { :git => "https://github.com/tealium/tealium-react-native.git", :tag => "#{s.version}" }
  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true
  s.swift_version = "5.0"
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES' }

  s.dependency "React-Core"
  s.dependency "tealium-react-native", "~> 2.2"
  s.dependency "tealium-react-native-swift", "~> 2.2"
  s.dependency "TealiumAdobeVisitorAPI", "~> 1.1"
  s.dependency "tealium-swift/Core", "~> 2.6"

end
