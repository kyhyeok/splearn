package kimspring.splearn.domain.member;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kimspring.splearn.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.util.Assert.isTrue;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {
  private Profile profile;

  private String introduction;

  private LocalDateTime registeredAt;

  private LocalDateTime activatedAt;

  private LocalDateTime deactivatedAt;

  static MemberDetail create() {
    MemberDetail memberDetail = new MemberDetail();
    memberDetail.registeredAt = LocalDateTime.now();
    return memberDetail;
  }

  void activatedAt() {
    isTrue(activatedAt == null, "이미 activatedAt은 설정되었습니다");
    this.activatedAt = LocalDateTime.now();
  }

  void deactivatedAt() {
    isTrue(deactivatedAt == null, "이미 deactivatedAt은 설정되었습니다");
    this.deactivatedAt = LocalDateTime.now();
  }

  void updateInfo(MemberInfoUpdateRequest updateRequest) {
    this.profile = convertToProfile(updateRequest.profileAddress());
    this.introduction = Objects.requireNonNull(updateRequest.introduction());
  }

  private Profile convertToProfile(@NotNull @Size(max = 15) String profileAddress) {
    if (profileAddress == null || profileAddress.isEmpty()) {
      return null;
    }

    return new Profile(profileAddress);
  }
}
