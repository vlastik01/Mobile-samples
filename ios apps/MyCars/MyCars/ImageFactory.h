//
//  ImageFactory.h
//  MyCars
//
//  Created by Vlastimil Adamovsky on 1/19/17.
//  Copyright © 2017 SharpHoster. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Car.h"

@interface ImageFactory : NSObject
  - (UIImage *)getImage:(NSString *)imageName;
   @property (strong, nonatomic) NSMutableArray *carNames;
@end
