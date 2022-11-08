//
//  AllVC.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 26/11/2021.
//

import UIKit
import Alamofire
import Firebase
import SPAlert


class AllVC: UIViewController, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {

    var indexID = 0
    var itemList = [Item]()
    var dataVM = DataVM()
    let db = Firestore.firestore()
    @IBOutlet weak var allCollectionView: UICollectionView!
    @IBOutlet weak var navBar: UINavigationBar!
    var url = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        allCollectionView.delegate = self
        allCollectionView.dataSource = self
        self.view.addSubview(allCollectionView)
        let cellTop = allCollectionView.collectionViewLayout as! UICollectionViewFlowLayout
        cellTop.itemSize = CGSize(width: ((UIScreen.main.bounds.width) - 100) / 2 , height: 200.0)
        
        
        if indexID == 0 {
            url = "liqour"
            self.navBar.topItem?.title = "Liquor"
        } else if indexID == 1 {
            url = "coffee"
            self.navBar.topItem?.title = "Coffee"
        } else if indexID == 2 {
            url = "travel"
            self.navBar.topItem?.title = "Travel"
        } else if indexID == 3 {
            url = "events"
            self.navBar.topItem?.title = "Events"
        } else if indexID == 4 {
            url = "entertainment"
            self.navBar.topItem?.title = "Entertainment"
        }
        loadItems(category: url)
    }
    
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return itemList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "allCell", for: indexPath) as! AllViewCell
        
        let item: Item = itemList[indexPath.row]
        cell.allName.text = item.name
        //cell.allPrice.text = item.price!
        cell.allName.lineBreakMode = NSLineBreakMode.byWordWrapping
        cell.allName.numberOfLines = 1
        cell.layer.cornerRadius = 5
        cell.clipsToBounds = true
        
        
//        AF.request(item.image!, method: .get).response{ response in
//           switch response.result {
//            case .success(let responseData):
//               cell.allImage!.image = nil
//                cell.allImage!.image = UIImage(data: responseData!)
//            case .failure(let error):
//                cell.allImage!.image = #imageLiteral(resourceName: "placeholder")
//                print("error---> ",error)
//            }
//        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let cellIndex = itemList[indexPath.row]
        let allVC = self.storyboard?.instantiateViewController(withIdentifier: "detailsVC") as! DetailsVC
        allVC.itemID = cellIndex.id!
        allVC.nameE = cellIndex.name!
//        allVC.priceE = cellIndex.price!
//        allVC.imagE = cellIndex.image!
        allVC.linkF = cellIndex.link!
        allVC.typeF = cellIndex.type!
        allVC.descE = cellIndex.desc!
        self.present(allVC, animated: true)
    }
    
    
    private func loadItems(category: String){
        showLoader(true, withText: "Loading...")
//        db.collection("deals")
//            .whereField("type", isEqualTo: category)
//            .order(by: "timestamp", descending: true)
//            .limit(to: 100)
//        .addSnapshotListener() { (querySnapshot, err) in
//            self.itemList.removeAll()
//        if let err = err {
//            self.showLoader(false)
//            SPAlert.present(title: "Error", message: "\(err)", preset: .error)
//        } else {
//            self.showLoader(false)
//            if let snapshotDocs = querySnapshot?.documents {
//                for document in snapshotDocs {
//                    let data = document.data()
//                    if let id = data["id"] as? String,
//                        let title = data["name"] as? String,
//                        let image = data["image"] as? String,
//                        let price = data["price"] as? String,
//                        let link = data["link"] as? String,
//                        let type = data["type"] as? String,
//                        let desc = data["desc"] as? String {
//                        let items = Item( id: id, name: title, image: image, price: price, link: link, type: type, desc: desc)
//                        self.itemList.append(items)
//                       }
//                }
//           }
//            DispatchQueue.main.async { self.allCollectionView.reloadData() }
//            
//        }
//      }
        
//        dataVM.dataCompletionHandler{ [weak self] (status, deals, message) in
//            guard self != nil else {return}
//            if status {
//                let itemArray : NSArray  = deals as! NSArray
//                for i in 0..<itemArray.count{
//                    self!.itemList.append(
//                        Item(
//                            id: (((itemArray[i] as AnyObject).value(forKey: "id") as? String)),
//                            name: (((itemArray[i] as AnyObject).value(forKey: "name") as? String)),
//                            image: (((itemArray[i] as AnyObject).value(forKey: "image") as? String)),
//                            price: (((itemArray[i] as AnyObject).value(forKey: "price") as? String)),
//                            link: (((itemArray[i] as AnyObject).value(forKey: "link") as? String)),
//                            type: (((itemArray[i] as AnyObject).value(forKey: "type") as? String)),
//                            desc: (((itemArray[i] as AnyObject).value(forKey: "desc") as? String)))
//                    )
//                }
//                DispatchQueue.main.async {
//                    self!.showLoader(false)
//                    self!.allCollectionView.reloadData()
//                }
//            } else {
//                SPAlert.present(title: message, preset: .error)
//            }
//        }
//        dataVM.getDeals(url)
        
    }
    
    
    @IBAction func backBtn(_ sender: Any) {self.dismiss(animated: true, completion: nil)}
    
}
