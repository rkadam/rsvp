//
//  RSVPLoginViewController.swift
//  RSVP
//
//  Created by Oliver Dormody on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import UIKit

class RSVPLoginViewController: UIViewController {
    private let emailTextFieldTag = 0
    private let passwordTextFieldTag = 1
    
    @IBOutlet weak var emailField: UITextField! {
        didSet {
            emailField.returnKeyType = UIReturnKeyType.Next
            emailField.tag = emailTextFieldTag
        }
    }
    @IBOutlet weak var passwordField: UITextField! {
        didSet {
            passwordField.returnKeyType = UIReturnKeyType.Go
            passwordField.tag = passwordTextFieldTag
        }
    }
    @IBOutlet weak var loginButton: UIButton! {
        didSet {
            loginButton.layer.cornerRadius = 2;
        }
    }
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView! {
        didSet {
//            activityIndicator.hidden = true
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.emailField.becomeFirstResponder()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    @IBAction func loginButtonTapped(sender: UIButton) {
        loginButton.alpha = 0.5
        loginButton.enabled = false
        activityIndicator.startAnimating()
    }
}

extension RSVPLoginViewController: UITextFieldDelegate {
    func textFieldDidBeginEditing(textField: UITextField) {

    }
    
    func textFieldDidEndEditing(textField: UITextField) {

    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        if textField.tag == emailTextFieldTag {
            passwordField.becomeFirstResponder()
        } else if textField.tag == passwordTextFieldTag {
            // Perform sign up
        }
        
        return false
    }
}

