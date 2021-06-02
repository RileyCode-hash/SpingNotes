<script src="raphael-min.js"><
<script src="flowchart-latest.js"></script>

---
 # Spring V1.0主要功能" 

---

## Spring IoC， DI, MVC的工作原理

### 遵循单一职责原则

### MIN版本Spring基本实现思路：

1. 配置阶段
    + 配置 web.xml |    *DispatcherServlet*
    + 设定 init-param | *contextConfigLocation = classpath:application.xml*
    + 设定 url-pattern | /*
    + 配置Annotation | *@Controller @Service @Autowrited @RequestMapping*
    ---
2. 初始化阶段
    + 调用init（）方法  | *加载配置文件*
    + IOC容器初始化 | *Map<String, Object>
    + 扫描相关的类  | scan-package="com.gupaoedu"
  IOC  + 创建实例化并保存至容器    | *通过反射机制将类实例化放入IOC容器中*
  DI  + 进行DI操作    | *扫描IOC容器中的实例，给没有赋值的属性自动赋值*
  MVC  + 初始化HandlerMapping  | *将一个URL和一个Method进行一对一的关联映射Map<String, Method>*
  ---
3. 运行阶段
    + 调用doPost()/doGet() | *web容器调用doPost/doGet方法，获得request/response对象*
    + 匹配HandlerMapping | *从request对象中获得用户输入的url，找到对应的Method*
    + 反射调用method.invoker() | *利用反射调用方法并返回结果*
    + response.getWrite().write() | *将返回结果输出到游览器*

    ---
### 新建一个类，实现三个方法

<details>
<summary> 代码折叠 </summary>

```java
    public class GPDispatchServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException， IOException {
            this.doPost(req, resp);
        }

        
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException， IOException {

            <!-- 6.根据URL委派给具体的调用的方法 -->
            doDispatch();
           
        }

        @Override
        protected void init(ServletConfig config) throws ServletException {
          <!-- 1.初始化方法init -->
          <!--1. 加载配置文件 -->
          doLoadConfig();


          <!-- 2.扫描相关的类 -->
          doScanner();


          <!-- Ioc功能 -->
          <!-- 3.初始化Ioc容器，将扫描的类进行实例化，缓存到Ioc容器中 -->
          doInstance();


         <!-- DI功能 -->
          <!--4.完成依赖注入  -->
          doAutowired();

        <!-- MVC功能 -->
          <!-- 5.初始化HandlerMapping -->
          doInitHandlerMapping();
           
           System.out.printLn("GP sTRING Framework is init.")
     
        }
        //根据contextConfigLocation的classpath找到对应的配置文件
        private void doLoadConfig(String contextConfigLocatoin)
            InputString is = this.getClass.
    }
```

</details>





##  Spring IoC

**工厂模式，原型，单例（工厂怎么把对象创建出来，交给用户）**
## 从Servlet到ApplicatoinContext

+ **Map** 容器

+ **BeanFactory** 工厂  

+ **ApplicationContext** 上下文：持有BeanFactory引用， 门面模式

+ **BeanDefinitionReader**解析器：负责解析所有的配置文件

+ **BeanDefinition**元信息，配置（保存各种配置信息）
        xml, yml, annotation, properties

+ **Bean实例**，反射实例化Object
    原生Bean，代理Bean

+ **BeanWrapper**包装器模式：缓存到了Ioc容器，缓存
    持有Bean引用

## ico顶层设计， ListableBeanFactory为例
```flow

st=>start: 用户
op1=>operation: ApplicationContext
sub1=>operation: BeanDefinitionReader
sub2=>operation: BeanDefinition
cond=>start: getBean()
cond2=>operation: BeanWrappe

io=>inputoutput: BeanWrapper
para=>start: factory

st->op1->cond
io->cond
sub1(left)->io
cond(right)->para
para(right)->sub1(bottom)->sub2
sub2->cond2
cond2(left)->cond
```
<br>

用户通过 =>  **ApplicationContext** => 调用 **getBean()**方法， 底层各种factory方法listable等，创建factory对象， 所以要调用**BeanDefinitionReader**, 读取bean配置文件，创建 **BeanDefinition** = > 换存到容器里就是**BeanWrapper**对象， 所以getBean()实际拿到的就是**BeanWrapper**对象

````java
init() {
    new ApplicationContext() {
        reader = new BeanDefinitionReader();
        reader.loadBeanDefinitions();

        factory.doRegistryBeanDefinition() {
            beanDefinitionMap.put()
        }

        doLoadInstance() {
            getBean() //循环调用
        }
    }
}

getBean() {
    BeanDefinition beanDefinition = registry.beanDefinitionMap.get(beanName);

    Object instance = instantiateBean();

    BeanWrapper beanWrapper = new BeanWrapper(instance);

    factoryBeanInstanceCache.put(beanWrapper);

    populateBean(); //依赖注入
}
````


# 基础java知识

## 1. servlet(模板模式)
- HttpServlet已经实现了service方法：HttpServlet是一个抽象类

一个类声明成抽象方法，一般有两个原因：
_有抽象方法_ OR
_没有抽象方法，但是不希望被实例化_

HttpServlet做成抽象类，仅仅是为了不让new

如何写一个Servet？

不用实现javax.servlet接口

不用继承GenericServlet抽象类

只需继承HttpServlet并重写doGet()/doPost()

父类把能写的逻辑都写完，把不确定的业务代码抽成一个方法，调用它。当子类重写该方法，整个业务代码就活了。这就是模板方法模式

父类：                                                               子类
````
service() {                                                        service() {
    doXxx(); //具体业务代码，但是父类无法知道子类具体业务逻辑，  <= 继承  doXxx();
    //所以后抽象惩罚让子类重写                                         }
}

protected void doXxx() {                                   <= 覆盖 void doXxx() {
    //空实现，或者默认实现                                          //具体实现
}                                                                   }
````
## 1.2 ServletContext

map，服务器会为每个应用创建一个ServletContext对象

ServletContext对象的作用是在整个Web应用的动态资源（Servlet/JSP）之间共享数据

这种用来装载共享数据的对象，在JavaWeb中共有4个，而且更习惯被成为“域对象”：

ServletContext域（Servlet间共享数据）

Session域（一次会话间共享数据，也可以理解为多次请求间共享数据）

Request域（同一次请求共享数据）

Page域（JSP页面内共享数据）

它们都可以看做是map，都有getAttribute()/setAttribute()方法。

## 2.Java泛型Generics
- 泛型类 `<T>`  Type Parameter
````
public class ArrayList<T> {
    private T[] array;
    private int size;
    public void add(T e) {...}
    public void remove(int index) {...}
    public T get(int index) {...}
}
````
- 对变量类型进行抽取
````
public User getUser(T t){...}
````
- 获取构造函数的参数

获取到构造函数的对象之后，可以通过getParameterTypes()获取到构造函数的参数。
````
Constructor constructors = birdClass.getConstructor(new Class[]{String.class});
            Class[] parameterTypes = constructors.getParameterTypes();
````
## 3.反射
- JVM是如何构建一个实例的
A a = new A(); 
>Step1: ClassLoader加载.class文件到内存（jvm内存;执行静态代码块和静态初始化语句

>Step2: 执行new，申请一个内存空间

>Step3:调用构造器，创建一个空白对象

>step4：子类调用父类构造器

>step5:构造器执行： 执行构造代码块和初始化语句； 构造器内容
- Class

Class类对象就相当于B超的探头，将一个类的方法、变量、接口、类名、类修饰符等信息告诉运行的程序。

+ 获取构造函数Constructor

获取构造函数的方法：
````
Class birdClass = Bird.class;
Constructor[] constructors = birdClass.getConstructors();
````

一个类会有多个构造函数，getConstructors()返回的是Constructor[]数组，包含了所有声明的用public修饰的构造函数。

如果你已经知道了某个构造的参数，可以通过下面的方法获取到回应的构造函数对象：
````
public class Alunbar {
    public static void  main(String arts[]){

        Class birdClass = Bird.class;
        try{
            Constructor constructors = birdClass.getConstructor(new Class[]{String.class});
        }catch(NoSuchMethodException  e){

        }
    }

    private class Bird {
        public Bird(){

        }

        public Bird(String eat){

        }
    }
}
````

- 类加载器

loadClass()，告诉它需要加载的类名，它会帮你加载
````
    // 子类应该重写该方法
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
````
加载.class文件大致可以分为3个步骤：

检查是否已经加载，有就直接返回，避免重复加载
当前缓存中确实没有该类，那么遵循父优先加载机制，加载.class文件
上面两步都失败了，调用findClass()方法加载
需要注意的是，ClassLoader类本身是抽象类，而抽象类是无法通过new创建对象的。所以它的findClass()方法写的很随意，直接抛了异常，反正你无法通过ClassLoader对象调用。也就是说，父类ClassLoader中的findClass()方法根本不会去加载.class文件。

正确的做法是，子类重写覆盖findClass()，在里面写自定义的加载逻辑。比如：
````
@Override
public Class<?> findClass(String name) throws ClassNotFoundException {
	try {
		/*自己另外写一个getClassData()
                  通过IO流从指定位置读取xxx.class文件得到字节数组*/
		byte[] datas = getClassData(name);
		if(datas == null) {
			throw new ClassNotFoundException("类没有找到：" + name);
		}
		//调用类加载器本身的defineClass()方法，由字节码得到Class对象
		return defineClass(name, datas, 0, datas.length);
	} catch (IOException e) {
		e.printStackTrace();
		throw new ClassNotFoundException("类找不到：" + name);
	}
}
````
defineClass()是ClassLoader定义的方法，目的是根据.class文件的字节数组byte[] b造出一个对应的Class对象。我们无法得知具体是如何实现的，因为最终它会调用一个native方法：

- 反射API
>创建实例

clazz.newInstance()底层还是调用Contructor对象的newInstance()。所以，要想调用clazz.newInstance()，必须保证编写类的时候有个无参构造。

## 为什么根据Class对象获取Method时，需要传入方法名+参数的Class类型？
调用Class对象的getMethod()方法时，内部会循环遍历所有Method，然后根据方法名和参数类型匹配唯一的Method返回。

## 调用method.invoke(obj, args);时为什么要传入一个目标对象？
把Method理解为方法执行指令吧，它更像是一个方法执行器，必须告诉它要执行的对象（数据）。

>反射调用方法

## 4.Java 动态代理
- 问题： 原有代码：
````
public class Calculator {

	//加
	public int add(int a, int b) {
		int result = a + b;
		return result;
	}

	//减
	public int subtract(int a, int b) {
		int result = a - b;
		return result;
	}

	//乘法、除法...
}

````
现有一个需求：在每个方法执行前后打印日志.

直接修改
````

public class Calculator {

	//加
	public int add(int a, int b) {
		System.out.println("add方法开始...");
		int result = a + b;
		System.out.println("add方法结束...");
		return result;
	}

	//减
	public int subtract(int a, int b) {
		System.out.println("subtract方法开始...");
		int result = a - b;
		System.out.println("subtract方法结束...");
		return result;
	}

	//乘法、除法...
}

````
上面的方案是有问题的：

直接修改源程序，不符合开闭原则。应该对扩展开放，对修改关闭
如果Calculator有几十个、上百个方法，修改量太大
存在重复代码（都是在核心代码前后打印日志）
日志打印硬编码在代理类中，不利于后期维护：比如你花了一上午终于写完了，组长告诉你这个功能取消，于是你又要打开Calculator花十分钟删除日志打印的代码！

- 静态代理,通过代理访问目标对象

静态代理的实现比较简单：编写一个代理类，实现与目标对象相同的接口，并在内部维护一个目标对象的引用。通过构造器塞入目标对象，在代理对象中调用目标对象的同名方法，并添加前拦截，后拦截等所需的业务功能。

- 将Calculator抽取为接口
````

/**
 * 目标对象实现类，实现Calculator接口
 */
public class CalculatorImpl implements Calculator {

	//加
	public int add(int a, int b) {
		int result = a + b;
		return result;
	}

	//减
	public int subtract(int a, int b) {
		int result = a - b;
		return result;
	}

	//乘法、除法...
}

````
- 创建代理类CalculatorProxy实现Calculator
````

/**
 * 代理对象实现类，实现Calculator接口
 */
public class CalculatorProxy implements Calculator {
        //代理对象内部维护一个目标对象引用
	private Calculator target;
        
        //构造方法，传入目标对象
	public CalculatorProxy(Calculator target) {
		this.target = target;
	}

        //调用目标对象的add，并在前后打印日志
	@Override
	public int add(int a, int b) {
		System.out.println("add方法开始...");
		int result = target.add(a, b);
		System.out.println("add方法结束...");
		return result;
	}

        //调用目标对象的subtract，并在前后打印日志
	@Override
	public int subtract(int a, int b) {
		System.out.println("subtract方法开始...");
		int result = target.subtract(a, b);
		System.out.println("subtract方法结束...");
		return result;
	}

	//乘法、除法...
}

````
- 使用代理对象完成加减乘除，并且打印日志
````

public class Test {
	public static void main(String[] args) {
		//把目标对象通过构造器塞入代理对象
		Calculator calculator = new CalculatorProxy(new CalculatorImpl());
		//代理对象调用目标对象方法完成计算，并在前后打印日志
		calculator.add(1, 2);
		calculator.subtract(2, 1);
	}
}

````

没解决重复代码，如果有很多类，没解决修改量太大的问题，
**所以我们要代理的不是代理类，而是代理对象，根据接口自动生成代理对象**

- 动态代理

Proxy有个静态方法：getProxyClass(ClassLoader, interfaces)，只要你给它传入类加载器和一组接口，它就给你返回代理Class对象。

- 使用代理对象完成加减乘除，并且打印日志
````

public class ProxyTest {
	public static void main(String[] args) throws Throwable {
		//Calculator的类加载器
		Class calculatorProxyClazz = Proxy.getProxyClass(Calculator.class.getClassLoader(), Calculator.class);
		//得到有参构造器
        Constructor constructor =  calculatorProxyClazz.getConstructor(InvocationHandler.class);
        //反射创建代理实例
        Calculator CalculatorProxyImpl = (Calculator)constructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method, method, Object[] args) throws Exception {
                //手动new一个目标对象
                CalculatorImpl calculatorImpl = new CalculatorImpl();
                //反射执行目标对象的方法
                Object result = method.invoke(calculatorImpl, args);
                //返回目标对象执行结果
                return result;
            }
        })
		CalculatorProxyImpl.add(1, 2);
		
	}
}

````
代理对象改变，invoke方法要改，所以把目标对象当参数传进来
````

public class ProxyTest {
	public static void main(String[] args) throws Throwable {
        //传入目标对象
        CalculatorImpl target = new CalculatorImpl();
        //根据它实现的接口生成代理对象， 代理对象调用目标对象方法
		Calculator calculatorProxy = (Calculator)getProxy(target);
        calculatorProxy.add(1, 2);
        calculatorProxy.substract(2, 1);
    }

    private static Object getProxy(final Object target) throws Exception {
		Class ProxyClazz = Proxy.getProxyClass(target.getclass().getClassLoader(), target.getclass().getInterfaces());
		//得到有参构造器
        Constructor constructor =  ProxyClazz.getConstructor(InvocationHandler.class);
        //反射创建代理实例
        Object proxy = constructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method, method, Object[] args) throws Exception {
                System.out.println(method.getName() + "方法开始执行。。");
                Object result = method.invoke(target, args);
                //反射执行目标对象的方法
                
                return result;
            }
        });
		return proxy;
		
	}
}

````
无论现在系统有多少类，只要你把实例传进来，getProxy()都能给你返回对应的代理对象。就这样，我们完美地跳过了代理类，直接创建了代理对象！

不过实际编程中，一般不用getProxyClass()，而是使用Proxy类的另一个静态方法：Proxy.newProxyInstance()，直接返回代理实例，连中间得到代理Class对象的过程都帮你隐藏：
````

public class ProxyTest {
	public static void main(String[] args) throws Throwable {
        //传入目标对象
        CalculatorImpl target = new CalculatorImpl();
        //根据它实现的接口生成代理对象， 代理对象调用目标对象方法
		Calculator calculatorProxy = (Calculator)getProxy(target);
        calculatorProxy.add(1, 2);
        calculatorProxy.substract(2, 1);
    }

    private static Object getProxy(final Object target) throws Exception {
		Object proxy = Proxy.newProxyInstance(target.getclass().getClassLoader(), target.getclass().getInterfaces(),
		new InvocationHandler() {
            
            public Object invoke(Object proxy, Method, method, Object[] args) throws Exception {
                System.out.println(method.getName() + "方法开始执行。。");
                Object result = method.invoke(target, args);
                //反射执行目标对象的方法
                
                return result;
            }
        }
        );
		return proxy;
		
	}
}

````




# Spring DI
组合复合原则（怎么给对象自动赋值，循环依赖注入）
# Spring MVC
委派，策略，解释器原则（用户输入URL怎么样和java代码关联）
# Spring AOP
责任，动态代理

我们需要一个通知类（TransactionManager）执行事务，一个代理工厂帮助生成代理对象，然后利用动态代理将事务代码织入代理对象的各个方法中。
````
UserService {
    public void test() {
        //开启事务
        userDao.add();
    }
}
````
````
BrandService {
    public void test() {
        //开启事务
        brandDao.add();
    }
}
````
````
CategoryService {
    public void test() {
        //开启事务
        categoryDao.add();
    }
}
````
希望最终达到的效果是，我加了个@MyTransactional后，代理工厂给我返回一个代理对象：

UserService --》 入参  |代理工厂| 出参 --》 UserServiceProxy

细节分析：

UserServiceProxy.test() --> //+1 +8
````
|代理工厂| 
  代理对象 {
    //1.开启事务
    txManager.beginTransaction(); //+2
    //2.执行事务
    rtValue = method.invoke(target, args) --> 调用代理对象同名方法+3 +5
       //3.提交事务
    txManager.commit(); //+6
    //3.返回结果
    return rtValue; //+7
}
````

````
UserService {
    public void test() { //+4
        //开启事务
        userDao.add();
    }
}
````
代理对象方法 = 事务 + 目标对象方法。

事务操作，必须使用同一个Connection对象。如何保证？第一次从数据源获取Connection对象并开启事务后，将它存入当前线程的ThreadLocal中，等到了DAO层，还是从ThreadLocal中取，这样就能保证开启事务和操作数据库使用的Connection对象是同一个。

开启事务后，Controller并不是直接调用Service，而是Spring提供的代理对象

Service和Dao关系也是i如此

# AOP事务具体代码实现

## ConnectionUtils工具类

````
package com.demo.myaopframework.utils;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;

/**
 * 连接的工具类，它用于从数据源中获取一个连接，并且实现和线程的绑定
 */
public class ConnectionUtils {

    private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();

    private static BasicDataSource dataSource = new BasicDataSource();

    //静态代码块,设置连接数据库的参数
    static{
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }


    /**
     * 获取当前线程上的连接
     * @return
     */
    public Connection getThreadConnection() {
        try{
            //1.先从ThreadLocal上获取
            Connection conn = tl.get();
            //2.判断当前线程上是否有连接
            if (conn == null) {
                //3.从数据源中获取一个连接，并且存入ThreadLocal中
                conn = dataSource.getConnection();
                tl.set(conn);
            }
            //4.返回当前线程上的连接
            return conn;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 把连接和线程解绑
     */
    public void removeConnection(){
        tl.remove();
    }
}
````

## AOP通知（事务管理器）

````
package com.demo.myaopframework.utils;

/**
 * 和事务管理相关的工具类，它包含了，开启事务，提交事务，回滚事务和释放连接
 */
public class TransactionManager {

    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    /**
     * 开启事务
     */
    public  void beginTransaction(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public  void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    public  void rollback(){
        try {
            connectionUtils.getThreadConnection().rollback();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 释放连接
     */
    public  void release(){
        try {
            connectionUtils.getThreadConnection().close();//还回连接池中
            connectionUtils.removeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
````

## 自定义注解

````
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTransactional {
}
````
## Service

````
public interface UserService {
	void getUser();
}

 
public class UserServiceImpl implements UserService {
	@Override
	public void getUser() {
		System.out.println("service执行...");
	}
}
````
## 实例工厂

````
public class BeanFactory {

	public Object getBean(String name) throws Exception {
		//得到目标类的Class对象
		Class<?> clazz = Class.forName(name);
		//得到目标对象
		Object bean = clazz.newInstance();
		//得到目标类上的@MyTransactional注解
		MyTransactional myTransactional = clazz.getAnnotation(MyTransactional.class);
		//如果打了@MyTransactional注解，返回代理对象，否则返回目标对象
		if (null != myTransactional) {
			ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
			TransactionManager txManager = new TransactionManager();
			txManager.setConnectionUtils(new ConnectionUtils());
			//装配通知和目标对象
			proxyFactoryBean.setTxManager(txManager);
			proxyFactoryBean.setTarget(bean);
			Object proxyBean = proxyFactoryBean.getProxy();
			//返回代理对象
			return proxyBean;
		}
		//返回目标对象
		return bean;
	}
}
````
## 代理工厂

````
public class ProxyFactoryBean {
	//通知
	private TransactionManager txManager;
	//目标对象
	private Object target;

	public void setTxManager(TransactionManager txManager) {
		this.txManager = txManager;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	//传入目标对象target，为它装配好通知，返回代理对象
	public Object getProxy() {
		Object proxy = Proxy.newProxyInstance(
				target.getClass().getClassLoader(),/*1.类加载器*/
				target.getClass().getInterfaces(), /*2.目标对象实现的接口*/
				new InvocationHandler() {/*3.InvocationHandler*/
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						try {
							//1.开启事务
							txManager.beginTransaction();
							//2.执行操作
							Object retVal = method.invoke(target, args);
							//3.提交事务
							txManager.commit();
							//4.返回结果
							return retVal;
						} catch (Exception e) {
							//5.回滚事务
							txManager.rollback();
							throw new RuntimeException(e);
						} finally {
							//6.释放连接
							txManager.release();
						}

					}
				}
		);
		return proxy;
	}

}
````

AOPTest, 
````
BeanFactory beanFactory = new BeanFactory();
try {
    Object bean = beanFactory.getBean("com.demo.mya.service.UserServiceImpl");
    Sout(bean.getClass().getName());
}
````
>com.demo.mya.service.UserServiceImpl

给UserServiceImpl添加@MyTransactional注解，得到代理对象：
>com.sun.proxy.$Proxy2


# JDBC
# JDBC的演化版本1.0
通过DriveManager得到Connection，得到PreparedStatement, PreparedStatement执行sql返回结果
````java
public class Basic {
    @Test
    public void testJdbc() throws SQLException, ClassNotFoundException {
        // 1.注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        // 2.建立练连接
        String url = "jdbc:mysql://192.168.56.10:3306/test";
        String user = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, user, password);

        // 3. 创建sql模板
        String sql = "select * from t_user where id = ?";
        PreparedStatement preparedStatement = conn.preparedStatement(sql);

        // 4. 设置模板参考
        preparedStatement.setInt(1, 5);

        // 5. 执行语句
        ResultSet rs = preparedStatement.executeQuery();

        // 6. 处理结果
        while (rs.next()) {
            System.out.println(rs.getObject(1) + "\t" + rs.getObject(2) + "\t" + rs.getObject(3) + "\t" + rs.getObject(4))；
            
        }

        // 7. 释放资源
        rs.close();
        preparedStement.close();
        conn.close();

    }
}
````
### 获取Connection的步骤太复杂，需要封装； 资源释放太随意，不够规范，数据库的连接数是有限的，如果不及时释放，会导致其他请求无法访问。应该把释放资源的操作放在finally中，保证资源一定会被关闭。


````java
public class Basic {
    // 抛异常
    @Test
    public void testJdbc() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // 1. 获得连接
            conn = JdbcUtils.getConnection();
            String sql = "select * from t_user where id = ?";
            ps = conn.preparedStatement(sql);

            // 4. 设置模板参考
            ps.setInt(1, 5);

            // 5. 执行语句
            rs = ps.executeQuery();

            // 6. 处理结果
            while (rs.next()) {
                System.out.println(rs.getObject(1) + "\t" + rs.getObject(2) + "\t" + rs.getObject(3) + "\t" + rs.getObject(4))；
                
            }
        } finally {
            JdbcUtils.free(rs, ps, conn);
        }
    }
}
````
JdbcUtils 1.0版本
````java
public class JdbcUtils {

        private static Properties pros = null;

        // 只在JdbcUtils类都加载执行一次
        static {
            // 1. 给pros进行初始化，加载jdbc.properties文件到props对象中
            try {
                InputStream in = JdbcUtils.class.getClassLoader()
                                .getResourceAsStream("jdbc.properties");
                props = new Properties();
                props.load(in);  
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //加载驱动类
            try {
                Class.forName(props.getProperty("driver"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        // 获取连接
        public static Connection getConnection() throws SQLException {
            // 得到Connection
            return DriverManager.getConnection(props.getProperty("url"),
                            props.getProperty("username"),
                            props.getProperty("password"));
        }

        // 释放连接
        public static void free (ResultSet rs, Statement st, Connection conn) {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrack();
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrack();
                } finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
            }
        }
    }
}
````
## CRUD
crud
+ dao
    + UserDao(interface)
    + UserDaoJdbcImpl
+ pojo
    + User
+ DAOTest
+ JdbcUtils

UserDao
````java
public interface UserDao {
	int addUser(User user) throws SQLException;
	int update(User user) throws SQLException;
	int delete(User user) throws SQLException;
	User getUser(int Id) throws SQLException;
	User findUser(String name, int age) throws SQLException;
}
````
UserDaoJdbcImpl
````java
public class UserDaoJdbcImpl implements UserDao {

	public int addUser(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "insert into t_user(name,age, birthday) values (?,?,?) ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getName());
			ps.setInt(2, user.getAge());
			ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
			return ps.executeUpdate();
/* 		} catch (SQLExceptoin e) {
            //转为DaoException（运行时异常）抛出，Service层可以不处理                         
			throw new DaoException(e.getMessage(), e); */
        } finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	public int delete(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "delete from t_user where id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getId());
			System.out.println(sql);
			return ps.executeUpdate(sql);
		 
        /* 		} catch (SQLExceptoin e) {
            //转为DaoException（运行时异常）抛出，Service层可以不处理                         
			throw new DaoException(e.getMessage(), e); */
        }   finally {
			JdbcUtils.free(rs, ps, conn);
		}

	}

	public int update(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "update t_user set name=?, age=?, birthday=? where id=? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getName());
			ps.setInt(2, user.getAge());
			ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
			ps.setInt(4, user.getId());
			return ps.executeUpdate();
            /* 		} catch (SQLExceptoin e) {
            //转为DaoException（运行时异常）抛出，Service层可以不处理                         
			throw new DaoException(e.getMessage(), e); */
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	public User findUser(String name, int age) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select id, name, birthday  from t_user where name=? and age=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setInt(2, age);
			rs = ps.executeQuery();
			while (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setAge(rs.getInt("age"));
				user.setName(rs.getString("name"));
				user.setBirthday(rs.getDate("birthday"));
			/* 		} catch (SQLExceptoin e) {
            //转为DaoException（运行时异常）抛出，Service层可以不处理                         
			throw new DaoException(e.getMessage(), e); */
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
		return user;
	}

	public User getUser(int userId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select id, name, age, birthday from t_user where id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setAge(rs.getInt("age"));
				user.setName(rs.getString("name"));
				user.setBirthday(rs.getDate("birthday"));
			/* 		} catch (SQLExceptoin e) {
            //转为DaoException（运行时异常）抛出，Service层可以不处理                         
			throw new DaoException(e.getMessage(), e); */
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
		return user;
	}

````
DAOTest
````java
public class DAOTest {
	public static void main(String[] args) throws SQLException {
		UserDao userDao = new UserDaoJdbcImpl();
		User user = new User();
		user.setAge(19);
		user.setName("little ming");
		user.setBirthday(new Date());
		userDao.addUser(user);
	}
}
````
异常处理
上面的CRUD并没有捕获异常，而是直接往外抛。这会带来两个后果：

SQLException是编译时异常，Service在调用DAO时必须处理异常，否则编译不通过。如何处理？要么继续抛，交给Controller处理（意义不大），要么try catch（Service层代码很臃肿，不美观）。
DAO接口有声明异常SQLException，这等于向外界暴露DAO层是JDBC实现。而且针对该接口只能用关系型数据库，耦合度太高了。后期无法切换DAO实现。
比较好的做法是，将SQLException转为运行时异常抛出，Service层可处理也可不处理。

DaoException
````java
public class DaoException extends RuntimeException {
	public DaoException() {
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}
}
````
## 模板方法模式重构
繁琐， 如果还有StudentDaoJdbcImpl、TeacherDaoJdbcImpl，那么同样的代码要写好多遍。所以，必须要重构。大体思路是：相同的代码抽取到父类AbstractDao。观察UserDao， 不论是UserDaoLmpl还是StudentDaoJdbcImpl，TeacherDaoJdbcImp，
只有sql模板和设置模板参数的代码不同。可以把sql和参数抽取成父类方法的形参

> String sql = "select id, name, age, birthday from t_user where id=?";
			
> ps.setInt(1, userId);

AbstractDao
````java
public abstract class AbstractDao {

	public int addUser(String sql, Object...args)  {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			// String sql = "insert into t_user(name,age, birthday) values (?,?,?) ";

            // sql由调用者传入
			ps = conn.prepareStatement(sql);
			// ps.setString(1, user.getName());
			// ps.setInt(2, user.getAge());
			// ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+ 1, args[i]);
            }
			return ps.executeUpdate();
		} catch (SQLExceptoin e) {                      
			throw new DaoException(e.getMessage(), e);
        } finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
}
````
UserDaoImpl
````java
public class UserDaoImpl extends AbstractDao implements UserDao {
    public int addUser(User user) {
        String sql = "insert into t_user(name, age, birthday) values (?,?,?) ";
        Object[] args = new Object[]{user.getName(), user.getAge(), user.getBirthday()};
        
        // 调用父类AbstractDao方法
        return super.update(sql, args);
    }
    public int update(User user) {
		String sql = "update t_user set name=?, age=?, birthday=? where id=? ";
		Object[] args = new Object[]{user.getName(), user.getAge(),
				user.getBirthday(), user.getId()};
		return super.update(sql, args);
	}

	//改
	public int delete(User user) {
		String sql = "delete from t_user where id=?";
		Object[] args = new Object[]{user.getId()};
		return super.update(sql, args);
	}
}
````
如何抽取查询方法?

user， student都需要用map来装

父类无法制定一个通用代码满足所有子类的结果集映射，因为只有子类自己知道映射规则。所以，我们只能把结果集映射的权利交还给子类去实现。子类如果需要查询，就必须自己实现AbstractDao的rowMapper方法。

AbstractDao
````java
public abstract class AbstractDao {

	public int update(String sql, Object...args)  {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			// String sql = "insert into t_user(name,age, birthday) values (?,?,?) ";

            // sql由调用者传入
			ps = conn.prepareStatement(sql);
			// ps.setString(1, user.getName());
			// ps.setInt(2, user.getAge());
			// ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+ 1, args[i]);
            }
			return ps.executeUpdate();
		} catch (SQLExceptoin e) {                      
			throw new DaoException(e.getMessage(), e);
        } finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
    //查询
    public List<Object> query(String sql, Object[] args, RowMapper, rowMapper)  {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List list = new ArrayList<>();
    try {
        conn = JdbcUtils.getConnection();
        // sql由调用者传入
        ps = conn.prepareStatement(sql);
 
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i+ 1, args[i]);
        }
        rs = ps.executeQuery();
        Object obj = null;
        while (rs.next()) {
            Object o = rowMapper.mapRow(rs);
            list.add(o);
        }
        return list;
    } catch (SQLExceptoin e) {                      
        throw new DaoException(e.getMessage(), e);
    } finally {
        JdbcUtils.free(rs, ps, conn);
    }
}

// 定义成抽象方法，让子类去实现
	abstract protected Object rowMapper(ResultSet rs);
       
````
UserDaoImpl
````java
public User findUser(String name, int age) {
    String sql =  "select id, name, age, birthday from t_user where id=?";
    Object[] args = new Object[] {name, age};
    Object user = super.query(sql, args);
    return (User) user;
}

public User getUser(int id) {
    String sql =  "select id, name, age, birthday from t_user where name=? and age=?";
    Object[] args = new Object[] {id};
    Object user = super.query(sql, args);
    return (User) user;
}

//UserDaoImpl的结果map
protected Object rowMapper(ResultSet rs) {
     User user = null;
        try {
            user = new User();
           user.setId(rs.getInt("id"));
			user.setAge(rs.getInt("age"));
			user.setName(rs.getString("name"));
			user.setBirthday(rs.getDate("birthday"));
		} catch (SQLException e) {
			throw new DaoException("mapping error");
		}
		return user;
	}
}
````
## 假设现在UserDao增加了一个新方法?
````java
public interface UserDao {
	int addUser(User user);
	int update(User user);
	int delete(User user);
	User getUser(int Id);
	User findUser(String name, int age);
	//新增查询方法：根据年龄查询
	List<User> selectUsers(int age);
}
````
### 返回值是List<User>，而UserDaoImpl中实现的映射方法rowMapper()只能封装User对象：
直接传方法不行？那我就把这个方法塞进一个对象里，通过对象去调用方法（把需要代理对象执行的代码写在InvocationHandler对象的invoke方法中，再把invocationHandler塞进代理对象供它调用）。

这种模式其实叫策略模式，而且一般是传入接口的实现类。

现在子类已经不需要实现父类的抽象方法了（一个规则无法满足不同返回值映射），改为由子类实现RowMapper接口传入匿名对象的方式，所以AbstractDao中的抽象方法可以删除。也就是说AbstractDao已经没有抽象方法了。于是我把它声明为普通类（可以new），并改名为MyJDBCTemplate。而且，使用MyJDBCTemplate时，我决定不再使用继承，而是选择组合方式（组合比继承灵活）。
RowMapper
````java
public interfae RowMapper {
    // map result 
    Object mapRow(ResultSet rs) throws SQLException;
}
````
MyJDBCTemplate
````java
public class MyJDBCTmplate {
    //增删改
	public int update(String sql, Object...args)  {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			// String sql = "insert into t_user(name,age, birthday) values (?,?,?) ";

            // sql由调用者传入
			ps = conn.prepareStatement(sql);
			// ps.setString(1, user.getName());
			// ps.setInt(2, user.getAge());
			// ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+ 1, args[i]);
            }
			return ps.executeUpdate();
		} catch (SQLExceptoin e) {                      
			throw new DaoException(e.getMessage(), e);
        } finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
    //查询
    public Object query(String sql, Object[] args, RowMapper rowMapper)  {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List list = new ArrayList<>();
    try {
        conn = JdbcUtils.getConnection();
        // sql由调用者传入
        ps = conn.prepareStatement(sql);
 
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i+ 1, args[i]);
        }
        rs = ps.executeQuery();
        Object obj = null;
        while (rs.next()) {
				Object o = rowMapper.mapRow(rs);
				list.add(o);
			}
			return list;
    } catch (SQLExceptoin e) {                      
        throw new DaoException(e.getMessage(), e);
    } finally {
        JdbcUtils.free(rs, ps, conn);
    }
}

       
````
UserDaoImpl
````java
public class UserDaoImpl implements UserDao {
    MyDBCTemplate jdbcTemplate = new MyJDBCTemplate();

    public int addUser(User user) {
        String sql = "insert into t_user(name, age, birthday) values (?,?,?) ";
        Object[] args = new Object[]{user.getName(), user.getAge(), user.getBirthday()};
        
        // 调用父类AbstractDao方法
        return jdbcTemplate.update(sql, args);
    }

    
    public int update(User user) {
        String sql = "update t_user set name=?, age=?, birthday=? where id=? ";
        Object[] args = new Object[]{user.getName(), user.getAge(), user.getBirthday(),  user.getId()};
        
        // 调用父类AbstractDao方法
        return jdbcTemplate.update(sql, args);
    }

    public User findUser(String name, int age) {
        String sql =  "select id, name, age, birthday from t_user where id=?";
        Object[] args = new Object[] {name, age};
        Object user = super.query(sql, args);
        return (User) user;
    }

    public int delete(User user) {
    String sql = "delete from t_user where id=?";
    Object[] args = new Object[]{user.getId()};
    //调用jdbcTemplate的update方法
    return jdbcTemplate.update(sql, args);

    public User getUser(int id) {
        String sql =  "select id, name, age, birthday from t_user where id=?";
        Object[] args = new Object[] {id};
        //调用jdbcTemplate的query方法，传入sql，args， RowMapper匿名对象
        List list = jdbcTemplate.query(sql, args, new RowMapper() {
            public Object mapRow(ResultSet rs) throws SQLException {
                User user = new User();
               user.setAge(rs.getInt("age"));
				user.setName(rs.getString("name"));
				user.setBirthday(rs.getDate("birthday"));
				return user;
            }
        });
        
        return (User)list.get(0);
    }

     public User findUser(String name, int age) {
        String sql =  "select id, name, age, birthday from t_user where name=? and age=?";
        Object[] args = new Object[] {name, age};
        //调用jdbcTemplate的query方法，传入sql，args， RowMapper匿名对象
        List list = jdbcTemplate.query(sql, args, new RowMapper() {
            public Object mapRow(ResultSet rs) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
               user.setAge(rs.getInt("age"));
				user.setName(rs.getString("name"));
				user.setBirthday(rs.getDate("birthday"));
				return user;
            }
        });
        
        return (User)list.get(0);
    }

     public List selectUser(int age) {
        String sql =  "select id, name, age, birthday from t_user where age=?";
        Object[] args = new Object[] {age};
        //调用jdbcTemplate的query方法，传入sql，args， RowMapper匿名对象
        List list = jdbcTemplate.query(sql, args, new RowMapper() {
            public Object mapRow(ResultSet rs) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
               user.setAge(rs.getInt("age"));
				user.setName(rs.getString("name"));
				user.setBirthday(rs.getDate("birthday"));
				return user;
            }
        });
        
        return list;
    }

 
````
jdbcUtils 2.0

````java
public class JdbcUtils {

        //初始化一个数据库
        private static MyDataSource dataSource = new MyDataSource();

        // 获取连接
        public static Connection getConnection() throws SQLException {
            // 从数据源获取Connection并返回
            return dataSource.getConnection();
        }

          // 获取数据源
        public static MyDataSource getDataSource() {   
            return dataSource;
        }

        // 释放连接
        public static void free (ResultSet rs, Statement st, Connection conn) {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrack();
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrack();
                } finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
            }
        }
    }
}
````
MyDataSource
````java
public class MyDataSource {

        //数据库信息，用于连接数据库，放进properties里面
       /*  private static String url = "jdbc:mysql://192.168.56.10:3306/test?userSSL-false";
        private static String user = "root";
        private static String password = "root"; */
        private static Properties pros = null;

        // 数据库信息 
        static {
            // 1. 给pros进行初始化，加载jdbc.properties文件到props对象中
            try {
                InputStream in = JdbcUtils.class.getClassLoader()
                                .getResourceAsStream("jdbc.properties");
                props = new Properties();
                props.load(in);  
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//连接池数据，省略
        private static int initCount = 5;
        private static int currentIdleCount = 0;

//LinkedList充当连接池，removeFirst取出连接，addLast归还连接
        private final static LinkedList<Connection> connectionpool = new LinkedList<>();
    
        public MyDataSource() {
            try {
                for (int i = 0; i < initCount; i++) {
                    // 创建RealConnection
                    Connection realConnection = DriverManager.getConnection(props.getProperty("url"),
                                props.getProperty("username"),
                                props.getProperty("password"));
                                // 将RealConnection传入createProxyConnection(),得到代理连接并加入池中， currentIdleCount++
                    connectionPool.addLast(this.createProxyConnection(realConnection));
                    currentIdleCount++;
                }
                Sout("...连接池初始化结束" + currentIdleCount + "个Connection...")
            } catch
        }

        // 获取连接
        public Connection getConnection() throws SQLException {
            // 同步代码
            synchronized (connectionPool) {

                // 连接池中还有空闲连接，从池中取出，currentIdleCount--

            }
            return connectionPool.removeFirst();
        }


        // 释放连接
        public static void free (ResultSet rs, Statement st, Connection conn) {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrack();
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrack();
                } finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
            }
        }
    }
}
````
# 工厂模式重构
User
````java
public class User {
	private Long id;
	private String name;
	private Integer age;
	private Date birthday;

        //省略getter、setter...
}
````
DaoFactory
````java
public class DaoFactory {
	private static UserDao userDao = null;
	private static DaoFactory daoFactory = new DaoFactory();

	private DaoFactory() {
		try {
			Properties prop = new Properties();
			InputStream inStream = DaoFactory.class.getClassLoader()
					.getResourceAsStream("daoconfig.properties");
			prop.load(inStream);
			//从配置文件中读取UserDao的实现类全类名
			String userDaoClass = prop.getProperty("userDao");
			Class userDaoImplClazz = Class.forName(userDaoClass);
			//反射创建对象
			userDao = (UserDao) userDaoImplClazz.newInstance();
		} catch (Throwable e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static DaoFactory getInstance() {
		return daoFactory;
	}

	public UserDao getUserDao() {
		return userDao;
	}
}
````
daoconfig.properties
````java
userDao = com.test.crudrefactorfinal.dao.UserDaoImpl2
````
DAOTest
````java
public class DAOTest {
	public static void main(String[] args) {
		//通过工厂得到DAO实现类，如果想换成UserDaoImpl2，修改配置即可
		UserDao userDao = DaoFactory.getInstance().getUserDao();
		List<User> users = userDao.selectUsers(18);
		for (User user : users) {
			System.out.println(user);
		}
	}
}
````
UserDao
````java
public interface UserDao {
	int addUser(User user);
	int update(User user);
	int delete(User user);
	User getUser(Long Id);
	User findUser(String name, Integer age);
	//新增查询方法：根据年龄查询

    //MyJDBCTemplate中query的返回值设置成List存在局限性。如果用户想映射出Map呢？所以用Object最好
	List<User> selectUsers(Integer age);
}
````
UserDaoImpl2
````java
public class UserDaoImpl2 implements UserDao {

	MyJDBCTemplate jdbcTemplate = new MyJDBCTemplate();

	//增
	public int addUser(User user) {
		String sql = "insert into t_user(name, age, birthday) values (?,?,?) ";
		Object[] args = new Object[]{user.getName(), user.getAge(),
				user.getBirthday()};
		//调用jdbcTemplate的update方法
		return jdbcTemplate.update(sql, args);
	}

	//删
	public int update(User user) {
		String sql = "update t_user set name=?, age=?, birthday=? where id=? ";
		Object[] args = new Object[]{user.getName(), user.getAge(),
				user.getBirthday(), user.getId()};
		//调用jdbcTemplate的update方法
		return jdbcTemplate.update(sql, args);
	}

	//改
	public int delete(User user) {
		String sql = "delete from t_user where id=?";
		Object[] args = new Object[]{user.getId()};
		//调用jdbcTemplate的update方法
		return jdbcTemplate.update(sql, args);
	}

	public User getUser(Long id) {
		String sql = "select id, name, age, birthday from t_user where id=?";
		Object[] args = new Object[]{id};
		//调用jdbcTemplate的query方法
	// 	List list = jdbcTemplate.query(sql, args, new RowMapper() {
	// 		public Object mapRow(ResultSet rs) throws SQLException {
	// 			User user = new User();
	// 			user.setId(rs.getInt("id"));
	// 			user.setAge(rs.getInt("age"));
	// 			user.setName(rs.getString("name"));
	// 			user.setBirthday(rs.getDate("birthday"));
	// 			return user;
	// 		}
	// 	});
	// 	return (User)list.get(0);
	// }
    //调用jdbcTemplate的query方法
        Object query = jdbcTemplate.query(sql, args, new BeanHandler(user.class));
        return (User)query;

	public User findUser(String name, Integer age) {
		String sql = "select id, name, age, birthday from t_user where name=? and age=?";
		Object[] args = new Object[]{name, age};
		//调用jdbcTemplate的query方法
	// 	List list = jdbcTemplate.query(sql, args, new RowMapper() {
	// 		public Object mapRow(ResultSet rs) throws SQLException {
	// 			User user = new User();
	// 			user.setId(rs.getInt("id"));
	// 			user.setAge(rs.getInt("age"));
	// 			user.setName(rs.getString("name"));
	// 			user.setBirthday(rs.getDate("birthday"));
	// 			return user;
	// 		}
	// 	});
	// 	return (User)list.get(0);
	// }
       Object query = jdbcTemplate.query(sql, args, new BeanHandler(user.class));
        return (User)query;


	public List selectUsers(Integer age) {
		String sql = "select id, name, age, birthday from t_user where age=?";
		Object[] args = new Object[]{age};
        // new BeanHandler,每次都让用户自己写一个匿名内部类实在太烦了，而且findUser和getUser方法返回值都是User，会重复。返回值类型其实是可以穷举的，比如单个Bean，List<Bean>、Map、List<Map>等。我们预先定义几个映射器供用户使用.
        Object query = jdbcTemplate.query(sql, args, new BeanHandler(user.class));
        return (List)query;
	}
}
````
RowMapper
````java
public interface ResultSetHandler {
	//映射结果集
	Object handler(ResultSet rs) ;
}
````
````java
public class MyJDBCTemplate {
	// 增删改
	public int update(String sql, Object...args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			// sql由调用者传入
			ps = conn.prepareStatement(sql);
			// 遍历设置模板参数
			for (int i = 0; i < args.length; i++){
				ps.setObject(i + 1, args[i]);
			}
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	//查询
	public Object query(String sql, Object[] args, ResultSetHandler resultSetHandler) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			// sql由调用者传入
			ps = conn.prepareStatement(sql);
			// 遍历设置模板参数
			for (int i = 0; i < args.length; i++)
				ps.setObject(i + 1, args[i]);
			rs = ps.executeQuery();
			// 映射规则由子类传入

            //MyJDBCTemplate中query的返回值设置成List存在局限性。如果用户想映射出Map呢？所以用Object最好
			return resultSetHandler.handler(rs);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
}


````
# Listener





# 注解
````java
public @interface 注解名称{
    属性列表;
}
````

````java
public interface 注解名称 extends Annotation{
    属性列表;
}
````

# ThreadLocal

ThreadLocal其实不存东西，ThreadLocalMap的key也不是Thread，是ThreadLocal。


# Spring JDBC ORM
模板方法，建造者模式
````java
````



```flow
st=>start: Start:>http://www.google.com[blank]
e=>end:>http://www.google.com
op1=>operation: My Operation
sub1=>subroutine: My Subroutine
cond=>condition: Yes
or No?:>http://www.google.com
io=>inputoutput: catch something...
para=>parallel: parallel tasks

st->op1->cond
cond(yes)->io->e
cond(no)->para
para(path1, bottom)->sub1(right)->op1
para(path2, top)->op1
```