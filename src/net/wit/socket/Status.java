package net.wit.socket;

/**
 * Created by linhuasen on 16/1/5.
 */
public class Status {

    /* 正在连接 */
    public final static int CONNECTING = 0;
    /* 连接失败 */
    public final static int CONNECT_FAIL = 1;
    /* 连接成功 */
    public final static int CONNECT_SUCCESS = 2;
    /* 网络异常 */
    public final static int NETWORK_ERROR = 3;
    /* 取消连接 */
    public final static int CONNECT_CANCEL = 4;
    /* 连接完成 */
    public final static int CONNECT_FINISH = 5;
}
