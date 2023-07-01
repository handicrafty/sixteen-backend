package io.handicrafty.sixteen.user.repository

import io.handicrafty.sixteen.common.jpa.configuration.JpaConfiguration
import io.handicrafty.sixteen.common.jpa.enumeration.Gender
import io.handicrafty.sixteen.common.jpa.enumeration.Mbti
import io.handicrafty.sixteen.user.model.User
import jakarta.validation.ConstraintViolationException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainerProvider
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * 사용자 엔티티의 JPA 레포지토리 테스트 Test Container 기반
 *
 * @see UserJpaRepository
 */
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfiguration::class, ValidationAutoConfiguration::class)
@DataJpaTest
class UserJpaRepositoryTest {

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Test
    fun `사용자 엔티티를 생성하고 이를 조회할 수 있다`() {
        // given
        val user = User(
            nickname = "감자감자",
            mbti = Mbti.ENFP,
            gender = Gender.MALE,
            age = 30,
        )

        // when
        val savedUser = userJpaRepository.save(user)

        // then
        val softAssertion = SoftAssertions()
        softAssertion.assertThat(savedUser.id).isNotNull
        softAssertion.assertThat(savedUser.nickname).isEqualTo("감자감자")
        softAssertion.assertThat(savedUser.mbti).isEqualTo(Mbti.ENFP)
        softAssertion.assertThat(savedUser.gender).isEqualTo(Gender.MALE)
        softAssertion.assertThat(savedUser.age).isEqualTo(30)
        softAssertion.assertThat(savedUser.modifyCount).isEqualTo(0)
        softAssertion.assertThat(savedUser.likeAndFeedbackCount).isEqualTo(0)
        softAssertion.assertThat(savedUser.createdAt).isNotNull
        softAssertion.assertThat(savedUser.updatedAt).isNotNull
        softAssertion.assertAll()
    }

    @Test
    fun `닉네임의 최대 글자수는 12자이고, 이를 초과하면 예외가 발생한다`() {
        // given 닉네임이 13자인 사용자가 있다.
        val user = User(
            nickname = "배고픈하마가배고픈하마를1",
            mbti = Mbti.ESTJ,
            gender = Gender.FEMALE,
            age = 10,
        )

        // when & then 사용자를 저장하려고 하면 예외가 발생된다.
        assertThatThrownBy {
            userJpaRepository.save(user)
        }.isInstanceOf(ConstraintViolationException::class.java)
            .hasMessageContaining("닉네임은 12자 이내여야 합니다.")
    }

    @ValueSource(strings = ["", " ", "\t", "\n"])
    @ParameterizedTest
    fun `닉네임은 비어있을 수 없고, 이를 위반하면 예외가 발생한다`(blankNickname: String) {
        // given 닉네임이 빈 문자열인 사용자가 있다.
        val user = User(
            nickname = blankNickname,
            mbti = Mbti.ESTJ,
            gender = Gender.FEMALE,
            age = 10,
        )

        // when & then 사용자를 저장하려고 하면 예외가 발생된다.
        assertThatThrownBy {
            userJpaRepository.saveAndFlush(user)
        }.isInstanceOf(ConstraintViolationException::class.java)
            .hasMessageContaining("닉네임은 비어있을 수 없습니다.")
    }

    companion object {
        /** 이곳에 작성해야 클래스 단위로 테스트 컨테이너가 생성되고 사라진다. */
        @JvmStatic
        @Container
        private val container = MySQLContainerProvider().newInstance("8.0.33")
            .withDatabaseName("sixteen")
            .withUsername("sixteen_application")
            .withPassword("sixteen_application")
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
            .withInitScript("sql/schema.sql")

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(dynamicPropertyRegistry: DynamicPropertyRegistry) {
            dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl)
            dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername)
            dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword)
        }
    }
}
