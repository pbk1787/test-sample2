package org.example.testcodesample.common.infrastructure;

import java.time.Clock;
import org.example.testcodesample.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public long millis() {
        return Clock.systemUTC().millis();
    }
}
