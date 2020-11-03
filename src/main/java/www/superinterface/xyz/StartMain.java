package www.superinterface.xyz;

import www.superinterface.xyz.core.ServerEngine;

/**
 *  JerryMouse入口启动类
 */
public class StartMain {

    public static void main(String[] args) {
        ServerEngine se = new ServerEngine();
        se.startServerEngine();
    }

}
