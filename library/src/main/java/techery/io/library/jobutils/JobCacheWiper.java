package techery.io.library.jobutils;

import rx.Observable;
import rx.functions.Action1;
import techery.io.library.jobexecutors.JobExecutor;
import techery.io.library.jobs.Job;

public class JobCacheWiper<T> implements Observable.Transformer<Job<T>, Job<T>> {

    private final JobExecutor<T> jobExecutor;

    public JobCacheWiper(JobExecutor<T> jobExecutor) {
        this.jobExecutor = jobExecutor;
    }

    @Override public Observable<Job<T>> call(Observable<Job<T>> tObservable) {
        return tObservable.doOnNext(new Action1<Job<T>>() {
            @Override
            public void call(Job<T> job) {
                if (job.status != Job.JobStatus.PROGRESS) jobExecutor.clearCache();
            }
        });
    }
}
