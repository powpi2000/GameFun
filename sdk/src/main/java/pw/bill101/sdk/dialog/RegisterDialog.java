package pw.bill101.sdk.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import BaseDialog;

import pw.bill101.sdk.listener.LoginListener;
import pw.bill101.sdk.utils.FormVerifyUtils;
import pw.bill101.sdk.utils.ResourceUtils;

/**
 * Created by powpi2000 on 2017/2/10.
 */

public class RegisterDialog extends BaseDialog {

    private LoginListener mLoginListener;

    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtCfmPwd;

    public RegisterDialog(Context context, LoginListener listener) {
        super(context);
        mLoginListener = listener;
        setContentView(ResourceUtils.getLayoutId(context, "pj_dialog_register"));

        Button btnLogin = (Button) findViewById(ResourceUtils.getId(context, "pj_btn_login"));
        btnLogin.setOnClickListener(mLoginClickListener);
        Button btnReg = (Button) findViewById(ResourceUtils.getId(context, "pj_reg_button"));
        btnReg.setOnClickListener(mRegClickListener);

        mEtUsername = (EditText) findViewById(ResourceUtils.getId(context, "pj_login_username_edittext"));
        mEtPassword = (EditText) findViewById(ResourceUtils.getId(context, "pj_login_password_editText"));
        mEtCfmPwd = (EditText) findViewById(ResourceUtils.getId(context, "pj_cfm_password_editText"));
    }

    private View.OnClickListener mLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LoginDialog dialog = new LoginDialog(mContext, mLoginListener);
            dialog.show();

            dismiss();
        }
    };

    private View.OnClickListener mRegClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = mEtUsername.getText().toString();
            String password = mEtPassword.getText().toString();
            String cfmPwd = mEtCfmPwd.getText().toString();

            if (!FormVerifyUtils.checkUserName(username)) {
                Toast.makeText(mContext, "请输入6-16位用户名", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!FormVerifyUtils.checkPassword(password)) {
                Toast.makeText(mContext, "请输入6-30位密码", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!TextUtils.equals(cfmPwd, password)) {
                Toast.makeText(mContext, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }

            doReg(username, password);
        }
    };

    private void doReg(String username, String password) {
        
        Map<String, String> params = new HashMap<>();
        params.put("userName", username);
        params.put("password", password);
        LoginTask task = new LoginTask(mContext, Api.REG, params, mLoginListener, this);
        task.start();
    }
}
