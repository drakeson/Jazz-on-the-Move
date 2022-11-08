//
//  HomeVC.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 23/11/2021.
//

import UIKit
import Alamofire
import KSImageCarousel
import SPIndicator

class HomeVC: UIViewController, KSICCoordinatorDelegate, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var sideMenuBtn: UIBarButtonItem!
    @IBOutlet weak var carousel: UIView!
    @IBOutlet weak var menuView: UICollectionView!
    
    var sliderList = [Slider]()
    var itemList: [Item] = []
    var homeSlider = HomeSliderVM()
    var dealsVM = HomeVM()
    var model = [URL(string: "https://via.placeholder.com/375x281/403ADA/FFFFFF?text=Image-0")!,]
    
    //MARK: - HomeVC Lyf Start
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupUI()
        self.loadSlider()
        self.loadDeals()
    }
    
    
    
    //MARK: - Setup UI Start
    func setupUI(){
        //Slider
        carousel.layer.cornerRadius = 10;
        carousel.layer.masksToBounds = true;
        
        navigationController?.navigationBar.tintColor = #colorLiteral(red: 0.06666666667, green: 0.0862745098, blue: 0.1450980392, alpha: 1)

        sideMenuBtn.target = revealViewController()
        sideMenuBtn.action = #selector(revealViewController()?.revealSideMenu)
        
        menuView.delegate = self
        menuView.dataSource = self
        self.view.addSubview(menuView)
        let cellTop = menuView.collectionViewLayout as! UICollectionViewFlowLayout
        cellTop.itemSize = CGSize(width: ((UIScreen.main.bounds.width) - 250) / 2 , height: 200.0)
        let TapDetector: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(Tapped(_:)))
        TapDetector.numberOfTapsRequired = 1
        TapDetector.numberOfTouchesRequired = 1
        menuView?.addGestureRecognizer(TapDetector)
    }
    
    
    func carouselDidTappedImage(at index: Int, coordinator: KSICCoordinator) {
//        let alert = UIAlertController(title: "KSImageCarousel", message: "🥷🏿 \(sliderList[index].url!)", preferredStyle: .alert)
//        let okAction = UIAlertAction(title: "Ok", style: .default, handler: nil)
//        alert.addAction(okAction)
//        present(alert, animated: true, completion: nil)
    }
    
    func loadSlider() {
        homeSlider.dataCompletionHandler{ [weak self] (status, deal, message) in
            guard self != nil else {return}
            if status {
                self!.sliderList = deal
                DispatchQueue.main.async {
                    self!.model.removeAll()
                    for i in 0..<deal.count {
                        self!.model.append(URL(string: "\(self!.sliderList[i].image!)")!)
                        if let coord = try? KSICInfiniteCoordinator(with: self!.model, placeholderImage: #imageLiteral(resourceName: "placeholder"), initialPage: 0) {
                                coord.shouldShowActivityIndicator = true
                                coord.activityIndicatorStyle = .white
                                coord.showCarousel(inside: self!.carousel, of: self!)
                                coord.startAutoScroll(withDirection: .left, interval: 7)
                                coord.delegate = self
                        }
                    }
                }
                return
            }
        }
        homeSlider.getDeals(Constants.liqourURL)
    }
    
    //MARK: - Setup UI End
    
    
    //MARK: - Collection View Start
    
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return itemList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "menuCell", for: indexPath) as! MenuViewCell
        cell.layer.cornerRadius = 5
        let item:Item = itemList[indexPath.row]
        cell.dealsName.text = item.name
        cell.dealsName.textAlignment = .left
        cell.dealsName.lineBreakMode = NSLineBreakMode.byWordWrapping
        cell.dealsName.numberOfLines = 0
        cell.dealsType.text = item.type!.uppercased()
        cell.dealsType.textAlignment = .left
        cell.dealsType.lineBreakMode = NSLineBreakMode.byWordWrapping
        cell.dealsType.numberOfLines = 0
        AF.request(item.poster!, method: .get).response{ response in
           switch response.result {
            case .success(let responseData):
                cell.dealsImg.image = nil
                cell.dealsImg.image = UIImage(data: responseData!)
            case .failure(let error):
                cell.dealsImg.image = #imageLiteral(resourceName: "placeholder")
                print("error---> ",error)
            }
        }
        
        return cell
    }
    
    @objc func Tapped(_ tap: UITapGestureRecognizer){
        let point: CGPoint = tap.location(in: menuView)
        let indexPath: IndexPath = (menuView?.indexPathForItem(at: point))!
        let cellIndex = itemList[indexPath.row]
        let allVC = self.storyboard?.instantiateViewController(withIdentifier: "detailsVC") as! DetailsVC
        allVC.itemID = cellIndex.id!
        allVC.nameE = cellIndex.name!
        allVC.bannerF = cellIndex.banner!
        allVC.linkF = cellIndex.link!
        allVC.typeF = cellIndex.type!
        allVC.descE = cellIndex.desc!
        self.present(allVC, animated: true)
    }
    
    func loadDeals() {
        dealsVM.dealsCompletionHandler{ [weak self] (status, items, message) in
            guard self != nil else {return}
            if status {
                DispatchQueue.main.async {
                    self!.itemList = items
                    self!.menuView.reloadData()
                }
            } else {
                SPIndicator.present(title: "Error", message: message, preset: .error)
            }
        }
        dealsVM.getDeals()
    }
    @IBAction func radioBtn(_ sender: Any) {
        SPIndicator.present(title: "Radio", message: "Coming Soon", preset: .done)

    }
    
    @IBAction func tvBtn(_ sender: Any) {
        SPIndicator.present(title: "Live TV", message: "Coming Soon", preset: .done)
    }
}
