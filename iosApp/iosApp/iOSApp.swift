import SwiftUI
import Shared

@main
struct iOSApp: App {
    
    init() {
        // Initialize Koin for iOS
        KoinIOSKt.initKoinIOS()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}