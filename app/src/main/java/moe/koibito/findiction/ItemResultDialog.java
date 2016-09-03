package moe.koibito.findiction;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Astoration on 2016. 9. 4..
 */
public class ItemResultDialog extends Dialog {
    private Button mAcceptButton;
    private Button mCancelButton;
    private TextView mItemDescription;
    private TextView mItemTitle;
    private ImageView mItemImage;
    private String mTitle;
    private String mDescription;
    private String mImage;

    public ItemResultDialog(Context context, String image, String title, String description){
        super(context);
        mTitle=title;
        mDescription=description;
        mImage = image;
    }

    public ItemResultDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.result_dialog);
        mAcceptButton = (Button) findViewById(R.id.dialog_accept);
        mCancelButton = (Button) findViewById(R.id.dialog_cancel);
        mItemTitle = (TextView) findViewById(R.id.item_title);
        mItemDescription = (TextView) findViewById(R.id.item_description);
        mItemImage = (ImageView) findViewById(R.id.dialog_image);
        mItemTitle.setText(mTitle);
        mItemDescription.setText(mDescription);
        if(!mImage.equals("")){
            Glide.with(getContext()).load(mImage).into(mItemImage);
        }

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
