【2016-03-15】


【2016-03-14】
IntelliJ IDEA Ctrl+Alt+Left/Right 失效
http://blog.csdn.net/lixwjava/article/details/45438503
======================================================================================================
	public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
			throws BeansException {

		super(parent);
		setConfigLocations(configLocations);
		if (refresh) {
			refresh();  //最重要是refresh()方法
		}
	}
=======================================================================================================
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				// Invoke factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
				initMessageSource();

				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
				onRefresh();

				// Check for listener beans and register them.
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}
		}
	}

【2016-03-11】
受这个帖子启发
http://stackoverflow.com/questions/7813741/spring-mockito-test-injection
这个地址就是springockito集成在一起的第三方jar提供方
https://bitbucket.org/kubek2k/springockito/downloads

1.王刚提供一种方式在spring的xml文件配置mock，使用的EasyMock实现的，扩展了spring标签，参考dubbo实现的

2.我自己找一个开源包2012年就实现我的想法通过标签配置在spring的xml文件
参见：D:\Code\Code_IntellijIdea\SSIBoss\src\test\resources\spring\test-orderAppCtx.xml
<mockito:mock id="itemOpService" class="com.neo.order.service.IItemOpService"/>
IItemOpService  只是一个接口类，没有时间类

3.具体单元类参见：D:\Code\Code_IntellijIdea\SSIBoss\src\test\java\com\neo\test\order\TestOrderService.java



【2016-03-10】
计划学习Mockito
研究将mock的对象注入到spring容器内，使用SpringJUnit4ClassRunner方式进行单元测试

spring中使用mockito
http://www.cnblogs.com/syxchina/p/4150879.html

官方：https://github.com/mockito/mockito
入门：5分钟了解Mockito http://liuzhijun.iteye.com/blog/1512780

Spring与Mockito组合单元测试简单实用 【未生效，还是绕不开@Autowired】
http://pwind.iteye.com/blog/1275159

Java Mocking入门—使用Mockito
http://www.importnew.com/10083.html


【2016-03-09】
参考：
http://lipeng200819861126-126-com.iteye.com/blog/2116357

1.事务依赖进入的切面方法是否加入@Transactional配置
2.private方法即使加@Transactional事务也不生效