//
//  AppDelegate.h
//  MyCars
//
//  Created by Vlastimil Adamovsky on 1/18/17.
//  Copyright © 2017 SharpHoster. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ImageFactory.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
- (UIImage *) getImage: (NSString *) imageName;
@property (strong, nonatomic) ImageFactory *imageFactory;
@end

