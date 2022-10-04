package github.aanxi.common.serializer;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.nio.charset.StandardCharsets;

public class ZookeeperSerializer implements ZkSerializer {
    public byte[] serialize(Object o) throws ZkMarshallingError {
        return String.valueOf(o).getBytes(StandardCharsets.UTF_8);
    }

    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
