# Job-executor 
Job-Executor is pretty simple library based on javaRX, which helps you to create code cleaner and leaner using reactive programmer paradigm. 
# What's this for?
It allows you to represent the sequence of actions as a single job, which executes in a job executors without having to write a lot of code unlike in common javaRx stream.
You can combine job executor whatever you want! Especially it's useful for multistep validation process. So just use your imagination)). And for easy debugging, the result of processing data is output on a console.
# How to use?
```groovy

    emailValidator = new Job1Executor<>(new Func1<String, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(String email) {
            return Observable.just(!TextUtils.isEmpty(email));
        }
    });

    passwordValidator = new Job1Executor<>(new Func1<String, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(String password) {
            boolean passwordValid = s != null && s.length() > 4;
            return Observable.just(passwordValid);
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
        
    ....

    api = new Api();
    validators = new Validators();
    
    ....

    void attemptLogin(String email, String password){
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
    
```
Or with Java8
    
```groovy

    emailValidator = new Job1Executor<>(email -> Observable.just(!TextUtils.isEmpty(email)));
    
    passwordValidator = new Job1Executor(password -> {
        boolean passwordValid = password != null && password.length() > 4;
        return Observable.just(passwordValid);
    });
    
    userInfoValidator = new Job2Executor((email, password) ->{
        return Observable.combineLatest(emailValidator.createPlainJobWith(email), 
                                        passwordValidator.createPlainJobWith(password),
                                        (emailValid, passwordValid) -> emailValid && passwordValid);
        
    ....
 
     api = new Api();
     validators = new Validators();
     
     ....
 
     void attemptLogin(String email, String password){
        validators.userInfoValidator.createPlainJobWith(email, password)
                        .filter(valid -> valid)
                        .subscribe(valid -> api.login());
     }
     
```   
    
# Installation
Use jitpack.io

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.1.3'
        classpath 'com.github.dcendents:android-maven-plugin:1.2'
    }
}
...
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'
...
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.techery:job-executor:0.1.1'
    ...
}
```