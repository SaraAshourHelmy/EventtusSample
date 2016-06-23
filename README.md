# EventtusSample
 EventtusSample is an application that allow user to login using twitter API and get all followers
 and tap on any follower to get last 10 tweets of them

#Application Features :
 
  - Login with twitter
  - Save user crediential in Shared File
  - Get user Follower and cash them in local Database
  - Refresh Follower list to get last update data
  - Get last 10 tweets of follower and cash them
  - Background image of follower display as sticky header
  - Localization (Arabic and English) by using default device language or by using setting inside application that exist in actinbar menu
  

#Application Flow :

- MainActivity : 
   this is Launch Activity and use to login
   with twitter API

- Move to SecondActivity After login successfully 
 * this Activity display userName and profile img
  and All followers of this twitter account

  * in case of no internet get followers data from database directly and cash all img in memory for
   2 Minutes
  * in this screen can change language from english to arabic or Conversely

- Move To FollowerInfoActivity 
  when tap on follower cell to display more info  and get last 10 tweets .
  Cashing tweets in database in case of tapping on followers when internet conneted ,
  if tap on follower cell when no internet connection there are no tweets cashed on database

#Developed by
    Sara Ashour
  https://www.linkedin.com/in/sara-ashour-4119a7102?trk=hp-identity-name
