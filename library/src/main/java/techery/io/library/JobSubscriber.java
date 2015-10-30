package techery.io.library;

import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class JobSubscriber<T> extends Subscriber<Job<T>> {

    private Action1<T> onSuccess;
    private Action1<Throwable> onError;
    private Action0 onProgress;
    private Action1<Job<T>> beforeEach;
    private Action1<Job<T>> afterEach;

    public JobSubscriber() {
    }

    public JobSubscriber<T> onSuccess(Action1<T> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public JobSubscriber<T> onError(Action1<Throwable> onError) {
        this.onError = onError;
        return this;
    }

    public JobSubscriber<T> onProgress(Action0 onProgress) {
        this.onProgress = onProgress;
        return this;
    }

    public JobSubscriber<T> beforeEach(Action1<Job<T>> onEach) {
        this.beforeEach = onEach;
        return this;
    }

    public JobSubscriber<T> afterEach(Action1<Job<T>> afterEach) {
        this.afterEach = afterEach;
        return this;
    }

    @Override public void onNext(Job<T> job) {
        if (beforeEach != null) beforeEach.call(job);
        switch (job.status) {
            case PROGRESS:
                if (onProgress != null) onProgress.call();
                break;
            case SUCCESS:
                if (onSuccess != null) onSuccess.call(job.value);
                break;
            case FAIL:
                if (onError != null) onError.call(job.error);
                break;
        }
        if (afterEach != null) afterEach.call(job);
    }

    @Override public void onCompleted() { }

    @Override public void onError(Throwable e) {
        if (onError != null) onError.call(e);
    }
}
