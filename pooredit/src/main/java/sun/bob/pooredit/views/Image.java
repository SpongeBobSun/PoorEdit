package sun.bob.pooredit.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sun.bob.pooredit.PoorEdit;
import sun.bob.pooredit.R;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/11/28.
 */
public class Image extends BaseContainer {

    private BaseImage baseImage;
    private ImageLoaderItf imageLoaderItf;
    public Image(Context context) {
        super(context);
    }

    public Image(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {
        baseImage = new BaseImage(getContext());
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.addView(baseImage);
        baseImage.setImageResource(R.drawable.image_file);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ((Activity) getContext()).startActivityForResult(i, Constants.REQ_PICK_IMAGE);
                PoorEdit.picking = Image.this;
            }
        });
    }

    public Image setImage(String image){
        if (imageLoaderItf == null){
            baseImage.setImageBitmap(BitmapFactory.decodeFile(image));
        } else {
            imageLoaderItf.loadImage(image);
        }
        return this;
    }

    public Image setImageLoader(ImageLoaderItf imageLoaderItf) {
        this.imageLoaderItf = imageLoaderItf;
        return this;
    }

    @Override
    protected void setType() {
        this.type = Constants.TYPE_IMAGE;
    }

    @Override
    public Object getJsonBean() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    interface ImageLoaderItf {
        void loadImage(String image);
    }

    class BaseImage extends ImageView{

        public BaseImage(Context context) {
            super(context);
        }
    }
}
