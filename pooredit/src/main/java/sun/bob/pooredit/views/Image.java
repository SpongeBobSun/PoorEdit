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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sun.bob.pooredit.PoorEdit;
import sun.bob.pooredit.R;
import sun.bob.pooredit.beans.ElementBean;
import sun.bob.pooredit.drawables.BackgroundBorder;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/11/28.
 */
public class Image extends BaseContainer {

    private BaseImage baseImage;
    private ImageLoaderItf imageLoaderItf;
    private boolean empty = true;
    private String imgPath;
    private LinearLayout container;
    private TextView hintText;
    private ImageView hintImg;
    public Image(Context context) {
        super(context);
    }

    public Image(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 5, 0, 5);
        container.setLayoutParams(layoutParams);
        hintText = new TextView(getContext());
        hintText.setText(" Click to select image.");
        hintText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));
        hintText.setGravity(Gravity.CENTER_VERTICAL);

        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        hintImg = new ImageView(getContext());
        hintImg.setImageResource(R.drawable.image_file);
        hintImg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(hintImg);
        container.addView(hintText);
        this.addView(container);

//        this.addView(baseImage);
        baseImage = new BaseImage(getContext());
        this.setBackground(BackgroundBorder.getInstance());
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFunction(v);
            }
        });
        this.setImageLoader(PoorEdit.imageLoaderItf);
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
            this.removeView(container);
            this.addView(baseImage);
            this.setBackground(null);
        } else {
            int fatherWidth = Image.this.getWidth();
            if (fatherWidth == 0){
                fatherWidth = viewWidth;
            }
            this.removeView(container);
            this.addView(baseImage);
            this.setBackground(null);
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

    public interface ImageLoaderItf {
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
