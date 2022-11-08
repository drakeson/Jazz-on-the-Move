//
//  HomeSliderVM.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 28/04/2022.
//

import Foundation

import UIKit
import Alamofire
import SwiftyJSON

class HomeSliderVM: NSObject {
    
    //MARK:- Deals
    typealias dataCallBack = (_ status:Bool, _ deal: [Slider], _ message: String) -> Void
    var dataCB: dataCallBack?
    let data = [AnyObject]()
    var sliderList = [Slider]()
    
    //fileprivate
    func getDeals(_ url:String){
        AF.request(url).responseJSON(completionHandler: { response in
            let status = response.response?.statusCode
            if status == 200 {
                switch response.result {
                case .success(_):
                    let sliderArray : NSArray  = response.value! as! NSArray
                    for i in 0..<sliderArray.count{
                        self.sliderList.append(
                            Slider(
                                id: (((sliderArray[i] as AnyObject).value(forKey: "id") as? String)),
                                image: ((sliderArray[i] as AnyObject).value(forKey: "image") as? String),
                                url: ((sliderArray[i] as AnyObject).value(forKey: "name") as? String)
                                ))
                    }
                    DispatchQueue.main.async {
                        self.dataCB?(true, self.sliderList, "Suceessful")
                    }
                    
                    break
                case let .failure(error):
                    print(error)
                    self.getDeals(url)
                }
            } else if status == 500 || status == 502 || status ==  503 || status ==  504{
                self.dataCB?(false, self.sliderList, "Internal Server Error")
            } else if status == 503 {
                self.dataCB?(false, self.sliderList, "Service Unavailable")
            } else if status == 404 {
                self.dataCB?(false, self.sliderList, "404 Not Found")
            } else if status == 408 {
                self.dataCB?(false, self.sliderList, "Request Timeout")
            } else {self.getDeals(url)
                self.dataCB?(false, self.sliderList, "Bad Internet")
            }
        })
    }
    
    func dataCompletionHandler(callBack: @escaping dataCallBack) {
        self.dataCB = callBack
    }
    //MARK:- Deals
}
