package www.superinterface.xyz.controller;

import www.superinterface.xyz.annotation.ReqeustMapping;

@ReqeustMapping(path = "/api")
public class helloController {

    @ReqeustMapping(path = "/hello")
    public String hello() {
        return "hello";
    }


}
