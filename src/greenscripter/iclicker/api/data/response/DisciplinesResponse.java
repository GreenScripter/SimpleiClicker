package greenscripter.iclicker.api.data.response;

import java.util.List;

/**
 * GET https://api.iclicker.com/v1/disciplines
 * [{
 * "disciplineId": "a14e0e77-1801-2759-aebe-af688f0be1b9",
 * "name": "Anthropology",
 * "sort": 10,
 * "created": "2017-05-21T02:07:14.786Z",
 * "creator": "00000000-0000-0000-0000-000000000000",
 * "updated": "2017-05-21T02:07:14.786Z",
 * "updater": "00000000-0000-0000-0000-000000000000"
 * },
 * List, manual type
 */
public class DisciplinesResponse {

	List<Discipline> disciplines;

	public static record Discipline(String disciplineId, String name, int sort, String created, String creator, String updated, String updater) {

	}
}
