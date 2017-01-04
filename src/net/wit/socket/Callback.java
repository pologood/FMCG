package net.wit.socket;



public interface Callback {
    void onSuccess(String result);

    void onError(Throwable ex, boolean isOnCallback);


    public static class CancelledException extends RuntimeException {
        public CancelledException(String detailMessage) {
            super(detailMessage);
        }
    }
}
