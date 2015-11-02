package techery.io.library;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.subjects.PublishSubject;

import static techery.io.library.Job.JobStatus.FAIL;
import static techery.io.library.Job.JobStatus.PROGRESS;
import static techery.io.library.Job.JobStatus.SUCCESS;

public abstract class JobExecutor<T> {

    private final PublishSubject<Job<T>> pipeline;
    private ConnectableObservable<Job<T>> cachedPipeline;

    public JobExecutor() {
        this.pipeline = PublishSubject.create();
        createCachedPipeline();
    }

    private void createCachedPipeline() {
        this.cachedPipeline = pipeline.replay(1);
        this.cachedPipeline.connect();
    }

    public Observable<Job<T>> connect() {
        return pipeline.asObservable();
    }

    public Observable<Job<T>> connectSuccessOnly() {
        return pipeline.asObservable().filter(job -> job.status == Job.JobStatus.SUCCESS);
    }

    public Observable<Job<T>> connectWithCache() {
        return cachedPipeline.asObservable();
    }

    public Observable<Job<T>> connectWithCacheSuccessOnly() {
        return connectWithCache().filter(job -> job.status == SUCCESS);
    }

    public void clearCache() {
        createCachedPipeline();
    }

    protected Observable<Job<T>> createInternally(Observable<T> source) {
        return source
                .flatMap(item -> Observable.just(Job.<T>builder().status(SUCCESS).value(item).create()))
                .doOnSubscribe(() -> {
                    Job<T> progressSignal = Job.<T>builder().status(PROGRESS).create();
                    pipeline.onNext(progressSignal);
                })
                .onErrorReturn(e -> {
                    return Job.<T>builder().status(FAIL).error(e).create();
                })
                .doOnNext(job -> {
                    pipeline.onNext(job);
                });
    }

}
