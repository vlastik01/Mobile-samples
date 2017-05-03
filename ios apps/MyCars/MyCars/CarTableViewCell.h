//
//  CarTableViewCell.h
//  MyCars
//
//  Created by Vlastimil Adamovsky on 1/18/17.
//  Copyright © 2017 SharpHoster. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CarTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *thumbnailImageView;

@end
