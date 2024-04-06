package com.flytrap.rssreader.service;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;

import com.flytrap.rssreader.api.post.business.service.PostOpenService;
import com.flytrap.rssreader.api.post.infrastructure.output.OpenPostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostOpenService 단위 테스트")
class PostOpenServiceTest {

    @Mock
    PostOpenRepository postOpenRepository;
    @InjectMocks
    PostOpenService postOpenService;

    @Nested
    class open {

    }

    @Nested
    class countOpens {

        @Test
        @DisplayName("countOpens 메서드는 구독자들의 포스트 오픈 횟수를 조회한다.")
        void countOpens_shouldCountOpensOfSubscribers() {
            // given
            when(postOpenRepository.countOpens(anyLong(), anyList()))
                    .thenReturn(List.of(new OpenPostCountOutput() {
                        @Override
                        public long getSubscribeId() {
                            return 1L;
                        }

                        @Override
                        public int getPostCount() {
                            return 3;
                        }
                    }));

            // when
            var result = postOpenService.countOpens(1L, List.of(1L));

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.get(1L).getPostCount()).isEqualTo(3);
                softAssertions.assertThat(result).hasSize(1);
            });
        }

        @Test
        @DisplayName("사용자가 2개 블로그에서 포스트를 읽었을 경우, 블로그별로 오픈 횟수를 조회한다.")
        void countOpens_shouldCountOpensOfSubscribersByBlog() {
            // given
            when(postOpenRepository.countOpens(anyLong(), anyList()))
                    .thenReturn(List.of(new OpenPostCountOutput() {
                        @Override
                        public long getSubscribeId() {
                            return 1L;
                        }

                        @Override
                        public int getPostCount() {
                            return 3;
                        }
                    }, new OpenPostCountOutput() {
                        @Override
                        public long getSubscribeId() {
                            return 2L;
                        }

                        @Override
                        public int getPostCount() {
                            return 2;
                        }
                    }));

            // when
            var result = postOpenService.countOpens(1L, List.of(1L, 2L));

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.get(1L).getPostCount()).isEqualTo(3);
                softAssertions.assertThat(result.get(2L).getPostCount()).isEqualTo(2);
                softAssertions.assertThat(result).hasSize(2);
            });
        }
    }

    @Test
    void deleteRead() {
    }
}
