package util;

import java.util.Collections;
import java.util.List;

public class PageUtil {
	private static int PAGE_SIZE = 7;
	
	/**
	 * 페이지네이션
	 * 
	 * @param <T> 리스트 타입
	 * @param list 전체 데이터 리스트
	 * @param page 페이지 번호
	 * @return 선택한 페이지에 해당하는 데이터 리스트; 없다면, 빈 리스트 반환
	 */
	public static <T> List<T> pagenate(List<T> list, int page) {
		int totalSize = list.size();
		int start = PAGE_SIZE * (page - 1);
		int end = Math.min(start + PAGE_SIZE, totalSize);

		if (start >= totalSize || start < 0) {
			return Collections.emptyList(); // 빈 리스트를 반환
		}
		return list.subList(start, end);
	}

	/**
	 * 총 페이지 수
	 * 
	 * @param totalSize 리스트의 크기
	 * @return 총 페이지
	 */
	public static int calculateTotalPage(int totalSize) {
		int totalPage = (totalSize / PAGE_SIZE);
		if (totalSize % PAGE_SIZE > 0) {
			totalPage = totalPage + 1;
		}
		return totalPage;
	}
}
