package io.handicrafty.sixteen.common.jpa.model

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class CreateUpdateTimeAuditEntity(
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    open var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    open var updatedAt: LocalDateTime? = null,
)
