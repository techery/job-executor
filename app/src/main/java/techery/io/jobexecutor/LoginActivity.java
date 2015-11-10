package techery.io.jobexecutor;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import rx.functions.Action1;
import rx.functions.Func1;



public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private Api api;
    private Validators validators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button signInButton = (Button) findViewById(R.id.email_sign_in_button);

        api = new Api();
        validators = new Validators();
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin(){
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        validators.userInfoValidator.createPlainJobWith(email, password)
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return aBoolean;
                    }
                }).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.e("Login", "true");
                        api.login();
                    }
        });
    }

}

