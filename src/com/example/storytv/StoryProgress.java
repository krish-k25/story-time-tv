package com.example.storytv;
import android.content.Context;import android.content.SharedPreferences;import java.util.Calendar;import java.util.GregorianCalendar;import java.util.TimeZone;
final class StoryProgress {
 private static SharedPreferences prefs(Context c){return c.getSharedPreferences("story_progress",Context.MODE_PRIVATE);}
 private static long today(){Calendar local=Calendar.getInstance();Calendar utc=new GregorianCalendar(TimeZone.getTimeZone("UTC"));utc.clear();utc.set(local.get(Calendar.YEAR),local.get(Calendar.MONTH),local.get(Calendar.DAY_OF_MONTH));return utc.getTimeInMillis()/86400000L;}
 static void complete(Context c,String id){SharedPreferences p=prefs(c);long last=p.getLong("last_read_day",Long.MIN_VALUE),now=today();int count=StreakLogic.next(last,p.getInt("reading_streak",0),now);SharedPreferences.Editor e=p.edit().putLong("last_read_day",now).putInt("reading_streak",count);if(!p.getBoolean(id,false))e.putBoolean(id,true);e.apply();StreakChannel.refreshAsync(c);}
 static boolean isComplete(Context c,String id){return prefs(c).getBoolean(id,false);}
 static int countLanguage(Context c,org.json.JSONArray stories,boolean tamil){int n=0;for(int i=0;i<stories.length();i++){org.json.JSONObject story=stories.optJSONObject(i);if(story!=null&&tamil=="ta".equals(story.optString("language"))&&isComplete(c,"story:"+story.optString("title")))n++;}if(!tamil){if(isComplete(c,"book:0"))n++;if(isComplete(c,"book:1"))n++;}return n;}
 static int count(Context c){int n=0;for(Object value:prefs(c).getAll().values())if(Boolean.TRUE.equals(value))n++;return n;}
 static int streak(Context c){SharedPreferences p=prefs(c);return StreakLogic.visible(p.getLong("last_read_day",Long.MIN_VALUE),p.getInt("reading_streak",0),today());}
}
