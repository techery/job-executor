package techery.io.library;

import rx.Observable;

public class JobCacheWiper<T> implements Observable.Transformer<Job<T>, Job<T>> {

    private final JobExecutor<T> jobExecutor;

    public JobCacheWiper(JobExecutor<T> jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    @Override public Observable<Job<T>> call(Observable<Job<T>> tObservable) {
        return tObservable.doOnNext(job -> {
            if (job.status != Job.JobStatus.PROGRESS) jobExecutor.clearCache();
        });
    }
}
