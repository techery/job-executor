package techery.io.library;

import rx.Observable;

public class JobToValue<T> implements Observable.Transformer<Job<T>, T> {
    @Override public Observable<T> call(Observable<Job<T>> jobObservable) {
        return jobObservable.flatMap(job -> {
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
        });
    }
}