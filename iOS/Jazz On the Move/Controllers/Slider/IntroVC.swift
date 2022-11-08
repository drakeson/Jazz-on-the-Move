//
//  IntroVC.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 23/11/2021.
//

import UIKit
import AVFoundation


@available(iOS 13.0, *)
class IntroVC: UIViewController {

    var AudioPlayer = AVAudioPlayer()
    @IBOutlet weak var coverImg: UIImageView!
    @IBOutlet weak var backView: UIView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var descLabel: UILabel!
    @IBOutlet weak var pageC: UIPageControl!
    
    private var items: [OnBoardingItem] = []
    private var currentPage: Int = 0
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        items = OnBoardingItem.placeholderItems
        setupPageControl()
        setupScreen(index: currentPage)
        updateImage(index: currentPage)
        setupGestures()
        playMusic()
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    
    
    
    
    //MARK: - Setups Start

    private func setupPageControl(){ pageC.numberOfPages = items.count }
    
    private func setupScreen(index: Int){
        titleLabel.text = items[index].title
        descLabel.text = items[index].desc
        coverImg.image = items[index].cover
        pageC.currentPage = index
        titleLabel.alpha = 1.0
        descLabel.alpha = 1.0
        titleLabel.transform = .identity
        descLabel.transform = .identity
    }
    
    private func setupGestures(){
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTapAnimation))
        view.isUserInteractionEnabled = true
        view.addGestureRecognizer(tapGesture)
    }
    
    
    private func updateImage(index: Int){
        let image = items[index].cover
        UIView.transition(with: coverImg, duration: 0.5, options: .transitionCrossDissolve, animations: {
            self.coverImg.image = image
        }, completion: nil)
    }
    
    
    
    private func playMusic() {
        do {
            if let bundle = Bundle.main.path(forResource: "raw", ofType: "mp3") {
                let alertSound = NSURL(fileURLWithPath: bundle)
                try AVAudioSession.sharedInstance().setCategory(AVAudioSession.Category.playback)
                try AVAudioSession.sharedInstance().setActive(true)
                try AudioPlayer = AVAudioPlayer(contentsOf: alertSound as URL)
                AudioPlayer.numberOfLoops = -1
                AudioPlayer.prepareToPlay()
                AudioPlayer.play()
            }
            } catch {
                print(error)
            }
    }
    
    //MARK: - Setups End
    
    
    
    
    //MARK: - Tap Gasture
    @objc func handleTapAnimation() {
        //MARK: - First Animation
        
        UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
            self.titleLabel.alpha = 0.8
            self.titleLabel.transform = CGAffineTransform(translationX: -30, y: 0)
        }) { _ in
            
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
                self.titleLabel.alpha = 0
                self.titleLabel.transform = CGAffineTransform(translationX: 0, y: -550)
            }, completion: nil )

        }
        //MARK: - Second Animation
        
        UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.7, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
            self.descLabel.alpha = 0.8
            self.descLabel.transform = CGAffineTransform(translationX: -30, y: 0)
            
        }) { _ in
            if self.currentPage < self.items.count - 1 {
                self.updateImage(index: self.currentPage + 1)
            }
            
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
                self.descLabel.alpha = 0
                self.descLabel.transform = CGAffineTransform(translationX: 0, y: -550)
            }) { _ in
                
                self.currentPage += 1
                if self.lastItem() { self.showMainApp() }
                else { self.setupScreen(index: self.currentPage) }
            }
        }
    }
    
    
    private func lastItem() -> Bool { return currentPage == self.items.count }
    
    
    private func showMainApp(){
        let mainApp = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MainVC")
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let sceneDelegate = windowScene.delegate as? SceneDelegate,
           let window = sceneDelegate.window {
            window.rootViewController = mainApp
            UIView.transition(with: window, duration: 0.25, options: .transitionCrossDissolve, animations: nil, completion: nil)
        }
    }
}

