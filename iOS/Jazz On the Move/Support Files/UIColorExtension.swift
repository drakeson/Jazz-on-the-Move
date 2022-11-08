//
//  UIColorExtension.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 24/11/2021.
//


import UIKit
import JGProgressHUD

extension UIColor {
    convenience init(hex hexString:String) {
        let hexString = hexString.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
        let scanner = Scanner(string: hexString)
        
        if (hexString.hasPrefix("#")) {
            scanner.scanLocation = 1
        }
        
        var color:UInt32 = 0
        scanner.scanHexInt32(&color)
        
        let mask = 0x000000FF
        let r = Int(color >> 16) & mask
        let g = Int(color >> 8) & mask
        let b = Int(color) & mask
        
        let red   = CGFloat(r) / 255.0
        let green = CGFloat(g) / 255.0
        let blue  = CGFloat(b) / 255.0
        
        self.init(red:red, green:green, blue:blue, alpha:1)
    }
}
extension UINavigationBar {

    func transparentNavigationBar() {
        self.setBackgroundImage(UIImage(), for: UIBarMetrics.default)
        self.shadowImage = UIImage()
        self.isTranslucent = true
        self.backgroundColor = UIColor.clear
    }
}
extension UIViewController {
    static let hud = JGProgressHUD(style: .light)

    func showLoader(_ show: Bool, withText text: String? = "Loading") {
        view.endEditing(true)
        UIViewController.hud.textLabel.text = text
        
        if show {
            UIViewController.hud.show(in: view)
        } else {
            UIViewController.hud.dismiss()
        }
    }
    
    
    func showError(_ errorMessage: String) {
        let alert = UIAlertController(title: "Error", message: errorMessage, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Ok", style: .cancel, handler: nil))
        present(alert, animated: true, completion: nil)
    }
    
    func setupToHideKeyboardOnTapOnView() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(
            target: self,
            action: #selector(UIViewController.dismissKeyboard))

        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }

    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}
extension Locale {
    static let currency: [String: (code: String?, symbol: String?, name: String?)] = isoRegionCodes.reduce(into: [:]) {
        let locale = Locale(identifier: identifier(fromComponents: [NSLocale.Key.countryCode.rawValue: $1]))
        $0[$1] = (locale.currencyCode, locale.currencySymbol, locale.localizedString(forCurrencyCode: locale.currencyCode ?? ""))
    }
}
