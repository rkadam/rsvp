//
//  RVSPViewController.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import UIKit

class RVSPOfferListViewController: UIViewController {
    private let offerListCellIdentifier = "RSVPOfferListTableViewCell"
    private let offerDetailSegueIdentifier = "OfferDetailViewSegue"

    @IBOutlet weak var tableView: UITableView! {
        didSet {
            tableView.separatorStyle = UITableViewCellSeparatorStyle.None
        }
    }
    @IBOutlet weak var createNewOfferButton: UIButton! {
        didSet {
            createNewOfferButton.layer.cornerRadius = 12.5
            
            let path = UIBezierPath()
            path.moveToPoint(CGPointMake(12.5, 5))
            path.addLineToPoint(CGPointMake(12.5, 20))
            path.moveToPoint(CGPointMake(5, 12.5))
            path.addLineToPoint(CGPointMake(20, 12.5))
            
            let shapeLayer = CAShapeLayer()
            shapeLayer.path = path.CGPath
            shapeLayer.strokeColor = UIColor.whiteColor().CGColor
            shapeLayer.fillColor = UIColor.clearColor().CGColor
            shapeLayer.lineWidth = 1
            
            createNewOfferButton.layer.addSublayer(shapeLayer)
        }
    }
    @IBOutlet weak var createNewOfferTextField: UITextField! {
        didSet {
            createNewOfferTextField.delegate = self
        }
    }
    @IBOutlet weak var createNewOfferGoButton: UIButton! {
        didSet {
            createNewOfferGoButton.alpha = 0
        }
    }
    @IBOutlet weak var indicatorView: UIActivityIndicatorView! {
        didSet {
            indicatorView.startAnimating()
        }
    }
    @IBOutlet weak var emptyListView: UIView! {
        didSet {
            emptyListView.alpha = 0
        }
    }
    
    var offerList: [RSVPOfferModel] = []
    private var targetOfferIndex: Int? = 0
    
    @IBOutlet weak var createNewOfferTextFieldLeadingConstraint: NSLayoutConstraint!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.navigationBar.translucent = false
        navigationController?.navigationBar.barTintColor = UIColor(red: 34/255, green: 64/255, blue: 153/255, alpha: 1)
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName:UIColor.whiteColor(), NSFontAttributeName: UIFont(name: "AvenirNext-DemiBold", size: 16)!]
        UIApplication.sharedApplication().statusBarStyle = UIStatusBarStyle.LightContent
        
        fetchOrderList()
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle {
        return UIStatusBarStyle.LightContent
    }
    
    private func fetchOrderList() {
        offerList.removeAll()
        RSVPNetworkManager.instance.getOfferList("asd") { (response, error) -> Void in
            if let _response = response as? NSDictionary {
                for offerData in _response["data"] as? Array<NSDictionary> ?? [] {
                    self.offerList.append(RSVPOfferModel(networkData: offerData))
                }
                
                self.tableView.reloadData()
                
                if self.offerList.count == 0 {
                    self.tableView.scrollEnabled = false
                    self.emptyListView.alpha = 1
                    self.view.bringSubviewToFront(self.emptyListView)
                }
            } else {
                // show the error message
                let alertController = UIAlertController(title: "Error", message: "Can't load the offer list.", preferredStyle: UIAlertControllerStyle.Alert)
                alertController.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Cancel, handler: nil))
                self.presentViewController(alertController, animated: true, completion: nil)
            }
            
            self.indicatorView.stopAnimating()
        }
    }
    
    @IBAction func goButtonTapped(sender: AnyObject) {
        // go to the new offer page
        let storyboard = UIStoryboard(name: "RSVPCreateOfferViewController", bundle: nil)
        if let viewController: UIViewController = storyboard.instantiateInitialViewController() {
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
    
    @IBAction func creatNewOfferButtonTapped(sender: AnyObject) {
        createNewOfferTextField.becomeFirstResponder()
    }
    
    // MARK: - Navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == offerDetailSegueIdentifier {
            guard let _targetOfferIndex = targetOfferIndex else { return }
            if let offerDetailVieController = segue.destinationViewController as? RSVPOfferDetailViewController {
                offerDetailVieController.offerModel = offerList[_targetOfferIndex]
            }
        }
    }
}

extension RVSPOfferListViewController: UITextFieldDelegate {
    func textFieldDidBeginEditing(textField: UITextField) {
        createNewOfferTextFieldLeadingConstraint.constant = 8
        
        UIView.animateWithDuration(0.33) { () -> Void in
            self.createNewOfferButton.alpha = 0
            self.createNewOfferGoButton.alpha = 1
            self.view.layoutIfNeeded()
        }
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        createNewOfferTextFieldLeadingConstraint.constant = 50
        
        UIView.animateWithDuration(0.33) { () -> Void in
            self.createNewOfferButton.alpha = 1
            self.createNewOfferGoButton.alpha = 0
            self.view.layoutIfNeeded()
        }
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        createNewOfferTextField.resignFirstResponder()
        return true
    }
}

extension RVSPOfferListViewController: UITableViewDelegate {
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 140
    }
    
    func tableView(tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 0
    }
    
    func tableView(tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 1
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        createNewOfferTextField.resignFirstResponder()
        targetOfferIndex = indexPath.row
        performSegueWithIdentifier(offerDetailSegueIdentifier, sender: nil)
    }
}

extension RVSPOfferListViewController: UITableViewDataSource {
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return offerList.count
    }
    
    func tableView(tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        return UIView(frame: CGRectZero)
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(offerListCellIdentifier) as! RSVPOfferListTableViewCell
        cell.offerModel = offerList[indexPath.row]
        return cell
    }
}
