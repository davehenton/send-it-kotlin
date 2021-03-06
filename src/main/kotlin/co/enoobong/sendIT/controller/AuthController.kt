package co.enoobong.sendIT.controller

import co.enoobong.sendIT.payload.BaseApiResponse
import co.enoobong.sendIT.payload.LoginRequest
import co.enoobong.sendIT.payload.SignUpRequest
import co.enoobong.sendIT.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping(
    "auth",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class AuthController(private val authService: AuthService) {

    @PostMapping("v1/signup")
    fun signUp(@RequestBody @Valid signUpRequest: SignUpRequest): ResponseEntity<BaseApiResponse> {
        val response = authService.signUpUser(signUpRequest)
        return if (response.status == HttpStatus.CREATED.value()) {

            val location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(signUpRequest.username).toUri()
            ResponseEntity.created(location).body(response)
        } else {
            ResponseEntity(response, HttpStatus.resolve(response.status) ?: HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("v1/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<BaseApiResponse> {
        val response = authService.loginUser(loginRequest)
        return ResponseEntity(response, HttpStatus.resolve(response.status) ?: HttpStatus.INTERNAL_SERVER_ERROR)
    }
}