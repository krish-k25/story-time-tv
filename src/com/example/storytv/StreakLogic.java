package com.example.storytv;
final class StreakLogic {
 static int next(long last,int current,long today){if(last==today)return current;if(last==today-1)return current+1;return 1;}
 static int visible(long last,int current,long today){return last==today||last==today-1?current:0;}
}
