//
//  MasterViewController.m
//  MyCars
//
//  Created by Vlastimil Adamovsky on 1/18/17.
//  Copyright © 2017 SharpHoster. All rights reserved.
//

#import "MasterViewController.h"
#import "DetailViewController.h"
#import "CarTableViewCell.h"
#import "Car.h"
//#import "AppDelegate.h"
#import "ImageFactory.h"


@interface MasterViewController ()
    {
        // Private instance variables
        AppDelegate * _appDelegate;
        NSArray *searchResults;
    }
  @property (weak, nonatomic) NSMutableArray * cars;
@end

@implementation MasterViewController

- (void)filterContentForSearchText:(NSString*)searchText scope:(NSString*)scope
{
    NSPredicate *resultPredicate = [NSPredicate predicateWithFormat:@"SELF contains[c] %@", searchText];
    searchResults = [self.cars filteredArrayUsingPredicate:resultPredicate];
}


-(BOOL)searchDisplayController:(UISearchDisplayController *)controller shouldReloadTableForSearchString:(NSString *)searchString
{
    [self filterContentForSearchText:searchString
                               scope:[[self.searchDisplayController.searchBar scopeButtonTitles] objectAtIndex:[self.searchDisplayController.searchBar selectedScopeButtonIndex]]];
    
    return YES;
}

- (AppDelegate *)appDelegate {
    if (!_appDelegate)
        _appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    return _appDelegate;
}
- (void)setAppDelegate:(AppDelegate *)newValue {
    _appDelegate = newValue;
}

- (NSMutableArray *) cars
{
    if (!_cars)
        _cars = self.appDelegate.imageFactory.carNames;
    return _cars;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self prefersStatusBarHidden];
}


- (void)viewWillAppear:(BOOL)animated {
    self.clearsSelectionOnViewWillAppear = self.splitViewController.isCollapsed;
    [super viewWillAppear:animated];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


//- (void)insertNewObject:(id)sender {
//    if (!self.objects) {
//        self.objects = [[NSMutableArray alloc] init];
//    }
//    [self.objects insertObject:[NSDate date] atIndex:0];
//    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:0];
//    [self.tableView insertRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationAutomatic];
//}


#pragma mark - Segues

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([[segue identifier] isEqualToString:@"showDetail"]) {
        NSIndexPath *indexPath = nil;
        NSString *mycarName = nil;
        
        //NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        if (self.searchDisplayController.active)
        {
            indexPath = [self.searchDisplayController.searchResultsTableView indexPathForSelectedRow];
            mycarName = searchResults[indexPath.row];
        }
        else
        {
            indexPath = [self.tableView indexPathForSelectedRow];
            mycarName = self.cars[indexPath.row];
        }
        
        
        //NSString *mycarName = self.cars[indexPath.row];
        DetailViewController *controller = (DetailViewController *)[[segue destinationViewController] topViewController];
        [controller setDetailItem: mycarName];
        controller.navigationItem.leftBarButtonItem = self.splitViewController.displayModeButtonItem;
        controller.navigationItem.leftItemsSupplementBackButton = YES;
    }
}


#pragma mark - Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (tableView == self.searchDisplayController.searchResultsTableView)
    {
       
        NSInteger c = searchResults.count;
        //return c < 1 ? self.cars.count : c;
        return searchResults.count;
    }
    else
    {
         return self.cars.count;
    }
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 44;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"CustomTableCell";
    CarTableViewCell *cell = (CarTableViewCell *) [self.tableView dequeueReusableCellWithIdentifier: CellIdentifier forIndexPath: indexPath];
                                                   
   if(cell == nil)
   {
       int k = 1;
   }
    
    
    NSString *mycarName;
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        mycarName = searchResults[indexPath.row];
    } else {
        mycarName = self.cars[indexPath.row];
    }

        cell.nameLabel.text = mycarName;
    
    
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        // retrive image on global queue
          UIImage * img = [self.appDelegate getImage: mycarName];
          //UIImage * img = [UIImage imageNamed: @"green_tea.jpg"];

    
        dispatch_async(dispatch_get_main_queue(), ^{
            //UIImage * img = [self.appDelegate getImage: mycarName];
            CarTableViewCell * cell = (CarTableViewCell *)[self.tableView cellForRowAtIndexPath:indexPath];
            // assign cell image on main thread
            cell.thumbnailImageView.image = img;
            [cell setNeedsLayout];
        });
   });
    
    
    return cell;
}



- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return NO;
}


//- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
//    if (editingStyle == UITableViewCellEditingStyleDelete) {
//        [self.objects removeObjectAtIndex:indexPath.row];
//        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
//    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
//        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view.
//    }
//}


@end
