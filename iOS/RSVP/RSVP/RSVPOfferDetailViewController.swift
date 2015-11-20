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
    
    var allResponders: [RSVPResponder] {
        return offerModel!.responses
    }
    var chosenResponders: [RSVPResponder] {
        var result = [RSVPResponder]()
        for (_, value) in allResponders.enumerate() {
            if value.selected {
                result.append(value)
            }
        }
        return result
    }
    var offerModel: RSVPOfferModel? = nil {
        didSet {
            title = offerModel?.title
        }
    }
    var shouldDisplayChosen: Bool = false
    @IBOutlet weak var segmentedControl: UISegmentedControl! {
        didSet {
            segmentedControl.tintColor = UIColor(red: 41/255, green: 235/255, blue: 227/255, alpha: 1)
        }
    }
    @IBOutlet weak var chartScrollView: UIScrollView! {
        didSet {
            chartScrollView.alpha = 0
            chartScrollView.delegate = self
        }
    }
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
            yearsAtPandoraChartView.xAxis.drawAxisLineEnabled = false
            yearsAtPandoraChartView.xAxis.spaceBetweenLabels = 2
            
            yearsAtPandoraChartView.leftAxis.labelCount = 3
            yearsAtPandoraChartView.leftAxis.labelPosition = .OutsideChart
            yearsAtPandoraChartView.leftAxis.spaceTop = 0.15
            
            yearsAtPandoraChartView.rightAxis.labelCount = 0
            yearsAtPandoraChartView.rightAxis
            
            yearsAtPandoraChartView.legend.position = .BelowChartLeft
            yearsAtPandoraChartView.legend.form = .Square
            yearsAtPandoraChartView.legend.formSize = 9
            yearsAtPandoraChartView.legend.xEntrySpace = 0
        }
    }
    @IBOutlet weak var departmentChartView: PieChartView! {
        didSet {
            departmentChartView.userInteractionEnabled = false
            departmentChartView.usePercentValuesEnabled = false
            departmentChartView.holeTransparent = true
            departmentChartView.holeRadiusPercent = 0.58
            departmentChartView.transparentCircleRadiusPercent = 0.61
            departmentChartView.descriptionText = ""
            departmentChartView.setExtraOffsets(left: 5, top: 10, right: 5, bottom: 5)
            
            departmentChartView.drawCenterTextEnabled = false
            departmentChartView.drawSliceTextEnabled = false
            
            departmentChartView.drawHoleEnabled = true
            departmentChartView.rotationAngle = 0.0
            departmentChartView.rotationEnabled = true
            departmentChartView.highlightPerTapEnabled = true
            
            let legend = departmentChartView.legend
            legend.position = .LeftOfChart
            legend.xEntrySpace = 7
            legend.yEntrySpace = 0
            legend.yOffset = 0
        }
    }
    @IBOutlet weak var pageControl: UIPageControl! {
        didSet {
            pageControl.currentPage = 1
            pageControl.pageIndicatorTintColor = UIColor(red: 34/255, green: 64/255, blue: 153/255, alpha: 1)
            pageControl.alpha = 0
        }
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
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.navigationBar.tintColor = UIColor.whiteColor()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        
        chartScrollView.contentSize = CGSizeMake(chartScrollView.bounds.width*2, chartScrollView.bounds.height)
        setUpChart()
    }
    
    @IBAction func winnerButtonTapped(sender: AnyObject) {
        guard let offerId = offerModel?.id else { return }
        
        RSVPNetworkManager.instance.chooseWinners(RSVP🎅🏽Model.instance.userId, 🎉: offerId) {
            (response, error) -> Void in
            guard let success = (response as? NSDictionary)?["success"] as? Bool else {
                let alertController = UIAlertController(title: "Error", message: "Can't choose winners, ", preferredStyle: UIAlertControllerStyle.Alert)
                alertController.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Cancel, handler: nil))
                self.presentViewController(alertController, animated: true, completion: nil)
                return
            }
            
            if success {
                let alertController = UIAlertController(title: "Success", message: "Winners are ready!", preferredStyle: UIAlertControllerStyle.Alert)
                alertController.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Cancel, handler: nil))
                self.presentViewController(alertController, animated: true, completion: nil)
                
                // refresh the response
                print(response)
                self.offerModel?.refreshTheResponses((response as? NSDictionary)?["data"]?["responses"] as? Array<NSDictionary> ?? [])
                self.collectionView.reloadData()
            } else {
                let alertController = UIAlertController(title: "Error", message: "Can't choose winners twice..", preferredStyle: UIAlertControllerStyle.Alert)
                alertController.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Cancel, handler: nil))
                self.presentViewController(alertController, animated: true, completion: nil)
            }
        }
    }
    
    private func setUpChart() {
        // bar chart
        var yearDictionary = Dictionary<String, Int>()
        for responser in offerModel?.responses ?? [] {
            if yearDictionary[String(responser.years)] != nil {
                yearDictionary[String(responser.years)]!++
            } else {
                yearDictionary[String(responser.years)] = 1
            }
        }
        
        var barChartYVals = [BarChartDataEntry]()
        
        var allKeys = Array(yearDictionary.keys)
        allKeys = allKeys.sort {
            return $0 < $1
        }
        
        var index = 0
        for (_, value) in yearDictionary {
            barChartYVals.append(BarChartDataEntry(value: Double(value), xIndex: index))
            index++
        }
        
        
        let barChartDataSet = BarChartDataSet(yVals: barChartYVals, label: "YEARS AT PANDORA")
        barChartDataSet.barSpace = 0.35
        barChartDataSet.valueFormatter = NSNumberFormatter()
        barChartDataSet.valueFormatter?.minimumFractionDigits = 0
        
        yearsAtPandoraChartView.data = BarChartData(xVals: allKeys, dataSets: [barChartDataSet])
        
        // pie chart
        var departmentDictionary = Dictionary<String, Int>()
        for responser in offerModel?.responses ?? [] {
            if departmentDictionary[responser.department] != nil {
                departmentDictionary[responser.department]!++
            } else {
                departmentDictionary[responser.department] = 1
            }
        }
        
        var pieChartYVals = [BarChartDataEntry]()
        
        index = 0
        for (_, value) in departmentDictionary {
            pieChartYVals.append(BarChartDataEntry(value: Double(value), xIndex: index))
            index++
        }
        
        let pieChartDataSet = PieChartDataSet(yVals: pieChartYVals, label: "DEPARTMENTS")
        pieChartDataSet.sliceSpace = 2.0
        pieChartDataSet.colors = [UIColor(red: 34/255, green: 64/255, blue: 153/255, alpha: 1),
            UIColor(red: 34/255, green: 64/255, blue: 153/255, alpha: 0.8),
            UIColor(red: 34/255, green: 64/255, blue: 153/255, alpha: 0.6),
            UIColor(red: 34/255, green: 64/255, blue: 153/255, alpha: 0.4),
            UIColor(red: 34/255, green: 64/255, blue: 153/255, alpha: 0.2),
            UIColor(red: 34/255, green: 64/255, blue: 153/255, alpha: 0.1)]
        
        let pieChartData = BarChartData(xVals: Array(departmentDictionary.keys), dataSets: [pieChartDataSet])
        departmentChartView.data = pieChartData
        departmentChartView.animate(xAxisDuration: 1.0)
        
        UIView.animateWithDuration(0.33) { () -> Void in
            self.chartScrollView.alpha = 1
            self.pageControl.alpha = 1
        }
    }
}

extension RSVPOfferDetailViewController: UIScrollViewDelegate {
    func scrollViewDidScroll(scrollView: UIScrollView) {
        if scrollView.contentOffset.x >= scrollView.bounds.size.width {
            pageControl.currentPage = 0
        } else {
            pageControl.currentPage = 1
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