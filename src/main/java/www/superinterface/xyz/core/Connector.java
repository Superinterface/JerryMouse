package www.superinterface.xyz.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class Connector implements Runnable {

    private Socket sc = null;
    private HttpRequest request = null;
    private HttpResponse response = null;
    private HandlerMapping handlerMapping = ServerEngine.getHandlerMapping();
    private Map<String, Handler> mapping = handlerMapping.getReqeustMapping();

    private Connector() {

    }

    public Connector(Socket sc) {
        this.sc = sc;
    }

    /*
     * 读取请求数据,分析请求,处理请求,最终响应.
     */
    public void run() {
        try {
            this.request = new HttpRequest(this.sc.getInputStream());
            this.response = new HttpResponse(this.sc.getOutputStream());
            this.request.parseRequest();
            // 处理逻辑,判断是静态资源请求还是需要业务处理的动态请求
            handler();
            this.response.response();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handler() throws UnsupportedEncodingException {
        // 判断请求类型(静态资源文件,动态业务请求)
        // 静态资源文件,加载文件流,根据文件后缀名设置对应的Content-Type进行响应.
        // 动态请求,则进入RequestMapping查找是否有对应的处理程序,有则进行调用处理,无则响应404.
        String url = this.request.getHttpUrl();
        String suffix = url.contains(".") ? url.substring(url.lastIndexOf("."), url.length()) : null;
        Map<String, String> mimeMapping = ServerEngine.getMimeMap();
        // 是否为静态资源文件
        if (suffix != null && mimeMapping.containsKey(suffix)) {
            // 读取文件流,填充到响应对象的输出流.
            System.out.printf(url);
            this.response.setHttpResponseCode(200);
            this.response.setHttpResponseMessage("ok");
            Map<String, String> heads = this.response.getHttpResponseHeads();
            heads.put("Content-Type", "text/html;charset=utf8");
            this.response.setHttpResponseBody("<h1>静态资源文件获取</h1>".getBytes("utf-8"));
        } else if (mapping.containsKey(url)) { // 查找HandlerMapping是否可以处理请求
            Handler h = mapping.get(url);
            Method m = h.getM();
            Object o = h.getO();
            // v1.0版本将所有方法返回值为String
            Object returnStr = null;
            try {
                Class[] types = m.getParameterTypes();
                if (types.length > 0) { // 处理方法带参数,则先将参数赋值,再调用方法.// v1.0 暂时不处理赋值参数.
//                    Object[] params = new Object[types.length];
//                    for (Object param : params) {
//                    }
                    returnStr = m.invoke(o);
                } else { // 处理方法不带参数
                    returnStr = m.invoke(o);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            this.response.setHttpResponseCode(200);
            this.response.setHttpResponseMessage("ok");
            Map<String, String> heads = this.response.getHttpResponseHeads();
            heads.put("Content-Type", "text/html;charset=utf8");
            this.response.setHttpResponseBody(new String(returnStr.toString()).getBytes("utf-8"));
        } else { // 不能处理当前的请求,响应404.
            this.response.setHttpResponseCode(404);
            this.response.setHttpResponseMessage("not found");
            Map<String, String> heads = this.response.getHttpResponseHeads();
            heads.put("Content-Type", "text/html;charset=utf8");
            this.response.setHttpResponseBody(new StringBuffer().append("<h1>404 not found</h1>").toString().getBytes("utf-8"));
            this.response.response();
        }
    }
}
