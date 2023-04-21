package greenscripter.iclicker.api.data.request;

/*
 * https://api.iclicker.com/authproxy/login
 * :method: POST
 * :authority: api.iclicker.com
 * :scheme: https
 * :path: /authproxy/login
 * content-length: 69
 * origin: https://instructor.iclicker.com
 * user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_16_0) AppleWebKit/537.36 (KHTML, like
 * Gecko) QtWebEngine/5.15.1 Chrome/80.0.3987.163 Safari/537.36
 * content-type: application/vnd.reef.login-proxy-request-v1+json
 * reef-auth-type: oauth
 * accept: application/json
 * sec-fetch-dest: empty
 * client-tag: REEF/INSTRUCTOR/5.0.5/WEB///
 * sec-fetch-site: same-site
 * sec-fetch-mode: cors
 * referer: https://instructor.iclicker.com/
 * accept-encoding: gzip, deflate, br
 * {"email":"","password":""}
 */
public record LoginRequest(String email, String password) {

}
