package net.wit.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class SocketClient {

    private static int CallSig = 0xDA00;
    private static int ResultSig = 0xDB00;
    private static int asError = 0x01;
    private static int asInvoke = 0x02;
    private static int asMask = 0xFF;

    private static int JSONOpenCommandText = 16;
    private static int JSONOpenClassName = 17;
    private static int JSONMessage = 18;

    private static int JSON_FLAG = 1;
    private static int BYTE_FLAG = 0;

    private static int NOT_RETURN = 0;
    private static int NEED_RETURN = 1;

    private static int varEmpty = 0x0000;
    private static int varNull = 0x0001;
    private static int varSmallint = 0x0002;
    private static int varInteger = 0x0003;
    private static int varSingle = 0x0004;
    private static int varDouble = 0x0005;
    private static int varCurrency = 0x0006;
    private static int varDate = 0x0007;
    private static int varOleStr = 0x0008;
    private static int varDispatch = 0x0009;
    private static int varError = 0x000A;
    private static int varBoolean = 0x000B;
    private static int varVariant = 0x000C;
    private static int varUnknown = 0x000D;
    private static int varShortInt = 0x0010;
    private static int varByte = 0x0011;
    private static int varWord = 0x0012;
    private static int varLongWord = 0x0013;
    private static int varInt64 = 0x0014;
    private static int varStrArg = 0x0048;
    private static int varString = 0x0100;
    private static int varAny = 0x0101;
    private static int varTypeMask = 0x0FFF;
    private static int varArray = 0x2000;
    private static int varByRef = 0x4000;

    private int flag = 1;
    private boolean needReturn = true;

    private int timeout = 10 * 3000;
    private Socket socket;
    private OutputStream outStream = null;
    private InputStream inStream = null;

    public static String ip = "";
    public static int port = 0;

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setNeedReturn(boolean needReturn) {
        this.needReturn = needReturn;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout(){
        return this.timeout;
    }

    private byte[] pkg(byte[] body) {

        int signature = CallSig | asInvoke;
        byte[] sign = intToBytes(signature);
        byte[] output = GZipUtils.compress(body);
        byte[] dataLen = intToBytes(body.length);
        byte[] totalLen = intToBytes(output.length + 4);
        byte[] pkg = new byte[12 + output.length];
        System.arraycopy(sign, 0, pkg, 0, 4);/* 0~4 */
        System.arraycopy(totalLen, 0, pkg, 4, 4);
        System.arraycopy(dataLen, 0, pkg, 8, 4);/* 4~8 */
        System.arraycopy(output, 0, pkg, 12, output.length);

        return pkg;
    }

    private byte[] pkgBody(String method, byte[] params) {
        int total = 8 + 8 + 8 + 4 + 2 + params.length;
        byte[] body = new byte[total];

        byte[] intToken = intToBytes(varInteger);
        System.arraycopy(intToken, 0, body, 0, 4);

        byte[] value = new byte[4];
        if (method.equals(Method.SQL)) {
            value = intToBytes(JSONOpenCommandText);
        } else if (method.equals(Method.CLS)) {
            value = intToBytes(JSONOpenClassName);
        } else {
            value = intToBytes(JSONMessage);
        }

        System.arraycopy(value, 0, body, 4, 4);

        System.arraycopy(intToken, 0, body, 8, 4);
        byte[] localId = intToBytes(0);
        System.arraycopy(localId, 0, body, 12, 4);

        System.arraycopy(intToken, 0, body, 16, 4);
        byte[] flagArr = new byte[4];
        if (flag == JSON_FLAG) {
            flagArr = intToBytes(JSON_FLAG);
        } else {
            flagArr = intToBytes(JSON_FLAG);
        }
        System.arraycopy(flagArr, 0, body, 20, 4);

        byte[] byteToken = intToBytes(varBoolean);
        System.arraycopy(byteToken, 0, body, 24, 4);
        if (needReturn) {
            body[28] = 0;
            body[29] = 1;
        } else {
            body[28] = 0;
            body[29] = 0;
        }

        System.arraycopy(params, 0, body, 30, params.length);
        return body;
    }

    private byte[] pkgParams(String str) {
        int total = getStringLength(str) + 4 + 4;
        byte[] params = new byte[total];

        byte[] type = intToBytes(varString);
        System.arraycopy(type, 0, params, 0, 4);

        byte[] len = intToBytes(getStringLength(str));
        System.arraycopy(len, 0, params, 4, 4);
        try {
            System.arraycopy(str.getBytes("GBK"), 0, params, 8, getStringLength(str));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }

    private int getStringLength(String str) {
        if (str == null) {
            return 0;
        }
        int length = 0;
        try {
            length = str.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return length;
    }

    private boolean connect() throws IOException {
        if("".equals(ip) || port <= 0){
            return false;
        }
        return connect(ip, port);
    }

    private boolean connect(String ip, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), timeout);
        outStream = socket.getOutputStream();
        inStream = socket.getInputStream();
        return true;
    }

    private String genJsonString(String method, String request, List<Parameter> params) {
        Parameter param = new Parameter(method, "string", request);
        params.add(0, param);
        Gson gson = new Gson();
        Map<String, List<Parameter>> map = new HashMap<String, List<Parameter>>();
        map.put("params", params);
        return gson.toJson(map);
    }

    private boolean sendRequst(String method, String request, List<Parameter> params) throws IOException {
        String jsonString = genJsonString(method, request, params);
        // 封装数据包
        byte[] pkg = pkg(pkgBody(method, pkgParams(jsonString)));
        // 发送数据
        outStream.write(pkg);
        return true;
    }

    public String reveive() throws IOException {
        // 接收数据包头
        byte[] head = new byte[8];
        int len = inStream.read(head);
 
        byte[] sign = new byte[4];
        byte[] length = new byte[4];
        System.arraycopy(head, 0, sign, 0, 4);
        System.arraycopy(head, 4, length, 0, 4);

        // 读数据体
        byte[] data = new byte[bytesToInt(length, 0)];
        int n = inStream.read(data);

        // byte[] comLen = new byte[4];
        // System.arraycopy(data, 0, comLen, 0, 4);
        // int ll = bytesToInt(comLen, 0);
        // Log.d("lhs", "lllllllllll = " + ll);
        // 取出数据包
        byte[] comData = new byte[bytesToInt(length, 0) - 4];
        System.arraycopy(data, 4, comData, 0, bytesToInt(length, 0) - 4);
        // 解压缩数据
        byte[] deData = GZipUtils.decompress(comData);
        // 去除8字节无效数据，得到最终数据
        byte[] finalData = new byte[deData.length - 8];
        System.arraycopy(deData, 8, finalData, 0, deData.length - 8);

        String result = new String(finalData, "GBK");

        if ((ResultSig | asError) == bytesToInt(sign, 0)) {// 错误的回复或正确的回复
            return build(false, result);
        } else if ((ResultSig | asInvoke) == bytesToInt(sign, 0)) {
            return build(true, result);
        } else {
            return "";
        }

    }

    private String build(boolean isRight, String data) {
        Result result = new Result();
        if (isRight) {
            result.setType("success");
        } else {
            result.setType("error");
        }
        result.setContent(data);
        return new Gson().toJson(result);
    }

    public String request(String method, String request, List<Parameter> params) throws IOException {
        if (!connect()) {
            return "";
        }
        if (!sendRequst(method, request, params)) {
            return "";
        }
        return reveive();

    }

    public void stopRequest() throws IOException {

        if (inStream != null) {
            socket.shutdownInput();
        }
        if (outStream != null) {
            socket.shutdownOutput();
        }
        if (socket != null) {
            socket.close();
        }

    }

    public int writeInt1(byte[] array, int pos, int value) { // 按C字节顺序
        array[pos++] = (byte) (value >>> 0);
        array[pos++] = (byte) (value >>> 8);
        array[pos++] = (byte) (value >>> 16);
        array[pos++] = (byte) (value >>> 24);

        return pos;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24) | ((src[offset + 1] & 0xFF) << 16) | ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
        return value;
    }

    private class Result {
        private String type;
        private String content;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }
}
