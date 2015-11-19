//
//  RSVPOfferModel.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import Foundation

enum RSVPOfferChoiceMethod: String {
    case FirstComeFirstServed = "first come first served"
    case Random = "random"
    case Manual = "manual"
}

class RSVPOfferModel {
    var uid: String = "invalid_id"
    var title: String = "Offer Title"
    var createDate: NSDate = NSDate()
    var endDate: NSDate = NSDate().dateByAddingTimeInterval(60*60*3)
    var numberOfInvitationAvaiable: Int = 0
    var emailDstributionList: String = "FYI-Oak@pandora.com"
    var invitationBody: String = "Empty Body."
    var rejectedBody: String = "Empty Body."
    var methodOfChoice: RSVPOfferChoiceMethod = .FirstComeFirstServed
    
    var responses: [Responder] = []
    
    var responsesCount: Int {
        return responses.count
    }
    
    var chosenCount: Int {
        var chosenCount = 0
        for response in responses {
            if response.selected {
                chosenCount++
            }
        }
        
        return chosenCount
    }
    
    init(networkData: NSDictionary) {
        uid = networkData["uid"] as? String ?? uid
        title = networkData["title"] as? String ?? title
        endDate = convertDate(networkData["rsvp_by_time"] as? String ?? "0")
        createDate = convertDate(networkData["create_time"] as? String ?? "0")
        numberOfInvitationAvaiable = networkData["response_accept_limit"] as? Int ?? numberOfInvitationAvaiable
        emailDstributionList = networkData["email_to"] as? String ?? emailDstributionList
        invitationBody = networkData["invitation_body"] as? String ?? invitationBody
        rejectedBody = networkData["rejectedBody"] as? String ?? rejectedBody
        methodOfChoice = RSVPOfferChoiceMethod(rawValue: networkData["method"] as? String ?? "manual") ?? .Manual
        
        responses.removeAll()
        for response in networkData["responses"] as? Array<NSDictionary> ?? [] {
            responses.append(Responder(networkData: response))
        }
    }
    
    func convertDate(dateString: String) ->NSDate {
        if let dateTimStamp = Double(dateString) {
            return NSDate(timeIntervalSince1970:dateTimStamp)
        } else {
            return NSDate()
        }
    }
}
