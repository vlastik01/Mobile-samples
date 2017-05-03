//
//  ImageFactory.m
//  MyCars
//
//  Created by Vlastimil Adamovsky on 1/19/17.
//  Copyright © 2017 SharpHoster. All rights reserved.
//

#import "ImageFactory.h"

@implementation ImageFactory
    NSString *jsonFile = @"cars";
    NSMutableDictionary *cars;
    NSCache *cache;
    NSMutableArray *carNames;

  -(ImageFactory *)init
    {
        self = [super init];
        //read json
        [self loadJson: jsonFile];
        return self;
    }
   //private
   -(void) loadJson: (NSString *) jsonFileName
    {
        //JSON read into array
        NSString *filePath = [[NSBundle mainBundle] pathForResource: jsonFileName ofType: @"json"];
        NSError *error;
        NSString *fileContents = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:&error];
        if(error) {
            NSLog(@"Error reading file: %@", error.localizedDescription);
        }
        NSData* data = [fileContents dataUsingEncoding:NSUTF8StringEncoding];
        NSDictionary *myJsonObject = (NSDictionary*)[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
        if(error) {
            NSLog(@"Error parsing JSON: %@", error.localizedDescription);
        }
        
        NSArray* anArray = (NSArray*) myJsonObject[@"cars"];
        cars = [NSMutableDictionary new];
        self.carNames = [NSMutableArray new];
        for (NSDictionary *dict in anArray) {
              cars[dict[@"name"]] = dict[@"image"] ;
                     }
         [self.carNames addObjectsFromArray: cars.allKeys];
        //now we need NSCHache to cache images
        cache = [NSCache new];
        [cache setName: @"cars"];
        
    }

  -(UIImage *)getImage:(NSString *)imageName
    {
        NSString *url = cars[imageName];
        UIImage *image = [cache objectForKey: url];
        if (image)
            return image;
        else
        {
            //load the image
            NSURL *urlNew = [NSURL URLWithString: url];
            NSData *data = [NSData dataWithContentsOfURL: urlNew];
            image = [UIImage imageWithData: data ];
            [cache setObject:image forKey:url];
           
            
        }
        return image;
    }
@end
