# EventtusSample

#1- MainActivity : 
   this is Launch Activity and use to login
   with twitter API

#2- After successful login Move to SecondActivity
   this Activity display userName and profile img
   and All followers of this twitter account

  in case of no internet get followers data from database directly and cash all img in memory for
  2 Minutes

#3- when tap on follower cell Move To FollowerInfoActivity to display more info  and get last 10 tweets 

   Cashing tweets in db in case of tapping on followers when internet conneted 
   if tap on followers when no internet connection there are no tweets cashed on database
