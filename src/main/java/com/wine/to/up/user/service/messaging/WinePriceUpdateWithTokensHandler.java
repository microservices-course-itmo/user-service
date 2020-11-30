package com.wine.to.up.user.service.messaging;

import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// for testing purposes todo remove
@Component
@Slf4j
public class WinePriceUpdateWithTokensHandler implements KafkaMessageHandler<WinePriceUpdatedWithTokensEvent> {
    @Override
    public void handle(WinePriceUpdatedWithTokensEvent winePriceUpdatedWithTokensEvent) {
        log.info(winePriceUpdatedWithTokensEvent.getWineId());
        log.info(String.valueOf(winePriceUpdatedWithTokensEvent.getNewWinePrice()));
        log.info(winePriceUpdatedWithTokensEvent.getWineName());
        log.info(String.valueOf(winePriceUpdatedWithTokensEvent.getUserTokensList()));
    }
}
