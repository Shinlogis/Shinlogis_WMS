-- 외래키 체크 false로
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 이름들을 반복적으로 드롭하는 쿼리 생성
SELECT CONCAT('DROP TABLE IF EXISTS `', table_name, '`;')
FROM information_schema.tables
WHERE table_schema = DATABASE();

--나오는거 ctrl c,v 해서 cmd해서 drop후 테이블, 데이터 입력해주세요

-- 외래키 체크 false 했던거 true로 복원
SET FOREIGN_KEY_CHECKS = 1;

