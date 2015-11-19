//
//  RSVPOfferDetailViewController.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/19/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import UIKit

class RSVPOfferDetailViewController: UIViewController {
    
    var responders = [RSVPResponder](count: 6, repeatedValue:RSVPResponder(networkData: [:]))

    var offerModel: RSVPOfferModel? = nil {
        didSet {
            // initial the views here
            title = offerModel?.title
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.navigationBar.tintColor = UIColor.whiteColor()
    }
}

extension RSVPOfferDetailViewController: UICollectionViewDelegate {}

extension RSVPOfferDetailViewController: UICollectionViewDataSource {
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return responders.count
        // FIXME: get count from info
    }
    
    // make a cell for each cell index path
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        
        // get a reference to our storyboard cell
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier("ResponderDetailCell", forIndexPath: indexPath) as! RSVPResponderCollectionViewCell
        
        // Use the outlet in our custom class to get a reference to the UILabel in the cell
        cell.responder = responders[indexPath.row]
        
        return cell
    }
}