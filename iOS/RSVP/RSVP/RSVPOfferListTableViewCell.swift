//
//  RSVPOfferListTableViewCell.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import UIKit

class RSVPOfferListTableViewCell: UITableViewCell {

    @IBOutlet weak var offerTitleLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var responsesCountLabel: UILabel! {
        didSet {
            responsesCountLabel.layer.masksToBounds = true
            responsesCountLabel.layer.cornerRadius = 17.5
        }
    }
    @IBOutlet weak var chosenCountLabel: UILabel! {
        didSet {
            chosenCountLabel.layer.masksToBounds = true
            chosenCountLabel.layer.cornerRadius = 17.5
        }
    }
    
    var offerModel: RSVPOfferModel? = nil {
        didSet {
            guard let _offerModel = offerModel else { return }
            
            offerTitleLabel.text = _offerModel.title
            let formatter = NSDateFormatter()
            formatter.dateFormat = "MMM dd, yyyy"
            dateLabel.text = formatter.stringFromDate(_offerModel.createDate)
            responsesCountLabel.text = "\(_offerModel.responsesCount)"
            chosenCountLabel.text = "\(_offerModel.chosenCount)"
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let bgColorView = UIView()
        bgColorView.backgroundColor = UIColor(red: 173/255, green: 216/255, blue: 230/255, alpha: 0.2)
        selectedBackgroundView = bgColorView
    }

    override func setHighlighted(highlighted: Bool, animated: Bool) {
        let color = responsesCountLabel.backgroundColor
        super.setHighlighted(highlighted, animated: animated)
        
        if(highlighted) {
            responsesCountLabel.backgroundColor = color
            chosenCountLabel.backgroundColor = color
        }
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        let color = responsesCountLabel.backgroundColor
        super.setSelected(selected, animated: animated)
        
        if selected {
            responsesCountLabel.backgroundColor = color
            chosenCountLabel.backgroundColor = color
        }
    }
}
