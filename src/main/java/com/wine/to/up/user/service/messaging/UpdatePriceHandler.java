package com.wine.to.up.user.service.messaging;

import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.catalog.service.api.message.UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.message.UserTokensOuterClass.UserTokens;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import com.wine.to.up.user.service.service.FavoritesService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdatePriceHandler implements KafkaMessageHandler<UpdatePriceMessageSentEvent> {
    private final FavoritesService favoritesService;
    private final KafkaMessageSender<WinePriceUpdatedWithTokensEvent> wineReviewedEventSender;

    @Autowired
    public UpdatePriceHandler(KafkaMessageSender<WinePriceUpdatedWithTokensEvent> wineReviewedEventSender,
                              FavoritesService favoritesService) {
        this.wineReviewedEventSender = wineReviewedEventSender;
        this.favoritesService = favoritesService;
    }

    @Override
    public void handle(UpdatePriceMessageSentEvent message) {
        // todo: setFCM and IOS tokens
        List<UserTokens> userTokens = new ArrayList<>();
//        favoritesService.findUserIdsByWineId(message.getId()).forEach(
//            s -> userTokens.add(
//                UserTokens.newBuilder()
//                    .setUserId(s)
//                    .addIosTokens("testIosToken")
//                    .build()
//            )
//        );
        // todo remove
        userTokens.add(UserTokens.newBuilder()
            .setUserId(1L)
            .addFcmTokens("elKDMqIgQ96O8GjukBv4FB:APA91bGgV-S9FS708ZjQhVP98J3T8Lc-" +
                "FzITpawvzgEgnpr73x_E4xOB0EGzZIPdyijuWaD5n0q0ItHVZRCFv4gPQnoo7YMBPZ" +
                "lpPYuNd7o35_mR4745n-m86g_jqyZ_VcE3Xi14qNLI")
            .build());

        wineReviewedEventSender.sendMessage(WinePriceUpdatedWithTokensEvent.newBuilder()
            .setWineId(message.getId())
            .setWineName(message.getName())
            .setNewWinePrice((float) message.getPrice())
            .addAllUserTokens(userTokens)
            .build());
    }
}
