package com.udacity.and.popularmovies.ui;

public class AsyncTaskResult<T> {
    private T result;
    private Exception exception;

    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }
    public void setException(Exception exception) {
        this.exception = exception;
    }
}
