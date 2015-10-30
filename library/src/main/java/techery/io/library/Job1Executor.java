package techery.io.library;

import rx.Observable;
import rx.functions.Func1;

public class Job1Executor<T, R> extends JobExecutor<R> {

    private final Func1<T, Observable<R>> observableFactory;

    public Job1Executor(Func1<T, Observable<R>> factory) {
        observableFactory = factory;
    }

    public Observable<Job<R>> createJobWith(T arg) {
        return createInternally(Observable.defer(() -> observableFactory.call(arg)));
    }

    public Observable<R> createPlainJobWith(T arg) {
        return createJobWith(arg).compose(new JobToValue<>());
    }

    public void executeJobWith(T arg) {
        createJobWith(arg).subscribe();
    }
}
