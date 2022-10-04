package github.aanxi.properties;

public class AARpcProperty {
    /**
     * 服务注册中心
     */
    private String registerAddress = "81.71.64.70:2181";

    /**
     * 服务端暴露端口
     */
    private Integer serverPort = 18000;

    /**
     * 服务协议
     */
    private String protocol = "aanxi";

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
