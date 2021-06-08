package org.springframework.beans;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HelloService {



        public String sayHello() {
            System.out.println("hello");
            return "hello";
        }

}
