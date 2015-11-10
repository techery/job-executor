package techery.io.library.jobexecutors;

import rx.Observable;
import rx.functions.Func0;
import techery.io.library.jobs.Job;
import techery.io.library.jobutils.JobToValue;

public class Job0Executor<R> extends JobExecutor<R> {

    private final Func0<Observable<R>> observableFactory;

    public Job0Executor(Func0<Observable<R>> factory) {
        observableFactory = factory;
    }

    public Observable<Job<R>> createJob() {
        return createInternally(Observable.defer(new Func0<Observable<R>>() {
            @Override
            public Observable<R> call() {
                return observableFactory.call();
            }
        }));
    }

    public Observable<R> createPlainJob() {
        return createJob().compose(new JobToValue<R>());
    }

    public void executeJob() {
        createJob().subscribe();
    }
}