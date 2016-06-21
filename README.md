# EventtusSample

#Application Features :
 
  - Login with twitter
  - Save user crediential in Shared File
  - Get user Follower and cash them in local Database
  - Refresh Follower list to get last update data
  - Get last 10 tweets of follower and cash them
  - Background image of follower display as sticky header
  - Localization (Arabic and English)
  - 
#Application Flow :

#1- MainActivity : 
   this is Launch Activity and use to login
   with twitter API

#2- After successful login Move to SecondActivity
   this Activity display userName and profile img
   and All followers of this twitter account

  in case of no internet get followers data from database directly and cash all img in memory for
  2 Minutes

#3- Move To FollowerInfoActivity 
    when tap on follower cell to display more info  and get last 10 tweets .
    Cashing tweets in database in case of tapping on followers when internet conneted 
    if tap on follower cell when no internet connection there are no tweets cashed on database
