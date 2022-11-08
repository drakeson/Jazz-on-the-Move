//
//  SideMenuCell.swift
//  Jazz On the Move
//
//  Created by Kato Drake Smith on 19/04/2022.
//

import UIKit

class SideMenuCell: UITableViewCell {
    
    class var identifier: String { return String(describing: self) }
    class var nib: UINib { return UINib(nibName: identifier, bundle: nil) }
    
    @IBOutlet weak var iconImageView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    

    override func awakeFromNib() {
        super.awakeFromNib()
        
        // Background
        self.backgroundColor = .clear
        
        // Icon
        self.iconImageView.tintColor = #colorLiteral(red: 0.2549019754, green: 0.2745098174, blue: 0.3019607961, alpha: 1)
        
        // Title
        self.titleLabel.textColor = #colorLiteral(red: 0.3803921569, green: 0.3803921569, blue: 0.3803921569, alpha: 1)
    }
    
}
