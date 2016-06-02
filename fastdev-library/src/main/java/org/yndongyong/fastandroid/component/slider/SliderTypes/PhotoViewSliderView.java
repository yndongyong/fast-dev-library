package org.yndongyong.fastandroid.component.slider.SliderTypes;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.yndongyong.fastandroid.R;

/**
 * Created by Dong on 2016/6/2.
 */
public class PhotoViewSliderView extends BaseSliderView {

    public PhotoViewSliderView(Context context)
    {
        super(context);
    }

    public View getView()
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_photo_view, null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);
        target.setScaleType(ImageView.ScaleType.FIT_CENTER);
        description.setText(getDescription());
        if (TextUtils.isEmpty(getDescription()))
            v.findViewById(R.id.description_layout).setVisibility(8);
        else {
            v.findViewById(R.id.description_layout).setVisibility(0);
        }
        bindEventAndShow(v, target);
        return v;
    }
}
