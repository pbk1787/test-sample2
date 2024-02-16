package org.example.testcodesample.mock;

import lombok.RequiredArgsConstructor;
import org.example.testcodesample.common.service.port.ClockHolder;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final long millis;

    @Override
    public long millis() {
        return millis;
    }
}
