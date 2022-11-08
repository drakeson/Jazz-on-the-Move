//
//  DataVM.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 29/11/2021.
//

import UIKit
import Firebase

class DataVM: NSObject {
    //MARK:- Deals
    typealias dataCallBack = (_ status:Bool, _ items: [Item], _ message: String) -> Void
    var dataCB: dataCallBack?
    let data = [AnyObject]()
    let db = Firestore.firestore()
    var itemList: [Item] = []
    
    //fileprivate
    func getItems(_ type: String){
        db.collection(Constants.FireBase.DEALS)
            .whereField(Constants.FireBase.TYPE, isEqualTo: type)
            .order(by: Constants.FireBase.TIME, descending: true).limit(to: 100)
            .addSnapshotListener() { (querySnapshot, err) in
            self.itemList = []
            if let err = err { self.dataCB?(false, self.itemList, "Error getting documents: \(err)") }
            else {
                if let snapshotDocs = querySnapshot?.documents {
                    for document in snapshotDocs {
                        let data = document.data()
                        DispatchQueue.main.async {
                            if let id = data[Constants.FireBase.ID] as? String,
                               let name = data[Constants.FireBase.NAME] as? String,
                               let poster = data[Constants.FireBase.POSTER] as? String,
                               let banner = data[Constants.FireBase.BANNER] as? String,
                               let link = data[Constants.FireBase.LINK] as? String,
                                   let type = data[Constants.FireBase.TYPE] as? String,
                                       let desc = data[Constants.FireBase.DESC] as? String {
                                        let newList = Item(id: id, name: name, poster: poster, banner: banner, link: link, type: type, desc: desc)
                                        self.itemList.append(newList)
                                        DispatchQueue.main.async {
                                            self.dataCB?(true, self.itemList, "Suceessful")
                                        }
                                }
                        }
                    }
                }
            }
        }
    }
    
    func itemsCompletionHandler(callBack: @escaping dataCallBack) {
        self.dataCB = callBack
    }
    //MARK:- Deals
}
