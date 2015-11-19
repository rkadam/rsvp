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
    @IBOutlet weak var collectionView: UICollectionView!
//    var allResponders = [RSVPResponder](count: 10, repeatedValue:RSVPResponder(networkData: [:]))
//    var chosenResponders = [RSVPResponder](count: 3, repeatedValue:RSVPResponder(networkData: [:]))
    
    var allResponders: [RSVPResponder] =  []
    var chosenResponders: [RSVPResponder] =  []
    var offerModel: RSVPOfferModel? = nil {
        didSet {
            title = offerModel?.title
            allResponders = offerModel!.responses

            for (_, value) in allResponders.enumerate() {
                if value.selected {
                    chosenResponders.append(value)
                }
            }
        }
    }
    var shouldDisplayChosen: Bool = false {
        didSet {
            let collectionIsEmpty = shouldDisplayChosen ? (chosenResponders.count == 0) : (allResponders.count == 0)
            collectionView.hidden = collectionIsEmpty
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
            yearsAtPandoraChartView.userInteractionEnabled = false
            yearsAtPandoraChartView.descriptionText = ""
            yearsAtPandoraChartView.drawBarShadowEnabled = false
            yearsAtPandoraChartView.drawValueAboveBarEnabled = true
            
            yearsAtPandoraChartView.maxVisibleValueCount = 16
            yearsAtPandoraChartView.pinchZoomEnabled = false
            yearsAtPandoraChartView.drawGridBackgroundEnabled = false
            
            yearsAtPandoraChartView.xAxis.labelPosition = .Bottom
            //yearsAtPandoraChartView.xAxis.labelFont = UIFont()
            yearsAtPandoraChartView.xAxis.drawAxisLineEnabled = false
            yearsAtPandoraChartView.xAxis.spaceBetweenLabels = 2
            
            yearsAtPandoraChartView.leftAxis.labelCount = 3
            yearsAtPandoraChartView.leftAxis.labelPosition = .OutsideChart
            yearsAtPandoraChartView.leftAxis.spaceTop = 0.15
            
            yearsAtPandoraChartView.rightAxis.labelCount = 0
            
            yearsAtPandoraChartView.legend.position = .BelowChartLeft
            yearsAtPandoraChartView.legend.form = .Square
            yearsAtPandoraChartView.legend.formSize = 9
            yearsAtPandoraChartView.legend.xEntrySpace = 4
        }
    }

    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        navigationController?.navigationBar.tintColor = UIColor.whiteColor()
        setUpChart()
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
        
        var index = 0
        for (_, value) in yearDictionary {
            yVals.append(BarChartDataEntry(value: Double(value), xIndex: index))
            index++
        }
        
        let chartDataSet = BarChartDataSet(yVals: yVals, label: "YEARS AT PANDORA")
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