//
//  RSVPCreateOfferViewController.swift
//  RSVP
//
//  Created by Christopher Cong on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import UIKit

//MARK: RSVPCreateOfferViewController
class RSVPCreateOfferViewController: UIViewController {
    
    let pandoraBlue = UIColor(red: 34, green: 64, blue: 153, alpha: 1)
    
    //MARK: Properties
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var titleCircle: UIView!
    @IBOutlet weak var descriptionTextView: UITextView!
    @IBOutlet weak var invitationTextField: UITextField!
    @IBOutlet weak var rsvpDatePicker: UIDatePicker!
    @IBOutlet weak var distributionListTextField: UITextField!
    @IBOutlet weak var methodOfChoicePicker: UIPickerView!
    @IBOutlet weak var previewOfferButton: UIButton!

    weak var activeField: UIView?
    
    //MARK: Initialization
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        self.setupShowKeyboard()
        self.setupHideKeyboard()
        
        let backButton = UIBarButtonItem(image: UIImage(named: "ic_back"), style: .Plain, target: self, action: Selector("goBack"))
        backButton.tintColor = UIColor.whiteColor()
        self.navigationItem.leftBarButtonItem = backButton
        
        let sendButton = UIBarButtonItem(title: "Send", style: .Plain, target: self, action: Selector("sendInvitation"))
        sendButton.tintColor = UIColor.whiteColor()
        sendButton.setTitleTextAttributes([NSFontAttributeName : UIFont(name: "AvenirNext-Regular", size: 15.0)!], forState: .Normal)
        self.navigationItem.rightBarButtonItem = sendButton
    }
    
    func setupShowKeyboard() {
        NSNotificationCenter.defaultCenter().addObserverForName(UIKeyboardDidShowNotification, object: nil, queue: NSOperationQueue.mainQueue()) { notification in
            if let info: Dictionary = notification.userInfo {
                if let kbSize = info[UIKeyboardFrameBeginUserInfoKey]?.CGRectValue.size {
                    let contentInsets = UIEdgeInsetsMake(0, 0, kbSize.height, 0)
                    self.scrollView.contentInset = contentInsets
                    self.scrollView.scrollIndicatorInsets = contentInsets
                    
                    if let textField = self.activeField {
                        var visibleRect = self.view.frame
                        visibleRect.size.height -= kbSize.height - textField.frame.height
                        
                        if !CGRectContainsPoint(visibleRect, textField.frame.origin) {
                            self.scrollView.scrollRectToVisible(textField.frame, animated: true)
                        }
                    }
                    
                }
            }
        }
    }
    
    func setupHideKeyboard() {
        NSNotificationCenter.defaultCenter().addObserverForName(UIKeyboardDidHideNotification, object: nil, queue: NSOperationQueue.mainQueue()) { notification in
            let contentInsets = UIEdgeInsetsZero
            self.scrollView.contentInset = contentInsets
            self.scrollView.scrollIndicatorInsets = contentInsets
        }
    }
    
    //MARK: View Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()

        titleCircle.layer.masksToBounds = true
        titleCircle.layer.cornerRadius = titleCircle.bounds.width / 2
        methodOfChoicePicker.selectRow(1, inComponent: 0, animated: false)
        
        let tapGesture = UITapGestureRecognizer(target: self, action: Selector("dismissKeyboardOnTap"))
        self.view.addGestureRecognizer(tapGesture)
        
        if self.navigationController != nil {
            self.navigationController!.navigationBar.titleTextAttributes = [
                NSFontAttributeName : UIFont(name: "AvenirNextCondensed-Regular", size: 24.0)!,
                NSForegroundColorAttributeName : UIColor.whiteColor()
            ]
        }
    }
    
    func dismissKeyboardOnTap() {
        if let field = self.activeField {
            field.resignFirstResponder()
        }
    }
    
    func goBack() {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    func sendInvitation() {
        
    }
}

//MARK: UITextViewDelegate
extension RSVPCreateOfferViewController: UITextViewDelegate {
    func textViewDidBeginEditing(textView: UITextView) {
        textView.becomeFirstResponder()
        activeField = textView
    }
    
    func textViewDidEndEditing(textView: UITextView) {
        textView.resignFirstResponder()
        activeField = nil
    }
}

//MARK: UITextFieldDelegate
extension RSVPCreateOfferViewController: UITextFieldDelegate {
    func textFieldDidBeginEditing(textField: UITextField) {
        textField.becomeFirstResponder()
        activeField = textField
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        textField.resignFirstResponder()
        activeField = nil
    }
}

//MARK: UIPickerViewDataSource
extension RSVPCreateOfferViewController: UIPickerViewDataSource {
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1;
    }
    
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return 3;
    }
}

//MARK: UIPickerViewDelegate
extension RSVPCreateOfferViewController: UIPickerViewDelegate {
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        switch row {
        case 0:
            return "First Come First Served"
        case 1:
            return "Random Order"
        case 2:
            return "Manual Choice"
        default:
            return "No one"
        }
    }
}