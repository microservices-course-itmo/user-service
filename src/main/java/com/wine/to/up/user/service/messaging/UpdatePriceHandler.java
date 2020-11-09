package com.wine.to.up.user.service.messaging;

import com.wine.to.up.catalog.service.api.message.UpdatePriceEventOuterClass.UpdatePriceEvent;
import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.message.UserTokensOuterClass.UserTokens;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import com.wine.to.up.user.service.service.SubscriptionService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdatePriceHandler implements KafkaMessageHandler<UpdatePriceEvent> {
    private final SubscriptionService subscriptionService;
    private final KafkaMessageSender<WinePriceUpdatedWithTokensEvent> wineReviewedEventSender;

    @Autowired
    public UpdatePriceHandler(KafkaMessageSender<WinePriceUpdatedWithTokensEvent> wineReviewedEventSender,
                              SubscriptionService subscriptionService) {
        this.wineReviewedEventSender = wineReviewedEventSender;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void handle(UpdatePriceEvent message) {
        // todo: setFCM and IOS tokens
        List<UserTokens> userTokens = new ArrayList<>();
        subscriptionService.findUserIdsByWineId(message.getId()).forEach(
            s -> userTokens.add(
                UserTokens.newBuilder()
                    .setUserId(s)
                    .addIosTokens("testIosToken")
                    .build()
            )
        );

        wineReviewedEventSender.sendMessage(WinePriceUpdatedWithTokensEvent.newBuilder()
            .setWineId(message.getId())
            .setWineName(message.getName())
            .setNewWinePrice((float) message.getPrice())
            .addAllUserTokens(userTokens)
            .build());
    }
}
