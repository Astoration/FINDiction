package moe.koibito.findiction;

import android.graphics.drawable.Drawable;

/**
 * Created by Astoration on 2016. 9. 3..
 */
public class NaviMenuItem {
    private Drawable iconDrawable;
    private String title;

    public NaviMenuItem(Drawable iconDrawable, String title){
        this.iconDrawable=iconDrawable;
        this.title=title;
    }

    public NaviMenuItem(){

    }

    public void setIconDrawable(Drawable d){
        iconDrawable=d;
    }
    public void setTitle(String t){
        title=t;
    }
    public Drawable getIconDrawable(){
        return iconDrawable;
    }
    public String getTitle(){
        return title;
    }
}
