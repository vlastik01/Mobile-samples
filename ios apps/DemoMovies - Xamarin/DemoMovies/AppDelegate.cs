using Foundation;
using UIKit;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using MonoTouch.Dialog;

                
namespace DemoMovies
{
	
	[Register("AppDelegate")]
	public class AppDelegate : UIApplicationDelegate
	{
		

		public override UIWindow Window
		{
			get;
			set;
		}


	
		UINavigationController _nav;
		DialogViewController _rootVC;
		RootElement _rootElement;

	

		public override bool FinishedLaunching(UIApplication application, NSDictionary launchOptions)
		{
			Window = new UIWindow(UIScreen.MainScreen.Bounds);

			_rootElement = new RootElement("Demo Movies"){
				new Section() {
								new LoadMoreElement("Load Movies", "Movies loading", async (LoadMoreElement obj) =>
													{
														var client = new HttpClient();
														string json = await client.GetStringAsync(@"https://facebook.github.io/react-native/movies.json");
														obj.Animating = false;
														_rootElement[0].Remove(obj);
														AddMovies(json);
													}
												   ) { BackgroundColor = UIColor.LightGray }
								}
			};


			_rootVC = new DialogViewController(_rootElement);
			_nav = new UINavigationController(_rootVC);

			Window.RootViewController = _nav;
			Window.MakeKeyAndVisible();
			return true;
		}

		private void AddMovies(string jsonstring)
		{
			var o = JObject.Parse(jsonstring);
			_rootElement[0].AddAll(o["movies"].Select((movie)=> new StringElement(movie["title"].ToString(), movie["releaseYear"].ToString())));

		}
	}
}

