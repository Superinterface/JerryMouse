package www.superinterface.xyz.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http 请求对象
 */
public class HttpRequest {

    private String httpMethod = null;
    private String httpUrl = null;
    private String httpVersion;
    private String httpRequestLine = null;
    private Map<String, String> httpRequestHeads = new HashMap<String, String>();
    private StringBuilder httpRequestBody = new StringBuilder();

    private InputStream is = null;

    public HttpRequest() {

    }

    public HttpRequest(InputStream is) {
        this.is = is;
    }

    // 解析请求
    public void parseRequest() {
        // parseHttpRequestLine
        httpRequestLine = readLines(this.is);
        // parseHttpRequestHead
        httpRequestHeads.putAll(readRequestHeads(this.is));
        // parseHttpRequestBody
        httpRequestBody.append(readRequestBody(this.is, httpRequestHeads.get("Content-Length")));
    }

    // 读一行字符串,从输入流中.
    private String readLine(InputStream is) {
        char a = ' ';
        char b = ' ';
        int index = -1;
        StringBuffer sb = new StringBuffer();
        try {
            while ((index = is.read()) != -1) {
                b = (char) index;
                if (a == 13 && b == 10) break;
                a = b;
                sb.append(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }

    // 读取请求行
    private String readLines(InputStream is) {
        String line = readLine(is);
        String[] strlines = line.split(" ");
        if(strlines.length == 3){
            this.httpMethod = strlines[0];
            this.httpUrl = strlines[1];
            this.httpVersion = strlines[2];
        }
        return line;
    }

    // 读取请求头
    private Map<String, String> readRequestHeads(InputStream is) {
        Map<String, String> headMap = new HashMap<String, String>();
        List<String> headList = new ArrayList<String>();
        String str = null;
        while ((str = readLine(is)) != null && !"".equals(str)) {
            headList.add(str);
        }
        for (String s : headList) {
            String[] strs = s.split(": ");
            if (strs.length == 2) headMap.put(strs[0], strs[1]);
        }
        return headMap;
    }

    // 读取请求体
    private String readRequestBody(InputStream is, String length) {
        // 如果请求头里面含有Content-Length,则直接读取对应长度的字节.否则按照-1来判断是否读取完毕所有的数据
        StringBuffer sb = new StringBuffer();
        int index = -1;
        try{
            if(length != null && !"".equals(length)){
                if("0".equals(length)) return null;
                index = Integer.parseInt(length);
                for(int i=0; i<index; i++){
                    sb.append((char)is.read());
                }
            }else{
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return sb.toString().trim();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHttpRequestLine() {
        return httpRequestLine;
    }

    public void setHttpRequestLine(String httpRequestLine) {
        this.httpRequestLine = httpRequestLine;
    }

    public Map<String, String> getHttpRequestHeads() {
        return httpRequestHeads;
    }

    public void setHttpRequestHeads(Map<String, String> httpRequestHeads) {
        this.httpRequestHeads = httpRequestHeads;
    }

    public StringBuilder getHttpRequestBody() {
        return httpRequestBody;
    }

    public void setHttpRequestBody(StringBuilder httpRequestBody) {
        this.httpRequestBody = httpRequestBody;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

}
