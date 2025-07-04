package com.shinlogis.wms.common.util;

import java.io.File;

//애플리케이션 전반에 걸쳐 유용하게 사용할 공통코드 중 파일 관련된 기능..
public class FileUtil {
	
	/*---------------------------------------
	 확장자 반환하는 메서드
	 *---------------------------------------*/
	public static String getExt(String path) {
		return path.substring(path.lastIndexOf('.')+1);
	}
	
	
	/*---------------------------------------
	 중복되지 않는 이름으로 새로운 파일 생성하기
	 targetDir: 파일이 생성될 디렉토리
	 *---------------------------------------*/
	public static File createFileByTime(String targetDir, String ext) {
		long time = System.currentTimeMillis();
		
		String filename = targetDir + File.separator + time + "." + ext;
		return new File(filename);
	}
}
