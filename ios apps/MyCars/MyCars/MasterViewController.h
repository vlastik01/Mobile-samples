//
//  MasterViewController.h
//  MyCars
//
//  Created by Vlastimil Adamovsky on 1/18/17.
//  Copyright © 2017 SharpHoster. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@class DetailViewController;

@interface MasterViewController : UITableViewController

//@property (weak, nonatomic) IBOutlet UITableViewCell *thumbnailImageView;
@property (strong, nonatomic) DetailViewController *detailViewController;
@property (strong, nonatomic) AppDelegate *appDelegate;
@end

