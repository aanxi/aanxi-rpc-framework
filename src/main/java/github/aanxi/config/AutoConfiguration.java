package github.aanxi.config;

import github.aanxi.client.ClientProxyFactory;
import github.aanxi.client.discovery.ZookeeperServiceDiscoverer;
import github.aanxi.client.net.NettyNetClient;
import github.aanxi.common.protocal.JavaSerializeMessageProtocol;
import github.aanxi.common.protocal.MessageProtocol;
import github.aanxi.properties.AARpcProperty;
import github.aanxi.server.NettyRpcServer;
import github.aanxi.server.RequestHandler;
import github.aanxi.server.RpcServer;
import github.aanxi.server.register.DefaultRpcProcessor;
import github.aanxi.server.register.ServiceRegister;
import github.aanxi.server.register.ZookeeperExportServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AutoConfiguration {
    @Bean
    public DefaultRpcProcessor defaultRpcProcessor() {
        return new DefaultRpcProcessor();
    }

    @Bean
    public ClientProxyFactory clientProxyFactory(@Autowired AARpcProperty AARpcProperty) {
        ClientProxyFactory clientProxyFactory = new ClientProxyFactory();
        // 设置服务发现者
        clientProxyFactory.setServiceDiscoverer(new ZookeeperServiceDiscoverer(AARpcProperty.getRegisterAddress()));

        // 设置支持的协议
        Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
        supportMessageProtocols.put(AARpcProperty.getProtocol(), new JavaSerializeMessageProtocol());
        clientProxyFactory.setSupportMessageProtocols(supportMessageProtocols);

        // 设置网络层实现
        clientProxyFactory.setNetClient(new NettyNetClient());
        return clientProxyFactory;
    }

    @Bean
    public ServiceRegister serviceRegister(@Autowired AARpcProperty AARpcProperty) {
        return new ZookeeperExportServiceRegister(
                AARpcProperty.getRegisterAddress(),
                AARpcProperty.getServerPort(),
                AARpcProperty.getProtocol());
    }

    @Bean
    public RequestHandler requestHandler(@Autowired ServiceRegister serviceRegister) {
        return new RequestHandler(new JavaSerializeMessageProtocol(), serviceRegister);
    }

    @Bean
    public RpcServer rpcServer(@Autowired RequestHandler requestHandler,
                               @Autowired AARpcProperty AARpcProperty) {
        return new NettyRpcServer(AARpcProperty.getServerPort(),
                AARpcProperty.getProtocol(), requestHandler);
    }

    @Bean
    public AARpcProperty AARpcProperty() {
        return new AARpcProperty();
    }
}
