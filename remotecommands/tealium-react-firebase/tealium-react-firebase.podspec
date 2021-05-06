require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "tealium-react-firebase"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  tealium-react-firebase
                   DESC
  s.homepage     = "https://github.com/github_account/tealium-react-firebase"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "Your Name" => "yourname@email.com" }
  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://github.com/github_account/tealium-react-firebase.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true
  # s.static_framework = true

  s.dependency "React"
  # ...
  s.dependency "tealium-react-native", "~> 2.0.2" # fingers crossed
  # s.dependency "TealiumFirebase", "~> 2.0.1"
  # s.dependency "Firebase", "~> 6.3"
  # s.dependency "FirebaseAnalytics", "~> 6.6"
end

