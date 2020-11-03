package www.superinterface.xyz.core;

import java.lang.reflect.Method;

public class Handler {

    private Method m = null;
    private Object o = null;

    public Handler(Method m, Object o){
        this.m = m;
        this.o = o;
    }

    public Method getM() {
        return m;
    }

    public void setM(Method m) {
        this.m = m;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}
