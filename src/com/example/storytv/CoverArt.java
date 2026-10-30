package com.example.storytv;
import android.content.Context;import android.graphics.*;import java.io.InputStream;
final class CoverArt {
 final Bitmap[] sheets=new Bitmap[7];final Bitmap[] books=new Bitmap[2];
 CoverArt(Context context) throws Exception {for(int i=0;i<7;i++){InputStream in=context.getAssets().open("covers"+i+".png");sheets[i]=BitmapFactory.decodeStream(in);in.close();}String[] paths={"seed/cover.jpg","new/cover.jpg"};for(int i=0;i<2;i++){InputStream in=context.getAssets().open(paths[i]);books[i]=BitmapFactory.decodeStream(in);in.close();}}
 void draw(Canvas c,Paint p,int index,RectF dest){Bitmap b;Rect src;if(index<30){b=sheets[index/10];int n=index%10,w=b.getWidth(),h=b.getHeight();src=new Rect((n%5)*w/5,(n/5)*h/2,(n%5+1)*w/5,(n/5+1)*h/2);}else if(index<33){b=sheets[3];int n=index-30,w=b.getWidth(),h=b.getHeight();src=new Rect(n*w/3,0,(n+1)*w/3,h);}else if(index<53){int item=index-33;b=sheets[4+item/10];int n=item%10,w=b.getWidth(),h=b.getHeight();src=new Rect((n%5)*w/5,(n/5)*h/2,(n%5+1)*w/5,(n/5+1)*h/2);}else if(index<78){b=sheets[6];int n=index-53,w=b.getWidth(),h=b.getHeight();src=new Rect((n%5)*w/5,(n/5)*h/5,(n%5+1)*w/5,(n/5+1)*h/5);}else{b=books[index-78];src=new Rect(0,0,b.getWidth(),b.getHeight());}c.drawBitmap(b,src,dest,p);}
}
