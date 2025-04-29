package util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	/**
	 * 기간 내 주말 제외한 워킹데이일 수
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 * @example "2024-12-27" ~ "2025-01-01" -> { 2024: 3, 2025: 1 }
	 */
	public static Map<Integer, Integer> calculateWorkingDays(String startDateStr, String endDateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate start = LocalDate.parse(startDateStr, formatter);
		LocalDate end = LocalDate.parse(endDateStr, formatter);

		Map<Integer, Integer> map = new HashMap<>();

		LocalDate current = start;
		while (!current.isAfter(end)) {
			DayOfWeek dayOfWeek = current.getDayOfWeek();

			// 주말 제외
			if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
				int year = current.getYear();
				map.put(year, map.getOrDefault(year, 0) + 1);
			}
			current = current.plusDays(1);
		}
		return map;
	}
}
