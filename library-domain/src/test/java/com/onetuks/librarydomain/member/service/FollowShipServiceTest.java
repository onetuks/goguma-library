package com.onetuks.librarydomain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.FollowShipFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.FollowShip;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FollowShipServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("팔로우를 등록하면 해당 팔로워 멤버의 팔로잉 카운트와 팔로이 멤버의 팔로워 카운트가 증가한다.")
  void register_Test() {
    // Given
    Member member = MemberFixture.create(101L, RoleType.USER);
    Member followee = MemberFixture.create(102L, RoleType.USER);
    FollowShip followShip = FollowShipFixture.create(101L, member, followee);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(memberRepository.read(followee.memberId())).willReturn(followee);
    given(followShipRepository.create(any(FollowShip.class))).willReturn(followShip);

    // When
    FollowShip result = followShipService.register(member.memberId(), followShip.followShipId());

    // Then
    assertAll(
        () -> assertThat(result.followShipId()).isNotNull(),
        () -> assertThat(result.follower().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.followee().memberId()).isEqualTo(followee.memberId()));
  }

  @Test
  @DisplayName("팔로우를 취소한다.")
  void remove_Test() {
    // Given
    Member member = MemberFixture.create(101L, RoleType.USER);
    Member followee = MemberFixture.create(102L, RoleType.USER);
    FollowShip followShip = FollowShipFixture.create(101L, member, followee);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(followShipRepository.read(followShip.followShipId())).willReturn(followShip);

    // When
    followShipService.remove(member.memberId(), followShip.followShipId());

    // Then
    verify(followShipRepository, times(1)).delete(followShip.followShipId());
  }

  @Test
  @DisplayName("권한 없는 멤버가 팔로우 취소시 예외를 던진다.")
  void remove_NotAuth_ExceptionThrown() {
    // Given
    long notAuthMemberId = 999L;
    Member member = MemberFixture.create(101L, RoleType.USER);
    Member followee = MemberFixture.create(102L, RoleType.USER);
    FollowShip followShip = FollowShipFixture.create(101L, member, followee);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(followShipRepository.read(followShip.followShipId())).willReturn(followShip);

    // When & Then
    assertThatThrownBy(() -> followShipService.remove(notAuthMemberId, followShip.followShipId()))
        .isInstanceOf(ApiAccessDeniedException.class);
  }
}
