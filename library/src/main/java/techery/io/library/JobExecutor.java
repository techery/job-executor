package techery.io.library;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
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
        return pipeline.asObservable().filter(new Func1<Job<T>, Boolean>() {
            @Override
            public Boolean call(Job<T> job) {
                return job.status == Job.JobStatus.SUCCESS;
            }
        });
    }

    public Observable<Job<T>> connectWithCache() {
        return cachedPipeline.asObservable();
    }

    public Observable<Job<T>> connectWithCacheSuccessOnly() {
        return connectWithCache().filter(new Func1<Job<T>, Boolean>() {
            @Override
            public Boolean call(Job<T> job) {
                return job.status == SUCCESS;
            }
        });
    }

    public void clearCache() {
        createCachedPipeline();
    }

    protected Observable<Job<T>> createInternally(Observable<T> source) {
        return source
                .flatMap(new Func1<T, Observable<Job<T>>>() {
                    @Override
                    public Observable<Job<T>> call(T item) {
                        return Observable.just(Job.<T>builder().status(SUCCESS).value(item).create());
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Job<T> progressSignal = Job.<T>builder().status(PROGRESS).create();
                        pipeline.onNext(progressSignal);
                    }
                })
                .onErrorReturn(new Func1<Throwable, Job<T>>() {
                    @Override
                    public Job<T> call(Throwable e) {
                        return Job.<T>builder().status(FAIL).error(e).create();
                    }
                })
                .doOnNext(new Action1<Job<T>>() {
                    @Override
                    public void call(Job<T> job) {
                        pipeline.onNext(job);
                    }
                });
    }

}
