//
//  Constants.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 28/04/2022.
//

import Foundation

struct Constants {
    static let sliderURL = "https://myhouseug.com/jazz/slider.json"
    static let liqourURL = "https://empirivumatea.com/jazz/liqour.json"
    static let travelURL = "https://empirivumatea.com/jazz/travel.json"
    static let eventsURL = "https://empirivumatea.com/jazz/events.json"
    static let lifestyleURL = "https://empirivumatea.com/jazz/lifestyle.json"
    static let coffeeURL = "https://empirivumatea.com/jazz/coffee.json"
    
    static let googleMap = "AIzaSyC80mrpWcKRullowfPHQKds6ek5jz4BSOs"
    static let PHONE_NUMBER = ""
    static let EMAIL_ADD = ""
    static let URLP = ""
    static let SHARETXT = ""
    //UIColor(named: MAG.BrandColors.mainColor)
    
    struct BrandColors {
        static let mainColor = "MainColor"
        static let secdColor = "MainColorDark"
        static let backColor = "background"
        static let acentColor = "AccentColor"
    }
    
    
    
    //MARK:- MESSAGES
    struct AppMessages {
        static let success = "Successful"
        static let error = "Failed"
        static let load = "Loading..."
        static let tryA = "Try Again"
        static let enterMsg = "Please Enter "
        static let wifi = "Wifi Connection"
        static let mobile = "Cellular Connection"
        static let noConnection = "No Connection"
        static let emailError = "Email should not be empty"
        static let nameError = "Name should not be empty"
        static let successMsg = "Successful"
    }
    
    //MARK:- HELP
    struct HelpInfo {
        static let PHONE_NUMBER = "+256704566939"
        static let EMAIL_ADD = "isocialtransformation32@gmail.com"
        static let URLP = "https://marketgarden.ug"
        static let SHARETXT = "The application links (primarily) female market vendors from the local Ugandan grocery markets to customers who wish to purchase fresh goods Download Market Garden App at https://marketgarden.ug"
    }
    
    struct ServerMessages {
        static let m404 = "No Items Found"
        static let m500 = "Server Error"
        static let m402 = "Request Failed Try Again"
        static let mRest = "Try Again Later"
    }
    

    //MARK:- Profile
    struct FireBase {
        static let ID = "id"
        static let NAME = "name"
        static let POSTER = "poster"
        static let BANNER = "banner"
        static let LINK = "link"
        static let TYPE = "type"
        static let DESC = "desc"
        
        static let DEALS = "deals"
        static let TIME = "timestamp"
        
        static let LIFESTYLE = "lifestyle"
        static let EVENTS = "events"
        static let COFFEE = "coffee"
        static let LIQOUR = "liqour"
        static let TRAVEL = "travel"
    }
    
}
