package sun.bob.pooredit.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sun.bob.pooredit.PoorEdit;
import sun.bob.pooredit.R;
import sun.bob.pooredit.beans.ElementBean;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/11/28.
 */
public class Image extends BaseContainer {

    private BaseImage baseImage;
    private ImageLoaderItf imageLoaderItf;
    private boolean empty = true;
    private String imgPath;
    public Image(Context context) {
        super(context);
    }

    public Image(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {
        baseImage = new BaseImage(getContext());
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.addView(baseImage);
        baseImage.setImageResource(R.drawable.image_file);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFunction(v);
            }
        });
    }

    public Image setImage(String image, int viewWidth){
        if (imageLoaderItf == null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(image, options);
            int width = options.outWidth;
            options = new BitmapFactory.Options();
            options.inScaled = true;
            int fatherWidth = Image.this.getWidth();
            if (fatherWidth == 0){
                fatherWidth = viewWidth;
            }
            options.inSampleSize = width / fatherWidth;
            bmp = BitmapFactory.decodeFile(image, options);
            baseImage.setImageBitmap(bmp);
        } else {
            int fatherWidth = Image.this.getWidth();
            if (fatherWidth == 0){
                fatherWidth = viewWidth;
            }
            imageLoaderItf.loadImage(baseImage, image, fatherWidth);
        }
        empty = false;
        this.setOnClickListener(null);
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onClickFunction(v);
            }
        });
        this.imgPath = image;
        return this;
    }

    public Image setImageLoader(ImageLoaderItf imageLoaderItf) {
        this.imageLoaderItf = imageLoaderItf;
        return this;
    }

    private boolean onClickFunction(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) getContext()).startActivityForResult(i, Constants.REQ_PICK_IMAGE);
        PoorEdit.picking = Image.this;
        return true;
    }

    @Override
    protected void setType() {
        this.type = Constants.TYPE_IMAGE;
    }

    @Override
    public Object getJsonBean() {
        return new ImageBean().setImgPath(imgPath).setWidth(getWidth());
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public void focus() {

    }

    interface ImageLoaderItf {
        void loadImage(ImageView imageView, String image, int imageWidth);
    }

    class BaseImage extends ImageView{

        public BaseImage(Context context) {
            super(context);
            this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.LEFT));
            this.setScaleType(ScaleType.CENTER_INSIDE);
        }
    }

    class ImageBean extends ElementBean{

        private String imgPath;
        private int width;

        public ImageBean(){
            super();
        }

        public String getImgPath() {
            return imgPath;
        }

        public ImageBean setImgPath(String imgPath) {
            this.imgPath = imgPath;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public ImageBean setWidth(int width) {
            this.width = width;
            return this;
        }

        @Override
        public ElementBean setType() {
            this.type = Constants.TYPE_IMAGE;
            return this;
        }
    }
}
