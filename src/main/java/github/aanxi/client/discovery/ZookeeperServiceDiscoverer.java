package github.aanxi.client.discovery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import github.aanxi.service.Service;
import github.aanxi.common.constants.ZKConstant;
import github.aanxi.common.serializer.ZookeeperSerializer;
import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ZookeeperServiceDiscoverer implements ServiceDiscoverer{

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private ZkClient zkClient;

    public ZookeeperServiceDiscoverer(String zkAddress) {
        zkClient = new ZkClient(zkAddress);
        zkClient.setZkSerializer(new ZookeeperSerializer());
    }

    @Override
    public List<Service> getServices(String name){
        String servicePath = ZKConstant.ZK_SERVICE_PATH + ZKConstant.PATH_DELIMITER + name + "/service";
        List<String> children = zkClient.getChildren(servicePath);
        return Optional.ofNullable(children).orElse(new ArrayList<>()).stream().map(str -> {
            String deCh = null;
            try {
                deCh = URLDecoder.decode(str,ZKConstant.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                return OBJECT_MAPPER.readValue(deCh, Service.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }
}
