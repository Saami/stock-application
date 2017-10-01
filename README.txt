To Compile and Start the Server:
in the project directory run: ./gradlew bootrun
the application will run on localhost:8080

API details:
URL: http://localhost:8080/bestTrade/{ticker}?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
PathParam: {ticker}. This is the stock symbol, it is case insensitive.
QueryParams:
 startDate. format yyyy-MM-dd. If no startDate is provided, default is set to 2017-01-01.
 endDate. format yyyy-MM-dd. If no endDate is provided, defaults to todays date.
Sample Url:http://localhost:8080/bestTrade/nflx?startDate=2017-01-05&endDate=2017-03-10
Sample Return: ["2017-01-12","2017-02-10"]

More About the Web Services-
This web service utilizes the Intrinio U.S.Public Company Data Feed. It Utilizes it’s Historical Data Api to get stock prices for stocks at the close of market for a particular day. For further reference - http://docs.intrinio.com/#historical-data

Iterative Improvements To Code (wishlist) -
Add caching. 
Wrap api call going to external service (Intrinio) using Hystrix so that we don’t keep hitting the api url in case it’s down.


