using Android.App;
using Android.Widget;
using Android.OS;
using System.Net;
using System.IO;

//using System.Json;
using System.Linq;
using System.Collections.Generic;
using Newtonsoft.Json;


namespace PizzaToppingsAndroid
{
	[Activity (Label = "PizzaToppingsAndroid", MainLauncher = true, Icon = "@mipmap/icon")]
	public class MainActivity : Activity
	{
		

		protected override void OnCreate (Bundle savedInstanceState)
		{
			base.OnCreate (savedInstanceState);
			SetContentView (Resource.Layout.Main);

			Button button = FindViewById<Button> (Resource.Id.myButton);
			
			button.Click += delegate {
				//get json
#region could be put in a separate library to share between ios and android
				WebRequest request = WebRequest.Create ("http://files.olo.com/pizzas.json");
				WebResponse response = request.GetResponse ();
				Stream dataStream = response.GetResponseStream ();
				StreamReader reader = new StreamReader (dataStream);

				string responseFromServer = reader.ReadToEnd ();
				reader.Close ();
				response.Close ();

				var myToppings = JsonConvert.DeserializeObject<List<Toppings>>(responseFromServer);
			
				var t = myToppings.Select(o => o.toppings)
					.Select(ar => ar.OrderBy(s => s).ToArray())
					.Select(mar => string.Join("\r\n", mar) )
					.GroupBy(gr => gr)
					.Select(mrg => new { Rating = mrg.Count(), Toppings = mrg.First()})
					.OrderByDescending(ord => ord.Rating)
					.Select(res => res.Toppings).Take(20).ToArray();
#endregion  

					ListView listView = FindViewById<ListView> (Resource.Id.listView1);
				    ArrayAdapter<string> itemsAdapter = new ArrayAdapter<string>(this, Android.Resource.Layout.SimpleListItem1, t);
				    
				    listView.SetAdapter(itemsAdapter);
				   
			};

		}
		  
	  }

	  
	  public class Toppings
		{
		  public string[] toppings;
		}
	   


	}



