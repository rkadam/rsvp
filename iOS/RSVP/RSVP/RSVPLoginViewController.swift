//
//  RSVPLoginViewController.swift
//  RSVP
//
//  Created by Oliver Dormody on 11/18/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import UIKit

class RSVPLoginViewController: UIViewController {
    
    @IBOutlet weak var emailField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
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
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    @IBAction func loginButtonTapped(sender: UIButton) {
        loginButton.alpha = 0.5
        loginButton.enabled = false
        activityIndicator.startAnimating()
    }
}

