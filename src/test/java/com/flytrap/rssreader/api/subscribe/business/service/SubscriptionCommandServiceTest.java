package com.flytrap.rssreader.api.subscribe.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.CustomServiceTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.api.parser.RssSubscribeParser;
import com.flytrap.rssreader.api.parser.dto.RssSourceData;
import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionCommand;
import com.flytrap.rssreader.global.exception.domain.DuplicateDomainException;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@CustomServiceTest
class SubscriptionCommandServiceTest {

    @Autowired
    SubscriptionCommandService subscriptionCommandService;

    @Autowired
    FolderCommand folderCommand;

    @Autowired
    SubscriptionCommand subscriptionCommand;

    @Autowired
    FolderQuery folderQuery;

    @MockBean
    RssSubscribeParser rssSubscribeParser;

    @Nested
    class AddSubscriptionToFolder {

        @Test
        void 폴더에_구독을_추가할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var rssUrl = "URL";
            var myFolder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(accountId)
                    .build()
            );
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));

            // when
            var subscription = subscriptionCommandService
                .addSubscriptionToFolder(accountId, myFolder.getId(), rssUrl);

            // then
            assertThat(subscription.getId()).isNotNull();
        }

        @Test
        void 접근_불가능한_폴더에_구독을_추가할_수_없다() {
            // given
            var accountId = new AccountId(1L);
            var rssUrl = "URL";
            var myFolder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(new AccountId(999L))
                    .build()
            );

            // when
            Executable subscriptionExecutable = () -> subscriptionCommandService
                .addSubscriptionToFolder(accountId, myFolder.getId(), rssUrl);

            // then
            assertThrows(ForbiddenAccessFolderException.class, subscriptionExecutable);
        }

        @Test
        void 한_폴더에_구독을_중복으로_추가할_수_없다() {
            // given
            var accountId = new AccountId(1L);
            var rssUrl = "URL";
            var myFolder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(accountId)
                    .build()
            );
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));
            var mySubscription = subscriptionCommand
                .createFrom(myFolder.getId(), rssUrl);

            // when
            Executable subscriptionExecutable = () -> subscriptionCommandService
                .addSubscriptionToFolder(accountId, myFolder.getId(), rssUrl);

            // then
            assertThrows(DuplicateDomainException.class, subscriptionExecutable);
        }
    }

    @Nested
    class RemoveSubscriptionToFolder {

        @Test
        void 폴더에_추가된_구독을_제거할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var rssUrl = "URL";
            var myFolder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(accountId)
                    .build()
            );
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));
            var mySubscription = subscriptionCommand
                .createFrom(myFolder.getId(), rssUrl);

            // when
            subscriptionCommandService
                .removeSubscriptionToFolder(accountId, myFolder.getId(), mySubscription.getId());
            var folderResult = folderQuery.read(myFolder.getId()).get();

            // then
            assertThat(folderResult.getSubscriptions()).hasSize(0);
        }

        @Test
        void 접근_불가능한_폴더의_구독은_삭제할_수_없다() {
            // given
            var accountId = new AccountId(1L);
            var rssUrl = "URL";
            var myFolder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(new AccountId(999L))
                    .build()
            );
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));
            var mySubscription = subscriptionCommand
                .createFrom(myFolder.getId(), rssUrl);

            // when
            Executable subscriptionExecutable = () -> subscriptionCommandService
                .removeSubscriptionToFolder(accountId, myFolder.getId(), mySubscription.getId());

            // then
            assertThrows(ForbiddenAccessFolderException.class, subscriptionExecutable);
        }
    }
}