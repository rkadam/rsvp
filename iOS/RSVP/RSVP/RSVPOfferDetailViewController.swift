//
//  RSVPOfferDetailViewController.swift
//  RSVP
//
//  Created by Jianghua Kuai on 11/19/15.
//  Copyright © 2015 Pandora. All rights reserved.
//

import UIKit
import Charts

class RSVPOfferDetailViewController: UIViewController {
    
    var responders = [RSVPResponder](count: 6, repeatedValue:RSVPResponder(networkData: [:]))

    var offerModel: RSVPOfferModel? = nil {
        didSet {
            // initial the views here
            title = offerModel?.title
        }
    }
    @IBOutlet weak var segmentedControl: UISegmentedControl! {
        didSet {
            segmentedControl.tintColor = UIColor(red: 41/255, green: 235/255, blue: 227/255, alpha: 1)
        }
    }
    @IBOutlet weak var chartScrollView: UIScrollView!
    @IBOutlet weak var yearsAtPandoraChartView: BarChartView! {
        didSet {
            yearsAtPandoraChartView.drawBarShadowEnabled = false;
            yearsAtPandoraChartView.drawValueAboveBarEnabled = true;
            
            yearsAtPandoraChartView.maxVisibleValueCount = 16;
            yearsAtPandoraChartView.pinchZoomEnabled = false;
            yearsAtPandoraChartView.drawGridBackgroundEnabled = false;
            
            yearsAtPandoraChartView.xAxis.labelPosition = .Bottom
            //yearsAtPandoraChartView.xAxis.labelFont = UIFont()
            yearsAtPandoraChartView.xAxis.drawAxisLineEnabled = false
            yearsAtPandoraChartView.xAxis.spaceBetweenLabels = 2
            
            yearsAtPandoraChartView.leftAxis.labelCount = 3
            yearsAtPandoraChartView.leftAxis.labelPosition = .OutsideChart
            yearsAtPandoraChartView.leftAxis.spaceTop = 0.15
            
            yearsAtPandoraChartView.legend.position = .BelowChartLeft
            yearsAtPandoraChartView.legend.form = .Square
            yearsAtPandoraChartView.legend.formSize = 9
            yearsAtPandoraChartView.legend.xEntrySpace = 4
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.navigationBar.tintColor = UIColor.whiteColor()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        setUpChart()
    }
    
    private func setUpChart() {
        var yearDictionary = Dictionary<String, Int>()
        for responser in offerModel?.responses ?? [] {
            if yearDictionary[String(responser.years)] != nil {
                yearDictionary[String(responser.years)]!++
            } else {
                yearDictionary[String(responser.years)] = 1
            }
        }
        
        var yVals = [BarChartDataEntry]()
        
        for (key, value) in yearDictionary {
            yVals.append(BarChartDataEntry(value: Double(value), xIndex: 0))
        }
        
        let chartDataSet = BarChartDataSet(yVals: yVals)
        chartDataSet.barSpace = 0.35
        
        let dataSets = [chartDataSet]
        let keys = Array(yearDictionary.keys)
        let data = BarChartData(xVals: keys, dataSets: dataSets)
        yearsAtPandoraChartView.data = data
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