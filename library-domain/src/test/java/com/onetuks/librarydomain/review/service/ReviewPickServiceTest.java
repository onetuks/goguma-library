package com.onetuks.librarydomain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.ReviewPickFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewPickServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("서평픽 등록하면 픽커와 리시버에게 포인트를 지급한다.")
  void register_CreditPoint_Test() {
    // Given
    Member picker = MemberFixture.create(101L, RoleType.USER);
    Member receiver = MemberFixture.create(201L, RoleType.USER);
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            101L, picker, ReviewFixture.create(101L, receiver, BookFixture.create(101L)));

    given(memberRepository.read(picker.memberId())).willReturn(picker);
    given(reviewRepository.read(reviewPick.review().reviewId())).willReturn(reviewPick.review());
    given(reviewPickRepository.create(any(ReviewPick.class))).willReturn(reviewPick);

    // When
    ReviewPick result =
        reviewPickService.register(picker.memberId(), reviewPick.review().reviewId());

    // Then
    assertAll(
        () -> assertThat(result.reviewPickId()).isEqualTo(reviewPick.reviewPickId()),
        () -> assertThat(result.member().memberId()).isEqualTo(reviewPick.member().memberId()),
        () -> assertThat(result.review().reviewId()).isEqualTo(reviewPick.review().reviewId()));

    verify(pointService, times(1)).creditPointForReviewPick(picker.memberId(), receiver.memberId());
  }

  @Test
  @DisplayName("서평픽 취소하면 서평픽이 취소되고, 픽커의 포인트를 차감한다.")
  void remove_DebitPoint_Test() {
    // Given
    Member picker = MemberFixture.create(103L, RoleType.USER);
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            103L,
            picker,
            ReviewFixture.create(
                103L, MemberFixture.create(203L, RoleType.USER), BookFixture.create(103L)));

    given(memberRepository.read(picker.memberId())).willReturn(picker);
    given(reviewPickRepository.read(reviewPick.reviewPickId())).willReturn(reviewPick);

    // When
    reviewPickService.remove(picker.memberId(), reviewPick.reviewPickId());

    // Then
    verify(reviewPickRepository, times(1)).delete(reviewPick.reviewPickId());
    verify(pointService, times(1)).debitPointForReviewPick(picker.memberId());
  }

  @Test
  @DisplayName("권한 없는 멤버가 서평픽 취소하면 에외를 던진다.")
  void remove_AccessDenied_ExceptionThrown() {
    // Given
    Member notPicker = MemberFixture.create(103L, RoleType.USER);
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            103L,
            MemberFixture.create(303L, RoleType.USER),
            ReviewFixture.create(
                103L, MemberFixture.create(203L, RoleType.USER), BookFixture.create(103L)));

    given(memberRepository.read(notPicker.memberId())).willReturn(notPicker);
    given(reviewPickRepository.read(reviewPick.reviewPickId())).willReturn(reviewPick);

    // When & Then
    assertThatThrownBy(
            () -> reviewPickService.remove(notPicker.memberId(), reviewPick.reviewPickId()))
        .isInstanceOf(ApiAccessDeniedException.class);

    verify(reviewPickRepository, never()).delete(reviewPick.reviewPickId());
    verify(pointService, never()).debitPointForReviewPick(notPicker.memberId());
  }
}
