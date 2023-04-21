package greenscripter.iclicker.api.data.response;

/*
 * 
 * {
 * "access_token": ".",
 * "token_type": "bearer",
 * "refresh_token": ".",
 * "expires_in": 86399,
 * "scope": "read write",
 * "userId": "5d1",
 * "jti": "ce4c"
 * }
 */
public record LoginResponse(String access_token, String token_type, String refresh_token, int expires_in, String scope, String userId, String jti) {

}
