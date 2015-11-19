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
    @IBOutlet weak var fullUpLabel: UILabel! {
        didSet {
            fullUpLabel.alpha = 0
            fullUpLabel.layer.cornerRadius = 2
            fullUpLabel.layer.masksToBounds = true
        }
    }
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
    @IBOutlet weak var chosenLabel: UILabel!
    @IBOutlet weak var seperatorView: UIView!
    
    var offerModel: RSVPOfferModel? = nil {
        didSet {
            guard let _offerModel = offerModel else { return }
            
            offerTitleLabel.text = _offerModel.title
            let formatter = NSDateFormatter()
            formatter.dateFormat = "MMM dd, yyyy"
            dateLabel.text = formatter.stringFromDate(_offerModel.createDate)
            responsesCountLabel.text = "\(_offerModel.responsesCount)"
            
            if _offerModel.endDate.compare(NSDate()) == NSComparisonResult.OrderedDescending {
                if _offerModel.chosenCount >= _offerModel.numberOfInvitationAvaiable {
                    fullUpLabel.text = "FULL UP"
                } else {
                    fullUpLabel.text = "LIVE"
                }
                
                fullUpLabel.alpha = 1
                chosenCountLabel.alpha = 0
                chosenLabel.alpha = 0
            } else {
                fullUpLabel.alpha = 0
                chosenLabel.alpha = 1
                chosenCountLabel.alpha = 1
                chosenCountLabel.text = "\(_offerModel.chosenCount)"
            }
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let bgColorView = UIView()
        bgColorView.backgroundColor = UIColor(red: 173/255, green: 216/255, blue: 230/255, alpha: 0.2)
        selectedBackgroundView = bgColorView        
    }

    override func setHighlighted(highlighted: Bool, animated: Bool) {
        let pandoraBlueColor = responsesCountLabel.backgroundColor
        let lightBlueColor = fullUpLabel.backgroundColor
        let grayColor = seperatorView.backgroundColor
        super.setHighlighted(highlighted, animated: animated)
        
        if(highlighted) {
            seperatorView.backgroundColor = grayColor
            fullUpLabel.backgroundColor = lightBlueColor
            responsesCountLabel.backgroundColor = pandoraBlueColor
            chosenCountLabel.backgroundColor = pandoraBlueColor
        }
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        let pandoraBlueColor = responsesCountLabel.backgroundColor
        let lightBlueColor = fullUpLabel.backgroundColor
        let grayColor = seperatorView.backgroundColor
        super.setSelected(selected, animated: animated)
        
        if selected {
            seperatorView.backgroundColor = grayColor
            fullUpLabel.backgroundColor = lightBlueColor
            responsesCountLabel.backgroundColor = pandoraBlueColor
            chosenCountLabel.backgroundColor = pandoraBlueColor
        }
    }
}
