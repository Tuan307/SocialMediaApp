package com.base.app.ui.video_call;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import com.base.app.R;
import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StringeeConnectionListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LifecycleObserver {
    private String to;
    //put your token here
    //private String token = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLk1ydWx4REZoa1NjZVh6RXZscDIzc21ZUnVONDNlSkZGLTE2ODE2MzU1NzciLCJpc3MiOiJTSy4wLk1ydWx4REZoa1NjZVh6RXZscDIzc21ZUnVONDNlSkZGIiwiZXhwIjoxNjg0MjI3NTc3LCJ1c2VySWQiOiJ1c2VyMiJ9.HiuqQgW9F8Q_ksncN5_9ajCddst_bqwZ6wgV83nnPfI";
    private String token = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLk1ydWx4REZoa1NjZVh6RXZscDIzc21ZUnVONDNlSkZGLTE2ODE2MzY3NzMiLCJpc3MiOiJTSy4wLk1ydWx4REZoa1NjZVh6RXZscDIzc21ZUnVONDNlSkZGIiwiZXhwIjoxNjg0MjI4NzczLCJ1c2VySWQiOiJ1c2VyMSJ9.1aWUjMNhdJ-jRf7YTCJ1L9S8qfec0vgx3vkboSRkg1c";
    private EditText etTo;
    private TextView tvUserId;
    private ProgressDialog progressDialog;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video_call);

        tvUserId = findViewById(R.id.tv_userid);

        Button btnVoiceCall = findViewById(R.id.btn_voice_call);
        btnVoiceCall.setOnClickListener(this);
        Button btnVideoCall = findViewById(R.id.btn_video_call);
        btnVideoCall.setOnClickListener(this);
        Button btnVoiceCall2 = findViewById(R.id.btn_voice_call2);
        btnVoiceCall2.setOnClickListener(this);
        Button btnVideoCall2 = findViewById(R.id.btn_video_call2);
        btnVideoCall2.setOnClickListener(this);

        progressDialog = ProgressDialog.show(this, "", "Connecting...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        // register data call back
        launcher = registerForActivityResult(new StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_CANCELED)
                        if (result.getData() != null) {
                            if (result.getData().getAction() != null && result.getData().getAction().equals("open_app_setting")) {
                                Builder builder = new Builder(this);
                                builder.setTitle(R.string.app_name);
                                builder.setMessage("Permissions must be granted for the call");
                                builder.setPositiveButton("Ok", (dialogInterface, id) -> {
                                    dialogInterface.cancel();
                                });
                                builder.setNegativeButton("Settings", (dialogInterface, id) -> {
                                    dialogInterface.cancel();
                                    // open app setting
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                });
                                builder.create().show();
                            }
                        }
                });

        initAndConnectStringee();

    }

    public void initAndConnectStringee() {
        if (Common.client == null) {
            Common.client = new StringeeClient(this);
            // Set host
//            List<SocketAddress> socketAddressList = new ArrayList<>();
//            socketAddressList.add(new SocketAddress("YOUR_IP", YOUR_PORT));
//            client.setHost(socketAddressList);
            Common.client.setConnectionListener(new StringeeConnectionListener() {
                @Override
                public void onConnectionConnected(final StringeeClient stringeeClient, boolean isReconnecting) {
                    runOnUiThread(() -> {
                        Log.d(Common.TAG, "onConnectionConnected");
                        progressDialog.dismiss();
                        tvUserId.setText("Connected as: " + stringeeClient.getUserId());
                        Utils.reportMessage(MainActivity.this, "StringeeClient connected as " + stringeeClient.getUserId());
                    });
                }

                @Override
                public void onConnectionDisconnected(StringeeClient stringeeClient, boolean isReconnecting) {
                    runOnUiThread(() -> {
                        Log.d(Common.TAG, "onConnectionDisconnected");
                        progressDialog.dismiss();
                        tvUserId.setText("Disconnected");
                        Utils.reportMessage(MainActivity.this, "StringeeClient disconnected.");
                    });
                }

                @Override
                public void onIncomingCall(final StringeeCall stringeeCall) {
                    runOnUiThread(() -> {
                        Log.d(Common.TAG, "onIncomingCall: callId - " + stringeeCall.getCallId());
                        if (Common.isInCall) {
                            stringeeCall.reject();
                        } else {
                            Common.callsMap.put(stringeeCall.getCallId(), stringeeCall);
                            Intent intent = new Intent(MainActivity.this, IncomingCallActivity.class);
                            intent.putExtra("call_id", stringeeCall.getCallId());
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onIncomingCall2(StringeeCall2 stringeeCall2) {
                    runOnUiThread(() -> {
                        Log.d(Common.TAG, "onIncomingCall2: callId - " + stringeeCall2.getCallId());
                        if (Common.isInCall) {
                            stringeeCall2.reject();
                        } else {
                            Common.calls2Map.put(stringeeCall2.getCallId(), stringeeCall2);
                            Intent intent = new Intent(MainActivity.this, IncomingCall2Activity.class);
                            intent.putExtra("call_id", stringeeCall2.getCallId());
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onConnectionError(StringeeClient stringeeClient, final StringeeError stringeeError) {
                    runOnUiThread(() -> {
                        Log.d(Common.TAG, "onConnectionError: " + stringeeError.getMessage());
                        progressDialog.dismiss();
                        tvUserId.setText("Connect error: " + stringeeError.getMessage());
                        Utils.reportMessage(MainActivity.this, "StringeeClient fails to connect: " + stringeeError.getMessage());
                    });
                }

                @Override
                public void onRequestNewToken(StringeeClient stringeeClient) {
                    runOnUiThread(() -> Log.d(Common.TAG, "onRequestNewToken"));
                    // Get new token here and connect to Stringe server
                }

                @Override
                public void onCustomMessage(String from, JSONObject msg) {
                    runOnUiThread(() -> Log.d(Common.TAG, "onCustomMessage: from - " + from + " - msg - " + msg));
                }

                @Override
                public void onTopicMessage(String from, JSONObject msg) {

                }
            });
        }
        Common.client.connect(token);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voice_call:
                makeCall(true, false);
                break;
            case R.id.btn_video_call:
                makeCall(true, true);
                break;
            case R.id.btn_voice_call2:
                makeCall(false, false);
                break;
            case R.id.btn_video_call2:
                makeCall(false, true);
                break;
        }
    }

    private void makeCall(boolean isStringeeCall, boolean isVideoCall) {
        to = "user2";
        if (to.trim().length() > 0) {
            if (Common.client.isConnected()) {
                Intent intent;
                if (isStringeeCall) {
                    intent = new Intent(this, OutgoingCallActivity.class);
                } else {
                    intent = new Intent(this, OutgoingCall2Activity.class);
                }
                intent.putExtra("from", Common.client.getUserId());
                intent.putExtra("to", to);
                intent.putExtra("is_video_call", isVideoCall);
                launcher.launch(intent);
            } else {
                Utils.reportMessage(this, "Stringee session not connected");
            }
        }
    }
}
