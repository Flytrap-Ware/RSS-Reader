package com.flytrap.rssreader.presentation.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.folder.business.service.FolderSubscribeService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.subscribe.business.facade.SubscribeInFolderFacade;
import com.flytrap.rssreader.fixture.FolderFixtureFactory;
import com.flytrap.rssreader.fixture.SubscribeFixtureFactory;
import com.flytrap.rssreader.api.subscribe.business.service.SubscribeService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("SubscribeInFolderFacade 테스트")
@ExtendWith(MockitoExtension.class)
class SubscribeInFolderFacadeTest {

    @Mock
    SubscribeService subscribeService;
    @Mock
    FolderSubscribeService folderSubscribeService;
    @InjectMocks
    SubscribeInFolderFacade subscribeInFolderFacade;

    @Nested
    @DisplayName("addSubscribesInFolder 실행시")
    class GetSubscribesInFolder {

        @Test
        @DisplayName("폴더에 속한 블로그 목록을 반환한다.")
        void addSubscribesInFolder_success() {
            // given
            when(folderSubscribeService.getFolderSubscribeIds(any()))
                    .thenReturn(Map.of(
                            FolderFixtureFactory.generateFolder(), List.of(1L, 2L, 3L)
                    ));
            when(subscribeService.read(any()))
                    .thenReturn(List.of(
                    SubscribeFixtureFactory.generateSubscribe(),
                    SubscribeFixtureFactory.generateSubscribe(2L),
                    SubscribeFixtureFactory.generateSubscribe(3L)));

            // when
            List<? extends Folder> folders = subscribeInFolderFacade.addSubscribesInFolder(List.of(FolderFixtureFactory.generateFolder()));

            // then
            assertEquals(3, folders.get(0).getSubscribes().size());
        }

        @Test
        @DisplayName("폴더에 속한 블로그 목록이 없으면 빈 목록을 반환한다.")
        void addSubscribesInFolder_empty() {
            // given
            when(folderSubscribeService.getFolderSubscribeIds(any()))
                    .thenReturn(Map.of(
                            FolderFixtureFactory.generateFolder(), List.of()
                    ));

            // when
            List<? extends Folder> folders = subscribeInFolderFacade.addSubscribesInFolder(
                    List.of(FolderFixtureFactory.generateFolder()));

            // then
            assertEquals(0, folders.get(0).getSubscribes().size());
        }

        @Test
        @DisplayName("폴더가 없으면 빈 목록을 반환한다.")
        void addSubscribesInFolder_emptyFolder() {
            // given

            // when
            List<? extends Folder> folders = subscribeInFolderFacade.addSubscribesInFolder(List.of());

            // then
            assertEquals(0, folders.size());
        }
    }
}
