package com.wine.to.up.user.service.messaging;

import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.catalog.service.api.message.UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.message.UserTokensOuterClass.UserTokens;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import com.wine.to.up.user.service.domain.entity.NotificationTokenType;
import com.wine.to.up.user.service.service.FavoritesService;
import java.util.ArrayList;
import java.util.List;

import com.wine.to.up.user.service.service.NotificationTokensService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdatePriceHandler implements KafkaMessageHandler<UpdatePriceMessageSentEvent> {
    private final FavoritesService favoritesService;
    private final NotificationTokensService notificationTokensService;
    private final KafkaMessageSender<WinePriceUpdatedWithTokensEvent> wineReviewedEventSender;

    @Autowired
    public UpdatePriceHandler(KafkaMessageSender<WinePriceUpdatedWithTokensEvent> wineReviewedEventSender,
                              FavoritesService favoritesService, NotificationTokensService notificationTokensService) {
        this.wineReviewedEventSender = wineReviewedEventSender;
        this.favoritesService = favoritesService;
        this.notificationTokensService = notificationTokensService;
    }

    @Override
    public void handle(UpdatePriceMessageSentEvent message) {
        List<UserTokens> userTokens = new ArrayList<>();
        favoritesService.findUserIdsByWineId(message.getId()).forEach(
            s -> userTokens.add(
                UserTokens.newBuilder()
                    .setUserId(s)
                    .addAllFcmTokens(notificationTokensService.getAllTokensByTypeAndUserId(s, NotificationTokenType.FCM_TOKEN))
                    .addAllIosTokens(notificationTokensService.getAllTokensByTypeAndUserId(s, NotificationTokenType.IOS_TOKEN))
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
