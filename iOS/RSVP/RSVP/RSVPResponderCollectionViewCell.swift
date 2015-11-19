//
//  RSVPResponderCollectionViewCell.swift
//  
//
//  Created by Oliver Dormody on 11/19/15.
//
//

import UIKit

class RSVPResponderCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var imageView: UIImageView! {
        didSet {
            imageView.layer.masksToBounds = true
            imageView.layer.cornerRadius = 25
        }
    }
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var userDepartmentLabel: UILabel!
    
    var responder: RSVPResponder! {
        didSet {
            let data = NSData(contentsOfURL : NSURL(string: responder.imageURL)!)
            imageView.image = UIImage(data : data!)
            
            userNameLabel.text = responder.name
            userDepartmentLabel.text = responder.department
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.backgroundColor = UIColor.whiteColor()
    }
}
