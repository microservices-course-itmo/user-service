package com.wine.to.up.user.service.messaging;

import com.wine.to.up.catalog.service.api.message.UpdatePriceEventOuterClass.UpdatePriceEvent;
import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.message.UserTokensOuterClass.UserTokens;
import com.wine.to.up.user.service.api.message.WineReviewedEventOuterClass.WineReviewedEvent;
import com.wine.to.up.user.service.service.SubscriptionService;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdatePriceHandler implements KafkaMessageHandler<UpdatePriceEvent> {
    public final SubscriptionService subscriptionService;
    private final KafkaMessageSender<WineReviewedEvent> wineReviewedEventSender;

    @Autowired
    public UpdatePriceHandler(KafkaMessageSender<WineReviewedEvent> wineReviewedEventSender,
                              SubscriptionService subscriptionService) {
        this.wineReviewedEventSender = wineReviewedEventSender;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void handle(UpdatePriceEvent message) {

        // todo: setFCM and IOS tokens
        List<UserTokens> userTokens = new ArrayList<>();
        subscriptionService.findUserIdsByWineId(Long.parseLong(message.getId())).stream().forEach(
                s -> UserTokens.newBuilder().setUserId(s).build()
        );

        wineReviewedEventSender.sendMessage(WineReviewedEvent.newBuilder()
            .setWineId(Long.parseLong(message.getId()))
            .setWineName(message.getName())
            .setNewWinePrice((float) message.getPrice())
            .addAllUserTokens(userTokens)
            .build());
    }
}
