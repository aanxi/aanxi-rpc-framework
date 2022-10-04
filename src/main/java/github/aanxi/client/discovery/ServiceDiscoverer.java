package github.aanxi.client.discovery;

import github.aanxi.service.Service;

import java.io.IOException;
import java.util.List;

public interface ServiceDiscoverer {
    List<Service> getServices(String name) throws IOException;
}
