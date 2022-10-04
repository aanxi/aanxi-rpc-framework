package github.aanxi.client;

import github.aanxi.client.discovery.ServiceDiscoverer;
import github.aanxi.client.net.NetClient;
import github.aanxi.common.protocal.MessageProtocol;
import github.aanxi.common.protocal.Request;
import github.aanxi.common.protocal.Response;
import github.aanxi.exception.AAException;
import github.aanxi.service.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.reflect.Proxy.newProxyInstance;

public class ClientProxyFactory {
    private ServiceDiscoverer serviceDiscoverer;

    private Map<String, MessageProtocol> supportMessageProtocols;

    private NetClient netClient;

    private Map<Class<?>, Object> objectCache = new HashMap<>();

    public ServiceDiscoverer getServiceDiscoverer() {
        return serviceDiscoverer;
    }

    public void setServiceDiscoverer(ServiceDiscoverer serviceDiscoverer) {
        this.serviceDiscoverer = serviceDiscoverer;
    }

    public Map<String, MessageProtocol> getSupportMessageProtocols() {
        return supportMessageProtocols;
    }

    public void setSupportMessageProtocols(Map<String, MessageProtocol> supportMessageProtocols) {
        this.supportMessageProtocols = supportMessageProtocols;
    }

    public NetClient getNetClient() {
        return netClient;
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    public Map<Class<?>, Object> getObjectCache() {
        return objectCache;
    }

    public void setObjectCache(Map<Class<?>, Object> objectCache) {
        this.objectCache = objectCache;
    }

    /**
     * 通过Java动态代理获取服务代理类
     *
     * @param clazz 被代理类Class
     * @param <T>   泛型
     * @return 服务代理类
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) this.objectCache.computeIfAbsent(clazz,
                cls -> newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new ClientInvocationHandler(cls)));
    }

    // getter setter ...

    /**
     * 客户端服务代理类invoke函数细节实现
     */
    private class ClientInvocationHandler implements InvocationHandler {
        private Class<?> clazz;

        private Random random = new Random();

        public ClientInvocationHandler(Class<?> clazz) {
            super();
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Exception {

            if (method.getName().equals("toString")) {
                return proxy.getClass().toString();
            }

            if (method.getName().equals("hashCode")) {
                return 0;
            }

            // 1、获得服务信息
            String serviceName = this.clazz.getName();
            List<Service> services = serviceDiscoverer.getServices(serviceName);

            if (services == null || services.isEmpty()) {
                throw new AAException("No provider available!");
            }

            // 随机选择一个服务提供者（软负载均衡）
            Service service = services.get(random.nextInt(services.size()));

            // 2、构造request对象
            Request req = new Request();
            req.setServiceName(service.getName());
            req.setMethod(method.getName());
            req.setParameterTypes(method.getParameterTypes());
            req.setParameters(args);

            // 3、协议层编组
            // 获得该方法对应的协议
            MessageProtocol protocol = supportMessageProtocols.get(service.getProtocol());
            // 编组请求
            byte[] data = protocol.marshallingRequest(req);

            // 4、调用网络层发送请求
            byte[] repData = netClient.sendRequest(data, service);

            // 5解组响应消息
            Response rsp = protocol.unmarshallingResponse(repData);

            // 6、结果处理
            if (rsp.getException() != null) {
                throw rsp.getException();
            }
            return rsp.getReturnValue();
        }
    }
}
