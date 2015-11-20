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
    private let manager = AFHTTPRequestOperationManager(baseURL: NSURL(string: "http://rsvp.savagebeast.com:80/api/"))
    
    func loginUser(🎅🏽: String, 🔑: String, 😊: NetworkCompletionBlock, 😞: NetworkCompletionBlock) -> Bool {
        var 🎉 = false
        manager.POST("login", parameters: ["uid":🎅🏽, "password":🔑], success: {(📡, 🍕) -> Void in
            🎉 = true
            😊(🍕, nil)
        }){(📡, 💣) -> Void in
            🎉 = false
            😞(nil, 💣)
        }
        
        return 🎉
    }
    
    func getOfferList(🎫: String, 🉐: NetworkCompletionBlock) {
        manager.GET("users/\(🎫)/invitations", parameters: nil, success: {
            (operation, response) -> Void in
            🉐(response, nil)
            }) { (operation, error) -> Void in
            🉐(nil, error)
        }
    }
    
    func postOffer(🍔: String, 🍺: AnyObject?, 💩: NetworkCompletionBlock) {
        manager.requestSerializer = AFJSONRequestSerializer()
        manager.POST("users/\(🍔)/invitations", parameters: 🍺, success: { (operation, response) in
            NSLog("\(operation.request.HTTPMethod)")
            NSLog("\(operation.request.allHTTPHeaderFields)")
            NSLog("\(operation.responseString)")
            💩(response, nil)
            }) { (operation, error) in
            💩(nil, error)
        }
    }
    
    func chooseWinners(🎫: String, 🎉: String, 🉐: NetworkCompletionBlock) {
        manager.GET("users/\(🎫)/invitations/\(🎉)/selectWinners", parameters: nil, success: {
            (operation, response) -> Void in
            🉐(response, nil)
            }) { (operation, error) -> Void in
                🉐(nil, error)
        }
    }
}