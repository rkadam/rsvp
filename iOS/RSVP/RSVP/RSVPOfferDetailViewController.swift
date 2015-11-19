//
//  RSVPOfferDetailViewController.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/19/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import UIKit

class RSVPOfferDetailViewController: UIViewController {
    @IBOutlet weak var segmentedControl: UISegmentedControl!
    @IBOutlet weak var collectionView: UICollectionView!
//    var allResponders = [RSVPResponder](count: 10, repeatedValue:RSVPResponder(networkData: [:]))
//    var chosenResponders = [RSVPResponder](count: 3, repeatedValue:RSVPResponder(networkData: [:]))

    var allResponders: [RSVPResponder] =  []
    var chosenResponders: [RSVPResponder] =  []
    var offerModel: RSVPOfferModel? = nil {
        didSet {
            allResponders = offerModel!.responses

            for (_, value) in allResponders.enumerate() {
                if value.selected {
                    chosenResponders.append(value)
                }
            }
        }
    }
    
    var shouldDisplayChosen: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBAction func valueChangedForSegmentedControl(sender: UISegmentedControl) {
        switch segmentedControl.selectedSegmentIndex
        {
            case 0:
                shouldDisplayChosen = false
                collectionView.reloadData()
            case 1:
                shouldDisplayChosen = true
                collectionView.reloadData()
            default:
                break;
            
        }
    }

}

extension RSVPOfferDetailViewController: UICollectionViewDelegate {}

extension RSVPOfferDetailViewController: UICollectionViewDataSource {
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return shouldDisplayChosen ? chosenResponders.count : allResponders.count
        // FIXME: get count from info
    }
    
    // make a cell for each cell index path
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        
        // get a reference to our storyboard cell
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier("ResponderDetailCell", forIndexPath: indexPath) as! RSVPResponderCollectionViewCell
        
        // Use the outlet in our custom class to get a reference to the UILabel in the cell
        cell.responder = shouldDisplayChosen ? chosenResponders[indexPath.row] : allResponders[indexPath.row]
        
        return cell
    }
}