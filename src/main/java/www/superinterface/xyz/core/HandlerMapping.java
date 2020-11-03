package www.superinterface.xyz.core;

import www.superinterface.xyz.annotation.ReqeustMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerMapping {

    // requestMapping (记录所有动态请求与业务controller之间的映射关系)
    private Map<String, Handler> reqeustMapping = new HashMap<String, Handler>();

    // 添加当前的映射url与Handler的映射关系
    public void process(List beans) {
        for (Object o : beans) {
            Class clazz = o.getClass();
            ReqeustMapping clRe = (ReqeustMapping) clazz.getAnnotation(ReqeustMapping.class);
            String clpath = clRe.path();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                ReqeustMapping re = method.getAnnotation(ReqeustMapping.class);
                String path = re.path();
                reqeustMapping.put(clpath + path, new Handler(method, o));
            }
        }
        System.out.println(reqeustMapping.toString());
    }

    public Map<String, Handler> getReqeustMapping() {
        return reqeustMapping;
    }

}
