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
    var id: String = "invalid_id"
    var uid: String = "invalid_id"
    var title: String = "Offer Title"
    var createDate: NSDate = NSDate()
    var endDate: NSDate = NSDate().dateByAddingTimeInterval(60*60*3)
    var numberOfInvitationAvaiable: Int = 0
    var emailDstributionList: String = "FYI-Oak@pandora.com"
    var invitationBody: String = "Empty Body."
    var rejectedBody: String = "Empty Body."
    var methodOfChoice: RSVPOfferChoiceMethod = .FirstComeFirstServed
    
    var responses: [RSVPResponder] = []
    
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
        id = networkData["id"] as? String ?? id
        uid = networkData["uid"] as? String ?? uid
        title = networkData["title"] as? String ?? title
        endDate = convertDate(networkData["rsvp_by_time"] as? Double ?? 0)
        createDate = convertDate(networkData["create_time"] as? Double ?? 0)
        numberOfInvitationAvaiable = networkData["response_accept_limit"] as? Int ?? numberOfInvitationAvaiable
        emailDstributionList = networkData["email_to"] as? String ?? emailDstributionList
        invitationBody = networkData["invitation_body"] as? String ?? invitationBody
        rejectedBody = networkData["rejectedBody"] as? String ?? rejectedBody
        methodOfChoice = RSVPOfferChoiceMethod(rawValue: networkData["method"] as? String ?? "manual") ?? .Manual
        
        responses.removeAll()
        for response in networkData["responses"] as? Array<NSDictionary> ?? [] {
            responses.append(RSVPResponder(networkData: response))
        }
    }
    
    func refreshTheResponses(networkData: Array<NSDictionary>) {
        responses.removeAll()
        for response in networkData {
            responses.append(RSVPResponder(networkData: response))
        }
    }
    
    func convertDate(dateTimStamp: Double) ->NSDate {
        return NSDate(timeIntervalSince1970:dateTimStamp/1000)
    }
}
