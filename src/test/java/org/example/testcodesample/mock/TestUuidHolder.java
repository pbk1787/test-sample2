package org.example.testcodesample.mock;

import lombok.RequiredArgsConstructor;
import org.example.testcodesample.common.service.port.UuidHolder;

@RequiredArgsConstructor
public class TestUuidHolder implements UuidHolder {

    private final String uuid;

    @Override
    public String random() {
        return uuid;
    }
}
