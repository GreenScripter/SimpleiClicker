package greenscripter.iclicker.api.data.response;

import java.util.List;

/*
 * https://api.iclicker.com/trogon/v4/profile
 * :method: GET
 * :authority: api.iclicker.com
 * :scheme: https
 * :path: /trogon/v4/profile
 * origin: https://instructor.iclicker.com
 * authorization: Bearer 9.2GI
 * content-type: application/json
 * reef-auth-type: oauth
 * accept: application/json
 * sec-fetch-dest: empty
 * user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_16_0) AppleWebKit/537.36 (KHTML, like
 * Gecko) QtWebEngine/5.15.1 Chrome/80.0.3987.163 Safari/537.36
 * client-tag: REEF/INSTRUCTOR/5.0.5/WEB///
 * sec-fetch-site: same-site
 * sec-fetch-mode: cors
 * referer: https://instructor.iclicker.com/
 * accept-encoding: gzip, deflate, br
 */
/*
 * {
 * "userid": "-",
 * "dateAdded": 1681158621221,
 * "firstName": "Name",
 * "lastName": "Name",
 * "email": "email@gmail.com",
 * "studentId": "",
 * "discipline": "Computer Science",
 * "seckey": "9",
 * "courseCount": 0,
 * "status": "trial",
 * "expirationDate": 1682406000000,
 * "expirationDateIso8601": "2023-04-25",
 * "daysRemaining": 14,
 * "institutionId": "a3db",
 * "institutionName": "University",
 * "inWarningPeriod": false,
 * "showWarningBadge": false,
 * "moduleWarning": [],
 * "institutionSiteLicenseList": [],
 * "registeredClickers": [],
 * "enableKeyboardShortcuts": false,
 * "clickerId": null
 * }
 */
public class ProfileResponse {

	public String userid;
	public long dateAdded;
	public String firstName;
	public String lastName;
	public String email;
	public String studentId;
	public String discipline;
	public String seckey;
	public int courseCount;
	public String status;
	public long expirationDate;
	public String expirationDateIso8601;
	public int daysRemaining;
	public String institutionId;
	public String institutionName;
	public boolean inWarningPeriod;
	public boolean showWarningBadge;
	public List<Object> moduleWarning;
	public List<Object> institutionSiteLicenseList;
	public List<Object> registeredClickers;
	public boolean enableKeyboardShortcuts;
	public Object clickerId;
}
