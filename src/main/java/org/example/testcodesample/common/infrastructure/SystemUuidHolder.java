package org.example.testcodesample.common.infrastructure;

import java.util.UUID;
import org.example.testcodesample.common.service.port.UuidHolder;
import org.springframework.stereotype.Component;

@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
