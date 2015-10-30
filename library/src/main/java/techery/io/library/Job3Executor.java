package techery.io.library;

import rx.Observable;
import rx.functions.Func3;

public class Job3Executor<T1, T2, T3, R> extends JobExecutor<R> {

    private final Func3<T1, T2, T3, Observable<R>> observableFactory;

    public Job3Executor(Func3<T1, T2, T3, Observable<R>> factory) {
        observableFactory = factory;
    }

    public Observable<Job<R>> createJobWith(T1 arg1, T2 arg2, T3 arg3) {
        return createInternally(Observable.defer(() -> observableFactory.call(arg1, arg2, arg3)));
    }

    public Observable<R> createPlainJobWith(T1 arg, T2 arg2, T3 arg3) {
        return createJobWith(arg, arg2, arg3).compose(new JobToValue<>());
    }

    public void executeJobWith(T1 arg1, T2 arg2, T3 arg3) {
        createJobWith(arg1, arg2, arg3).subscribe();
    }
}
