//
//  DetailsVC.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 26/11/2021.
//

import UIKit
import Alamofire
import AVKit
import YouTubePlayer

class DetailsVC: UIViewController {

    @IBOutlet weak var navBar: UINavigationBar!
    @IBOutlet weak var playerView: YouTubePlayerView!
    @IBOutlet weak var coverImg: UIImageView!
    @IBOutlet weak var titleL: UILabel!
    @IBOutlet weak var priceL: UILabel!
    @IBOutlet weak var bookN: UIButton!
    @IBOutlet weak var callN: UIButton!
    @IBOutlet weak var descL: UILabel!
    
    var itemID = ""
    var nameE = ""
    var bannerF = ""
    var descE = ""
    var linkF = ""
    var typeF = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navBar.transparentNavigationBar()
        titleL.text = nameE
        descL.text = descE
        navBar.topItem!.title = nameE
        AF.request(bannerF, method: .get).response{ response in
           switch response.result {
            case .success(let responseData):
               self.coverImg.image = nil
               self.coverImg.image = UIImage(data: responseData!)
           case .failure(_):
               self.coverImg.image = #imageLiteral(resourceName: "placeholder")
            }
        }
        //callN.isHidden = true
        if typeF == "events" || typeF == "entertainment" {
            callN.isHidden = true
        }
        priceL.isHidden = true
            
    }
    
    
    
    @IBAction func bookBtn(_ sender: Any) {
        if typeF == "events" && linkF.contains("mp3"){
            guard let url = URL(string: linkF) else { return }
            print("URl: \(linkF)")
            let player = AVPlayer(url: url)
            let controller = AVPlayerViewController()
            controller.player = player
            present(controller, animated: true) {
                player.play()
            }
        } else if typeF == "events" && linkF.contains("youtube"){
            //let newLink = linkF.replacingOccurrences(of: "https://www.youtube.com/watch?v=", with: "")
            //KXtf2gEdnyQ
            coverImg.isHidden = true
            playerView.playerVars = [ "playsinline": "1", "controls": "0", "showinfo": "0" ] as YouTubePlayerView.YouTubePlayerParameters
            playerView.loadVideoID("KXtf2gEdnyQ")
        } else if typeF == "entertainment" {
            
        } else {
            
        }
        
        
    }
    
    @IBAction func callNow(_ sender: Any) {
        let url: NSURL = URL(string: "TEL://+256787667581")! as NSURL
        UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
    }
    
    @IBAction func backBtn(_ sender: Any) { self.dismiss(animated: true, completion: nil)}
    
}
