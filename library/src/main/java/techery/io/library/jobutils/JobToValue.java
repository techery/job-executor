package techery.io.library.jobutils;

import rx.Observable;
import rx.functions.Func1;
import techery.io.library.jobs.Job;

public class JobToValue<T> implements Observable.Transformer<Job<T>, T> {
    @Override public Observable<T> call(Observable<Job<T>> jobObservable) {
        return jobObservable.flatMap(new Func1<Job<T>, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> call(Job<T> job) {
                switch (job.status) {
                    case PROGRESS:
                        return Observable.never();
                    case SUCCESS:
                        return Observable.just(job.value);
                    case FAIL:
                        return Observable.error(job.error);
                    default:
                        throw new IllegalArgumentException("Job status is unknown");
                }
            }
        });
    }
}