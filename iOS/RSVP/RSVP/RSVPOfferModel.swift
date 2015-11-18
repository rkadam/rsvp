//
//  RSVPOfferModel.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import Foundation

enum RSVPOfferChoiceMethod: Int {
    case FirstComeFirstServed
    case Random
    case Manual
}

class RSVPOfferModel {
    var title: String = "Offer Title"
    var createDate: NSDate = NSDate()
    var responsesCount: Int = 0
    var chosenCount: Int = 0
    var numberOfInvitationAvaiable: Int = 0
    var emailDstributionList: String = "FYI-Oak@pandora.com"
    var methodOfChoice: RSVPOfferChoiceMethod = .FirstComeFirstServed
}
