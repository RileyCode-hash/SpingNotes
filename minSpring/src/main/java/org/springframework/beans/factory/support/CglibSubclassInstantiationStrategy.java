package org.springframework.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class CglibSubclassInstantiationStrategy implements InstantiationStrategy{

    /*
    * use CGLIB
    *
    * @param beanDefinition
    * @return
    * @throws BeansException
    */



    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        try {
            Constructor declaredConstructor = beanDefinition.getBeanClass().getDeclaredConstructor();

            if (declaredConstructor == null) {
                return enhancer.create();
            }
    }catch (Exception e) {
            throw new BeansException("Failed to instantiate[" + beanDefinition.getBeanClass().getName() + "]", e);
        }

        return null;
    }

}
