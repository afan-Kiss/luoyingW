package com.hjq.demo.mine_chenmo.chatui.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hjq.demo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GlideUtils {

 	public static void loadChatImage(final Context mContext, String imgUrl,final ImageView imageView) {



	final RequestOptions options = new RequestOptions()
				.placeholder(R.mipmap.icon_user_defalt_chat)// 正在加载中的图片
				.error(R.mipmap.default_img_failed); // 加载失败的图片

		Glide.with(mContext)
				.load(imgUrl) // 图片地址
				.apply(options)
				.into(new SimpleTarget<Drawable>() {
					@Override
					public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
						ImageSize imageSize = BitmapUtil.getImageSize(((BitmapDrawable)resource).getBitmap() );
						RelativeLayout.LayoutParams imageLP =(RelativeLayout.LayoutParams )(imageView.getLayoutParams());
						imageLP.width = imageSize.getWidth();
						imageLP.height = imageSize.getHeight();
						imageView.setLayoutParams(imageLP);

						Glide.with(mContext)
								.load(resource)
								.apply(options) // 参数
								.into(imageView);
					}
				});
 	}

	public static void loadRoundedCornersImage(final Context mContext, String imgUrl,final ImageView imageView){
		//设置图片圆角角度
		RoundedCorners roundedCorners= new RoundedCorners(20);
		//通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
		RequestOptions options=RequestOptions.placeholderOf(R.mipmap.icon_user_defalt_chat)
				.error(R.mipmap.default_img_failed).bitmapTransform(roundedCorners).override(300, 300);

		Glide.with(mContext).load(imgUrl).apply(options).into(imageView);

	}

	public static void showImage(final ImageView imageView){
		imageView.setVisibility(View.VISIBLE);
	}
	public static void dismissImage(final ImageView imageView){
		imageView.setVisibility(View.INVISIBLE);
	}



}
