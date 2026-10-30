package com.example.storytv;
import android.animation.ValueAnimator;import android.graphics.*;import android.os.Build;import android.os.SystemClock;
final class StoryMotion {
 static boolean enabled(){return Build.VERSION.SDK_INT<26||ValueAnimator.areAnimatorsEnabled();}
 static float phase(){return enabled()?SystemClock.uptimeMillis()/1000f:0f;}
 static int focusColor(float phase){float u=(float)((Math.sin(phase*2)+1)*0.5);return Color.rgb((int)(40+16*u),(int)(78+28*u),(int)(84+23*u));}
 static void sparkle(Canvas c,Paint p,float x,float y,float radius,int color){p.setColor(color);Path path=new Path();path.moveTo(x,y-radius);path.lineTo(x+radius*.27f,y-radius*.27f);path.lineTo(x+radius,y);path.lineTo(x+radius*.27f,y+radius*.27f);path.lineTo(x,y+radius);path.lineTo(x-radius*.27f,y+radius*.27f);path.lineTo(x-radius,y);path.lineTo(x-radius*.27f,y-radius*.27f);path.close();c.drawPath(path,p);}
 static void scene(Canvas c,Paint p,float time){for(int i=0;i<7;i++){float x=150+i*155,y=115+(i%3)*34;float r=4+(float)(Math.sin(time*1.8+i)*1.5);sparkle(c,p,x,y,r,0xbbefffdf);}}
}
