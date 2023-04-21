package greenscripter.iclicker.api.data.request;

//POST https://api.iclicker.com/login/autologin
//{"launchTokenId":"3e","clientId":"reef-instructor-web-client-id"
public record AutoLoginRequest(String launchTokenId, String clientId) {

}
