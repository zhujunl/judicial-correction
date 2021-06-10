package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogPlayVoiceBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;


/**
 * 播放音频
 */
public class PlayVoiceDialog extends BaseDialog<DialogPlayVoiceBinding, PlayVoiceDialog.ClickListener> {

    private final Builder mBuilder;

    private MediaPlayer mPlayer;

    public PlayVoiceDialog(@NonNull Context context, ClickListener clickListener, @NonNull Builder builder) {
        super(context, clickListener);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        this.mBuilder = builder;
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_play_voice;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        init();
        binding.btnRetry.setOnClickListener(v -> {
            if (listener != null) {
                if (isPlay()) {
                    stop();
                    binding.btnRetry.setText("播放");
                } else {
                    play();
                    binding.btnRetry.setText("停止");
                }
            }
        });
        binding.btnTryAgain.setOnClickListener(v -> {
            release();
            if (listener != null) {
                dismiss();
                listener.onDetermine();
            }
        });
        if (mPlayer!=null) {
            mPlayer.setOnCompletionListener(mp -> {
                binding.btnRetry.setText("播放");
            });
        }
    }

    @Override
    public void dismiss() {
        release();
        super.dismiss();

    }

    @Override
    public void cancel() {
        release();
        super.cancel();
    }

    private void init() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mBuilder.filePath);
            mPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    private void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }


    private void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    private boolean isPlay() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    private void release() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.setOnCompletionListener(null);
            mPlayer.release();
            mPlayer = null;
        }
    }

    public interface ClickListener extends BaseDialogListener {

        void onDetermine();
    }

    public static class Builder {

        public String filePath;

        public Builder() {
        }

        public Builder setPathFile(String filePath) {
            this.filePath = filePath;
            return this;
        }

    }

}
