package github.aanxi.service;

public class Service {
    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务协议
     */
    private String protocol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 服务地址，格式：IP:Port
     */
    private String address;

    public String getAddress() {
        return address;
    }
}
