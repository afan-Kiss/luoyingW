package com.hjq.demo.rong;

import android.util.Log;
import android.view.SurfaceView;

import java.util.HashMap;

import io.rong.calllib.IRongCallListener;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;

public class RongListener implements IRongCallListener {
    private static final String TAG = "RongListener";
    @Override
    public void onCallOutgoing(RongCallSession rongCallSession, SurfaceView surfaceView) {
        Log.i(TAG, "onCallOutgoing: ");
    }

    @Override
    public void onCallConnected(RongCallSession rongCallSession, SurfaceView surfaceView) {
        Log.i(TAG, "onCallConnected: ");
    }

    @Override
    public void onCallDisconnected(RongCallSession rongCallSession, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onRemoteUserRinging(String s) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onRemoteUserJoined(String s, RongCallCommon.CallMediaType callMediaType, int i, SurfaceView surfaceView) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onRemoteUserInvited(String s, RongCallCommon.CallMediaType callMediaType) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onRemoteUserLeft(String s, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onMediaTypeChanged(String s, RongCallCommon.CallMediaType callMediaType, SurfaceView surfaceView) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onError(RongCallCommon.CallErrorCode callErrorCode) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onRemoteCameraDisabled(String s, boolean b) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onRemoteMicrophoneDisabled(String s, boolean b) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onNetworkReceiveLost(String s, int i) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onNetworkSendLost(int i, int i1) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onFirstRemoteVideoFrame(String s, int i, int i1) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onAudioLevelSend(String s) {
        Log.i(TAG, "onCallConnected: ");

    }

    @Override
    public void onAudioLevelReceive(HashMap<String, String> hashMap) {
        Log.i(TAG, "onCallConnected: ");

    }
}
