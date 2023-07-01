package io.handicrafty.sixteen.user.model

import io.handicrafty.sixteen.common.jpa.enumeration.Gender
import io.handicrafty.sixteen.common.jpa.enumeration.Mbti
import io.handicrafty.sixteen.common.jpa.model.CreateUpdateTimeAuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.Hibernate

@Entity
@Table(name = "user")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    @field:NotBlank(message = "닉네임은 비어있을 수 없습니다.")
    @field:Size(min = 0, max = 12, message = "닉네임은 12자 이내여야 합니다.")
    @Column(name = "nickname", nullable = false, length = 12)
    open var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti", nullable = false, length = 4)
    open var mbti: Mbti,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 6)
    open var gender: Gender,

    @Column(name = "age", nullable = false)
    open var age: Int,

    @Column(name = "modify_count", nullable = false)
    open var modifyCount: Int = 0,

    @Column(name = "like_and_feedback_count", nullable = false)
    open var likeAndFeedbackCount: Long = 0,

    // TODO: primaryBadge 매핑
): CreateUpdateTimeAuditEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , createdAt = $createdAt , updatedAt = $updatedAt )"
    }
}
