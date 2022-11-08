//
//  AppDelegate.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 23/11/2021.
//

import UIKit
import Firebase
import IQKeyboardManagerSwift
import SwiftKeychainWrapper
import UserNotifications

@main
@available(iOS 13.0, *)
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate  {

    var window: UIWindow?
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        IQKeyboardManager.shared.enable = true
        if #available(iOS 13.0, *) { window?.overrideUserInterfaceStyle = .light }
        
        FirebaseApp.configure()
        _ = Firestore.firestore()
        
        let center  = UNUserNotificationCenter.current()
        center.delegate = self
        // set the type as sound or badge
        center.requestAuthorization(options: [.sound,.alert,.badge]) { (granted, error) in
            // Enable or disable features based on authorization

            }
            application.registerForRemoteNotifications()
        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        
        Messaging.messaging().token { token, error in
           // Check for error. Otherwise do what you will with token here
            if let error = error {
                print("Error fetching remote instange ID: \(error)")
            } else {
                let _: Bool = KeychainWrapper.standard.set(token!, forKey: "TOKEN")
                //print("Remote instance ID token: \(token!)")
            }
        }
        
    }

    private func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: NSError) {
      print("Registration failed!")
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,  willPresent notification: UNNotification, withCompletionHandler   completionHandler: @escaping (_ options:   UNNotificationPresentationOptions) -> Void) {
        print("Handle push from foreground")
        // custom code to handle push while app is in the foreground
        print("\(notification.request.content.userInfo)")
    }

    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        print("Handle push from background or closed")
        // if you set a member variable in didReceiveRemoteNotification, you  will know if this is from closed or background
        print("\(response.notification.request.content.userInfo)")
    }
    
    
    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

