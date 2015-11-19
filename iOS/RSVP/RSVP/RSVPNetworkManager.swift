//
//  RSVPNetworkManager.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import Foundation
import AFNetworking

typealias NetworkCompletionBlock = (Any?, NSError?)->Void

class RSVPNetworkManager {
    static let instance = RSVPNetworkManager()
    private let manager = AFHTTPRequestOperationManager(baseURL: NSURL(string: "http://aai.savagebeast.com:9000/api/"))

    func loginUser(🎅🏽: String, 🔑: String) -> Bool {
        return true
    }
    
    func getOfferList(🎫: String, 🉐: NetworkCompletionBlock) {
        manager.GET("users/\(🎫)/invitations", parameters: nil, success: {
            (operation, response) -> Void in
            🉐(response, nil)
            }) { (operation, error) -> Void in
            🉐(nil, error)
        }
    }
}