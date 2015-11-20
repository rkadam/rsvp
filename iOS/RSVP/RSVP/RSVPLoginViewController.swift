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
            emailField.tag = emailTextFieldTag
        }
    }
    @IBOutlet weak var passwordField: UITextField! {
        didSet {
            passwordField.tag = passwordTextFieldTag
        }
    }
    @IBOutlet weak var loginButton: UIButton! {
        didSet {
            loginButton.layer.cornerRadius = 6;
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
        self.view.endEditing(true)

        if isEmailValid(emailField.text!) && self.passwordField.text != "" {
            loginButton.alpha = 0.5
            loginButton.enabled = false
            activityIndicator.startAnimating()
            RSVPNetworkManager.instance.loginUser(emailField.text!, 🔑: passwordField.text!,
                😊: { (🍕, 💣) -> Void in
                    self.activityIndicator.stopAnimating()
                    let alert = UIAlertController(title: "LOGIN YES", message: "YES LOGIN", preferredStyle: UIAlertControllerStyle.Alert)
                    alert.addAction(UIAlertAction(title: "YES", style: UIAlertActionStyle.Default, handler: nil))
                    self.presentViewController(alert, animated: true, completion: nil)
                }) { (🍕, 💣) -> Void in
                    self.loginButton.alpha = 1
                    self.loginButton.enabled = true
                    self.activityIndicator.stopAnimating()
                    let alert = UIAlertController(title: "Login Error", message: "There was an error logging you in. Please try again later. Error code: \(💣!.code)", preferredStyle: UIAlertControllerStyle.Alert)
                    alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Default, handler: nil))
                    self.presentViewController(alert, animated: true, completion: nil)
            }
        } else {
            let alert = UIAlertController(title: "Invalid Login", message: "Please enter a valid email and password", preferredStyle: UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Default, handler: nil))
            self.presentViewController(alert, animated: true, completion: nil)
        }
        
    }
    
    @IBAction func cancelButtonTapped(sender: UIButton) {
        self.dismissViewControllerAnimated(true, completion: {});
    }
    
    func isEmailValid(email: String) -> Bool {
        if email == "" {
            return false
        }
        let regex = try! NSRegularExpression(pattern:"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$",
            options: [.CaseInsensitive])
        
        return regex.firstMatchInString(email, options:[],
            range: NSMakeRange(0, email.utf16.count)) != nil
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


