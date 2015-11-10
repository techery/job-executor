package techery.io.library.jobs;

public class Job<T> {
    public enum JobStatus {
        PROGRESS, SUCCESS, FAIL
    }

    public final JobStatus status;
    public final T value;
    public final Throwable error;

    private Job(JobStatus status, T value, Throwable error) {
        this.status = status;
        this.value = value;
        this.error = error;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @Override public String toString() {
        return "Job{" +
                "status=" + status +
                ", value=" + value +
                ", error=" + error +
                '}';
    }

    public static class Builder<T> {

        private JobStatus status;
        private T value;
        private Throwable error;

        public Builder() {
        }

        public Builder<T> status(JobStatus status) {
            this.status = status;
            return this;
        }

        public Builder<T> value(T value) {
            this.value = value;
            return this;
        }

        public Builder<T> error(Throwable error) {
            this.error = error;
            return this;
        }

        public Job<T> create() {
            if (status == null) {
                throw new IllegalArgumentException("Status must not be null");
            }
            return new Job<>(status, value, error);
        }
    }
}
