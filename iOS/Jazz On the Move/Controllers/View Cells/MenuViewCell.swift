//
//  MenuViewCell.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 24/11/2021.
//

import UIKit
import Cards

class MenuViewCell: UICollectionViewCell {
    
    @IBOutlet weak var dealsImg: UIImageView!
    @IBOutlet weak var dealsName: UILabel!
    @IBOutlet weak var dealsType: UILabel!
    
    
    override func prepareForReuse() {
        self.dealsImg.image = #imageLiteral(resourceName: "placeholder")
        super.prepareForReuse()
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        DispatchQueue.main.async {
            self.dealsType.textAlignment = .left
            self.dealsType.lineBreakMode = NSLineBreakMode.byWordWrapping
            self.dealsType.numberOfLines = 0
            self.dealsImg.clipsToBounds = true
            self.dealsName.textAlignment = .left
            self.dealsName.lineBreakMode = NSLineBreakMode.byWordWrapping
            self.dealsName.numberOfLines = 0
        }
    }
}
