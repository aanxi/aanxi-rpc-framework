package github.aanxi.common.protocal;

public interface MessageProtocol {
    /**
     * 编组请求
     *
     * @param req 请求信息
     * @return 请求字节数组
     * @throws Exception 编组请求异常
     */
    byte[] marshallingRequest(Request req) throws Exception;

    /**
     * 解组请求
     *
     * @param data 请求字节数组
     * @return 请求信息
     * @throws Exception 解组请求异常
     */
    Request unmarshallingRequest(byte[] data) throws Exception;

    /**
     * 编组响应
     *
     * @param rsp 响应信息
     * @return 响应字节数组
     * @throws Exception 编组响应异常
     */
    byte[] marshallingResponse(Response rsp) throws Exception;

    /**
     * 解组响应
     *
     * @param data 响应字节数组
     * @return 响应信息
     * @throws Exception 解组响应异常
     */
    Response unmarshallingResponse(byte[] data) throws Exception;
}
