//
//  RSVPResponder.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import Foundation

class RSVPResponder {
    var department = "Engineering"
    var invitationId = "invalid_invitation_id"
    var name = "Oliver Dormody"
    var responseDate = NSDate()
    var responseBody = "Empty Response."
    var selected = false
    var uid = "invalid_id"
    var years = 0
    var imageURL = "https://ray.savagebeast.com/sbldap/image.cgi?uid=odormody"
    
    init(networkData: NSDictionary) {
        department = networkData["department"] as? String ?? department
        invitationId = networkData["invitation_id"] as? String ?? invitationId
        name = networkData["name"] as? String ?? name
        responseBody = networkData["response_body"] as? String ?? responseBody
        selected = networkData["selected"] as? Bool ?? selected
        uid = networkData["uid"] as? String ?? uid
        responseDate = convertDate(networkData["reponded"] as? Double ?? 0)
        years = networkData["years"] as? Int ?? years
    }
    
    func convertDate(dateTimStamp: Double) ->NSDate {
        return NSDate(timeIntervalSince1970:dateTimStamp/1000)
    }
}