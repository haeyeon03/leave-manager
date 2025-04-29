package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.CustomFileWritable;

public class FileUtil {
	public static String BASE_PATH = "C:/workspace/LeaveManager/src/files/";

	// filePath 경로의 파일을 읽어 문자열 리스트로 반환
	public static List<String> readFile(String filePath) {
		List<String> lines = new ArrayList<String>();
		try {
			FileInputStream fi = new FileInputStream(BASE_PATH + filePath);
			Scanner scan = new Scanner(fi);
			while (true) {
				if (!scan.hasNextLine()) {
					break;
				}
				lines.add(scan.nextLine());
			}
			scan.close();
			fi.close();
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return lines;
	}

	public static void saveFile(String filePath, List<? extends CustomFileWritable> data) {
		try {
			FileOutputStream fo = new FileOutputStream(BASE_PATH + filePath);
			PrintStream out = new PrintStream(fo);
			for (CustomFileWritable d : data) {
				out.print(d.joinWithComma());
			}
			out.close();
			fo.close();
		} catch (IOException e) {
			e.getLocalizedMessage();
		}
	}
}
