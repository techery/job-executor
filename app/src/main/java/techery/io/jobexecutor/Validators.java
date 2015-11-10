package techery.io.jobexecutor;

import android.text.TextUtils;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import techery.io.library.jobexecutors.Job1Executor;
import techery.io.library.jobexecutors.Job2Executor;

public class Validators {

    private final Job1Executor<String, Boolean> emailValidator;
    private final Job1Executor<String, Boolean> passwordValidator;

    public final Job2Executor<String, String, Boolean> userInfoValidator;

    public Validators() {

        emailValidator = new Job1Executor<>(new Func1<String, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(String email) {
                return Observable.just(!TextUtils.isEmpty(email));
            }
        });

        passwordValidator = new Job1Executor<>(new Func1<String, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(String s) {
                boolean emailValid = s != null && s.length() > 4;
                return Observable.just(emailValid);
            }
        });

        userInfoValidator = new Job2Executor<>(new Func2<String, String, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(String email, String password) {
                return Observable.combineLatest(emailValidator.createPlainJobWith(email), passwordValidator.createPlainJobWith(password),
                        new Func2<Boolean, Boolean, Boolean>() {
                            @Override
                            public Boolean call(Boolean emailValid, Boolean passwordValid) {
                                return emailValid && passwordValid;
                            }
                        });
            }
        });
    }
}
