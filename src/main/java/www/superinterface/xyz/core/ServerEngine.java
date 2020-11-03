package www.superinterface.xyz.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 基于ServerSocket的仿Tomcat的服务引擎
 * author: k
 * version: V1.0
 */
public class ServerEngine {

    private static ServerSocket ss = null;
    private static int port = 9090; // 默认9090端口
    public static String HTTP_VERSION = "HTTP/1.1";
    // mimeMap
    private static Map<String, String> MIME_MAP = new HashMap<String, String>();
    // handlerMapping
    private static HandlerMapping HANDLER_MAPPING = new HandlerMapping();

    static {
        // 加载配置
        initConfiguration();
        // 扫描指定包路径下的 Controller 注解的类
        loadRequestMapping();
        // 初始化Server引擎
        //initServerEngine();
    }

    // 初始化服务引擎开始监听端口,并对发送过来的数据进行处理响应.
    public void startServerEngine() {
        try {
            System.out.println("=====启动ServerEngine开始");// TODO 打标记,后面替换日志打印方式.
            ss = new ServerSocket(port);
            while (true) {
                System.out.println("=====等待一个客户端连接");// TODO 打标记,后面替换日志打印方式.
                new Thread(new Connector(ss.accept())).start();
                System.out.println("=====处理客户端连接完毕");// TODO 打标记,后面替换日志打印方式.
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadRequestMapping() {
        SAXReader saxreader = new SAXReader();
        InputStream is = ServerEngine.class.getClassLoader().getResourceAsStream("controller.xml");
        List<Object> beans = new ArrayList<Object>();
        try {
            Document document = saxreader.read(is);
            Element root = document.getRootElement();
            List<Element> elements = root.elements("controller");
            for (Element e : elements) {
                String packagePath = e.attributeValue("class");
                Object bean = Class.forName(packagePath).newInstance();
                beans.add(bean);
            }
            HANDLER_MAPPING = new HandlerMapping();
            HANDLER_MAPPING.process(beans);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 从配置文件读取数据(端口....等)
    private static void initConfiguration() {
        System.out.println("=====初始化ServerEngine配置开始");// TODO 打标记,后面替换日志打印方式.
        readMimeMap();
        System.out.println("=====初始化ServerEngine配置完毕");// TODO 打标记,后面替换日志打印方式.
    }

    private static void readMimeMap() {
        // mime.type
        SAXReader saxreader = new SAXReader();
        InputStream is = ServerEngine.class.getClassLoader().getResourceAsStream("web.xml");
        try {
            // 解析配置文件
            Document doc = saxreader.read(is);
            // 根节点
            Element rootEle = doc.getRootElement();
            List<Element> elements = rootEle.elements("mime-mapping");
            for (Element e : elements) {
                MIME_MAP.put(e.element("extension").getText(), e.element("mime-type").getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> getMimeMap() {
        return MIME_MAP;
    }

    public static HandlerMapping getHandlerMapping() {
        return HANDLER_MAPPING;
    }

}
