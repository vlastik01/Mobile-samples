//
//  DetailViewController.h
//  MyCars
//
//  Created by Vlastimil Adamovsky on 1/18/17.
//  Copyright © 2017 SharpHoster. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Car.h"

@interface DetailViewController : UIViewController

@property (strong, nonatomic) NSString *detailItem;

@property (weak, nonatomic) IBOutlet UIImageView *myImageView;
@property (weak, nonatomic) IBOutlet UILabel *name;

@end

