//
//  RSVP🎅🏽CollectionViewCell.swift
//  
//
//  Created by Oliver Dormody on 11/19/15.
//
//

import UIKit

class RSVP🎅🏽CollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var imageView: UIImageView! {
        didSet {
            self.imageView.layer.masksToBounds = true
            self.imageView.layer.cornerRadius = 25
        }
    }
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var userDepartmentLabel: UILabel!
    
//    var 🎅🏽: RSVP🎅🏽Model {
//        didSet {
//            
//        }
//    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.backgroundColor = UIColor.whiteColor()
    }
}
