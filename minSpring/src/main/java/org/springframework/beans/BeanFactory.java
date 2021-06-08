package org.springframework.beans;

import java.util.HashMap;
import java.util.Map;
/*
* BeanFactory
* for
* simpleBeanContainer
*/
//public class BeanFactory {
//    private Map<String, Object> beanMap = new HashMap<>();
//
//    public void registerBean(String name, Object bean) {
//        beanMap.put(name, bean);
//    }
//
//    public Object getBean(String name) {
//        return beanMap.get(name);
//    }
//}
public interface BeanFactory {

    /*
    * get bean
    *
    * @param name
    * @return
    * @throws BeansException when nonexisting*/

    Object getBean(String name) throws BeansException;
}
