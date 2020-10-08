package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.VideoBlock;
import io.intercom.android.sdk.transforms.VideoTransform;
import io.intercom.android.sdk.utilities.BackgroundUtils;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.com.squareup.okhttp.Callback;
import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.Transformation;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Video implements VideoBlock {
    /* access modifiers changed from: private */
    public final Context context;
    private final LayoutInflater inflater;

    public Video(Context context2, StyleType style) {
        this.context = context2;
        this.inflater = LayoutInflater.from(context2);
    }

    public View addVideo(String videoUrl, VideoProvider provider, final String id, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        final ImageView imageView = (ImageView) this.inflater.inflate(R.layout.intercomsdk_blocks_video, parent, false);
        switch (provider) {
            case WISTIA:
                Bridge.getApi().getVideo("http://fast.wistia.com/oembed?url=http://home.wistia.com/medias/" + id, new Callback() {
                    public void onFailure(Request request, IOException e) {
                        Video.this.setFailedImage(imageView);
                    }

                    public void onResponse(Response response) {
                        final String wistiaVideoUrl = "http://fast.wistia.net/embed/iframe/" + id;
                        JSONObject video = new JSONObject();
                        try {
                            JSONObject video2 = new JSONObject(response.body().string());
                            try {
                                response.body().close();
                                video = video2;
                            } catch (IOException e) {
                                video = video2;
                            }
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                            try {
                                response.body().close();
                            } catch (IOException e3) {
                            }
                        } catch (IOException e4) {
                            try {
                                response.body().close();
                            } catch (IOException e5) {
                            }
                        } catch (Throwable th) {
                            try {
                                response.body().close();
                            } catch (IOException e6) {
                            }
                            throw th;
                        }
                        String wistiaThumbnailUrl = video.optString("thumbnail_url");
                        int end = wistiaThumbnailUrl.indexOf("?image_crop_resized");
                        if (end > 0) {
                            wistiaThumbnailUrl = wistiaThumbnailUrl.substring(0, end);
                        }
                        final String finalWistiaThumbnailUrl = wistiaThumbnailUrl;
                        new Handler(Video.this.context.getMainLooper()).post(new Runnable() {
                            public void run() {
                                Video.this.createThumbnail(imageView, wistiaVideoUrl, finalWistiaThumbnailUrl);
                            }
                        });
                    }
                });
                break;
            case YOUTUBE:
                createThumbnail(imageView, "http://www.youtube.com/watch?v=" + id, "http://img.youtube.com/vi/" + id + "/default.jpg");
                break;
            case VIMEO:
                Bridge.getApi().getVideo("http://vimeo.com/api/v2/video/" + id + ".json", new Callback() {
                    public void onFailure(Request request, IOException e) {
                        Video.this.setFailedImage(imageView);
                    }

                    public void onResponse(Response response) {
                        if (response.isSuccessful()) {
                            JSONObject video = new JSONObject();
                            try {
                                video = new JSONArray(response.body().string()).optJSONObject(0);
                                try {
                                    response.body().close();
                                } catch (IOException e) {
                                }
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                                try {
                                    response.body().close();
                                } catch (IOException e3) {
                                }
                            } catch (IOException e4) {
                                try {
                                    response.body().close();
                                } catch (IOException e5) {
                                }
                            } catch (Throwable th) {
                                try {
                                    response.body().close();
                                } catch (IOException e6) {
                                }
                                throw th;
                            }
                            final String vimeoThumbnailUrl = video.optString("thumbnail_large");
                            new Handler(Video.this.context.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    Video.this.createThumbnail(imageView, "http://player.vimeo.com/video/" + id, vimeoThumbnailUrl);
                                }
                            });
                        }
                    }
                });
                break;
        }
        BlockUtils.setLayoutMarginsAndGravity(imageView, 17, isLastObject);
        return imageView;
    }

    /* access modifiers changed from: private */
    public void createThumbnail(ImageView imageView, final String videoUrl, String thumbnailUrl) {
        Picasso.with(this.context).load(thumbnailUrl).placeholder(R.drawable.intercomsdk_video_thumbnail_fallback).error(R.drawable.intercomsdk_video_thumbnail_fallback).transform((Transformation) new VideoTransform()).fit().into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(videoUrl));
                intent.setFlags(268435456);
                Video.this.context.startActivity(intent);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setFailedImage(final ImageView imageView) {
        imageView.post(new Runnable() {
            public void run() {
                BackgroundUtils.setBackground(imageView, Video.this.context.getResources().getDrawable(R.drawable.intercomsdk_video_thumbnail_fallback));
            }
        });
    }
}
