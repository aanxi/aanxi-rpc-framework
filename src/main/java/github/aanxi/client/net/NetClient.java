package github.aanxi.client.net;

import github.aanxi.service.Service;

public interface NetClient {
    byte[] sendRequest(byte[] data, Service service) throws InterruptedException;
}
