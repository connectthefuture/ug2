//
//  ReadLaterActivity.m
//  newsyc
//
//  Created by Mark Nemec on 22/10/12.
//
//

#import "InstapaperActivity.h"
#import "InstapaperController.h"

@implementation InstapaperActivity

// Returns the type of the Activity
- (NSString *)activityType {
    return @"Read Later";
}

// Returns the title displayed in the 'Read Later' button
- (NSString *)activityTitle {
    return @"Read Later";
}

// Returns the icon image for the button
- (UIImage *)activityImage {
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad) {
        return [UIImage imageNamed:@"instapaper-ipad"];
    }
    return [UIImage imageNamed:@"instapaper"];
}

// It is always the case that sharing will be invoked with a URL
- (BOOL)canPerformWithActivityItems:(NSArray *)activityItems {
    return YES;
}

// Initialize the URL variable to the first supplied argument
- (void)prepareWithActivityItems:(NSArray *)activityItems {
    URL = [activityItems objectAtIndex:0];
}

// Returns the login controller in case the user is not logged in
// returns nil otherwise
- (UIViewController *)activityViewController {
    return [[InstapaperController sharedInstance] submitURL:URL];
}

// This is the method invoked when a user clicks on the 'Read Later' button
- (void)performActivity {
    [self activityViewController];
    [self activityDidFinish:YES];
}

@end
