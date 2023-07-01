package io.handicrafty.sixteen.user.model

import io.handicrafty.sixteen.common.jpa.enumeration.Gender
import io.handicrafty.sixteen.common.jpa.enumeration.Mbti
import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserValidationTest {

    val validatorFactory = Validation.buildDefaultValidatorFactory()
    val validator = validatorFactory.validator

    @Test
    fun `닉네임의 최대 글자수는 12자이고, 이를 초과하면 예외가 발생한다`() {
        // given 닉네임이 13자인 사용자가 있다.
        val user = User(
            nickname = "배고픈하마가배고픈하마를1",
            mbti = Mbti.ESTJ,
            gender = Gender.FEMALE,
            age = 10,
        )

        // when 사용자를 Validation 하면
        val constraintViolations = validator.validate(user)

        // then 검증 결과가 닉네임의 최대 글자수를 위반했다는 것을 알 수 있다.
        assertThat(constraintViolations).isNotEmpty()
        constraintViolations.forEach {
            assertThat(it.message).isEqualTo("닉네임은 12자 이내여야 합니다.")
        }
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

        // when 사용자를 Validation 하면
        val constraintViolations = validator.validate(user)

        // then 검증 결과가 닉네임이 비어있다는 것을 알 수 있다.
        assertThat(constraintViolations).isNotEmpty()
        constraintViolations.forEach {
            assertThat(it.message).isEqualTo("닉네임은 비어있을 수 없습니다.")
        }
    }

}
