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
    
    @IBOutlet weak var createNewOfferTextFieldLeadingConstraint: NSLayoutConstraint!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func goButtonTapped(sender: AnyObject) {
        // go to the new offer page
    }
    
    @IBAction func creatNewOfferButtonTapped(sender: AnyObject) {
        createNewOfferTextField.becomeFirstResponder()
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
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
        return 150
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
    }
}

extension RVSPOfferListViewController: UITableViewDataSource {
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 2
    }
    
    func tableView(tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        return UIView(frame: CGRectZero)
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(offerListCellIdentifier) as! RSVPOfferListTableViewCell
        
        if indexPath.row == 0 {
            let offerModel = RSVPOfferModel()
            offerModel.responsesCount = 20
            cell.offerModel = offerModel
        } else {
            let offerModel = RSVPOfferModel()
            offerModel.endDate = NSDate()
            cell.offerModel = offerModel
        }
        return cell
    }
}