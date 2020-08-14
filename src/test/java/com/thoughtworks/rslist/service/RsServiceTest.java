package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Trade;
import com.thoughtworks.rslist.domain.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class RsServiceTest {
  RsService rsService;

  @Mock RsEventRepository rsEventRepository;
  @Mock UserRepository userRepository;
  @Mock VoteRepository voteRepository;
  @Mock TradeRepository tradeRepository;
  LocalDateTime localDateTime;
  Vote vote;

  @BeforeEach
  void setUp() {
    initMocks(this);
    rsService = new RsService(rsEventRepository, userRepository, voteRepository, tradeRepository);
    localDateTime = LocalDateTime.now();
    vote = Vote.builder().voteNum(2).rsEventId(1).time(localDateTime).userId(1).build();
  }

  @Test
  void shouldVoteSuccess() {
    // given

    UserDto userDto =
        UserDto.builder()
            .voteNum(5)
            .phone("18888888888")
            .gender("female")
            .email("a@b.com")
            .age(19)
            .userName("xiaoli")
            .id(2)
            .build();
    RsEventDto rsEventDto =
        RsEventDto.builder()
            .eventName("event name")
            .id(1)
            .keyword("keyword")
            .voteNum(2)
            .user(userDto)
            .build();

    when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventDto));
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(userDto));
    // when
    rsService.vote(vote, 1);
    // then
    verify(voteRepository)
        .save(
            VoteDto.builder()
                .num(2)
                .localDateTime(localDateTime)
                .user(userDto)
                .rsEvent(rsEventDto)
                .build());
    verify(userRepository).save(userDto);
    verify(rsEventRepository).save(rsEventDto);
  }

  @Test
  void shouldThrowExceptionWhenUserNotExist() {
    // given
    when(rsEventRepository.findById(anyInt())).thenReturn(Optional.empty());
    when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
    //when&then
    assertThrows(
        RuntimeException.class,
        () -> {
          rsService.vote(vote, 1);
        });
  }

  @Test
  void should_sort_rsEvent_when_save_by_voteNum() {
    List<RsEventDto> rsEventDtoList = new ArrayList<>();
    UserDto userDto = UserDto.builder().userName("zkj").age(25).gender("male").email("4381@qq.com").phone("18888888888").build();
    for (int i = 0; i < 5; i++) {
       rsEventDtoList.add(RsEventDto.builder().eventName("第" + (i + 1) + "条").keyword("twU").voteNum(10 - i).user(userDto).build());
    }
    when(rsEventRepository.findAll()).thenReturn(rsEventDtoList);
    assertEquals(rsService.sortRsEventByVoteNum(rsEventRepository.findAll()).get(0).getEventName(), "第1条");
    rsEventDtoList.add(RsEventDto.builder().eventName("存入一条").keyword("twU").voteNum(14).user(userDto).build());
    assertEquals(rsService.getHadRankedRsEventAfterSave(rsEventRepository.findAll()).get(0).getEventName(), "存入一条");
  }

  @Test
  void should_return_new_rsEvent_when_buy_rsEvent_rank_given_amount_more_than_origin_raEvent_amount() {
    List<RsEventDto> rsEventDtoList = new ArrayList<>();
    UserDto userDto = UserDto.builder().userName("zkj").age(25).gender("male").email("4381@qq.com").phone("18888888888").build();
    for (int i = 0; i < 5; i++) {
      rsEventDtoList.add(RsEventDto.builder().id(i + 1).eventName("第" + (i + 1) + "条").keyword("twU").voteNum(10 - i).user(userDto).build());
    }
    when(rsEventRepository.findById(5)).thenReturn(Optional.of(rsEventDtoList.get(4)));
    when(rsEventRepository.findByRank(4)).thenReturn(Optional.of(rsService.sortRsEventByVoteNum(rsEventDtoList).get(3)));
    Trade trade = Trade.builder()
            .amount(100)
            .rank(4)
            .build();
    rsService.buy(trade, 5);
    verify(rsEventRepository)
            .save(
                    RsEventDto.builder()
                            .user(userDto)
                            .eventName("第5条")
                            .keyword("twU")
                            .voteNum(7)
                            .id(5)
                            .rank(4)
                            .build());
  }

  @Test
  void should_throw_exception_when_buy_rsEvent_rank_given_amount_not_more_than_origin_raEvent_amount() {
    List<RsEventDto> rsEventDtoList = new ArrayList<>();
    UserDto userDto = UserDto.builder().userName("zkj").age(25).gender("male").email("4381@qq.com").phone("18888888888").build();
    for (int i = 0; i < 5; i++) {
      rsEventDtoList.add(RsEventDto.builder().id(i + 1).eventName("第" + (i + 1) + "条").keyword("twU").voteNum(10 - i).user(userDto).amount(200).build());
    }
    when(rsEventRepository.findById(5)).thenReturn(Optional.of(rsEventDtoList.get(4)));
    when(rsEventRepository.findByRank(4)).thenReturn(Optional.of(rsService.sortRsEventByVoteNum(rsEventDtoList).get(3)));
    Trade trade = Trade.builder()
            .amount(100)
            .rank(4)
            .build();
    assertThrows(BuyRsEventRankFailException.class, () ->rsService.buy(trade, 5));
  }
}
