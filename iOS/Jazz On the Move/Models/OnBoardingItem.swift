//
//  OnBoardingItem.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 24/11/2021.
//

import UIKit

struct OnBoardingItem {
    let title: String
    let desc: String
    let cover: UIImage?
    
    
    static let placeholderItems: [OnBoardingItem] = [
        .init(title: "Liqour", desc: "Tickle your taste buds with fine wine and feel the chill of a great Whiskey.", cover: UIImage.init(named: "liqour")),
        .init(title: "Travel", desc: "From the ends of the world all the way, to nature's beauty.", cover: UIImage.init(named: "travel")),
        .init(title: "Events", desc: "Get to know what's hot on the streets, Where's that plot at?.", cover: UIImage.init(named: "events")),
        .init(title: "Entertainment", desc: "Looking for the top watched shows on Tv, settle in we've got you covered.", cover: UIImage.init(named: "entertainment"))
    ]
}
