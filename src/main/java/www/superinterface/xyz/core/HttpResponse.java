package www.superinterface.xyz.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  HTTP响应对象
 */
public class HttpResponse {

    //private Socket socket = null;
    private OutputStream out = null;

    private int httpResponseCode = 200;
    private String httpResponseMessage = "OK";
    private String httpResponseLine = null;
    private Map<String, String> httpResponseHeads = new HashMap<String, String>();
    //private String httpResponseBody = null;
    private byte[] httpResponseBody = null;

    private HttpResponse() {

    }

    public HttpResponse(OutputStream out) {
        this.out = out;
    }

    public void response() {
        httpResponseHeads.put("Server", "JerryMouse-1.0");
        httpResponseHeads.put("DateTime", new StringBuilder().append(System.currentTimeMillis()).toString());
        try {
            // line
            this.out.write(new StringBuffer().append(ServerEngine.HTTP_VERSION).append(" ").append(this.httpResponseCode).append(" ").append(this.httpResponseMessage).append("\r\n").toString().getBytes());
            // head
            Set<String> keys = httpResponseHeads.keySet();
            for (String s : keys) {
                this.out.write(new StringBuffer().append(s).append(": ").append(httpResponseHeads.get(s)).append("\r\n").toString().getBytes());
            }
            this.out.write("\r\n".getBytes());
            // body
//            this.out.write(this.httpResponseBody == null ? "\r\n".getBytes() : new StringBuffer().append("\r\n").append(this.httpResponseBody).append("\r\n").toString().getBytes());
            this.out.write(httpResponseBody);
            this.out.flush();
            // 根据长短链接的标志来确定是否关闭, this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
//            try{
//                //this.out.flush();
//                //this.out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public String getHttpResponseMessage() {
        return httpResponseMessage;
    }

    public void setHttpResponseMessage(String httpResponseMessage) {
        this.httpResponseMessage = httpResponseMessage;
    }

    public Map<String, String> getHttpResponseHeads() {
        return httpResponseHeads;
    }

    public byte[] getHttpResponseBody() {
        return httpResponseBody;
    }

    public void setHttpResponseBody(byte[] httpResponseBody) {
        this.httpResponseBody = httpResponseBody;
    }

}
