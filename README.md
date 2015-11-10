# Job-executor 
Job-Executor is pretty simple library based on javaRX, which helps you to create code more cleaner and leaner using reactive programmer paradigm. 
# What's this for?
It allows you to represent the sequence of actions as a single job, which executes in a job executors without having to write a lot of code unlike in common javaRx stream.
You can combine job executor whatever you want! Especially it's useful for multistep validation process. So just use your imagination)). And for easy debugging, the result of processing data is output on a console.
# How to use?
There must be sample
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