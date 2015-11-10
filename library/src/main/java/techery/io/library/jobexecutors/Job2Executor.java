package techery.io.library.jobexecutors;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func2;
import techery.io.library.jobs.Job;
import techery.io.library.jobutils.JobToValue;

public class Job2Executor<T1, T2, R> extends JobExecutor<R> {

    private final Func2<T1, T2, Observable<R>> observableFactory;

    public Job2Executor(Func2<T1, T2, Observable<R>> factory) {
        observableFactory = factory;
    }

    public Observable<Job<R>> createJobWith(final T1 arg1, final T2 arg2) {
        return createInternally(Observable.defer(new Func0<Observable<R>>() {
            @Override
            public Observable<R> call() {
                return observableFactory.call(arg1, arg2);
            }
        }));
    }

    public Observable<R> createPlainJobWith(T1 arg, T2 arg2) {
        return createJobWith(arg, arg2).compose(new JobToValue<R>());
    }

    public void executeJobWith(T1 arg1, T2 arg2) {
        createJobWith(arg1, arg2).subscribe();
    }
}