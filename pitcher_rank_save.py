import time  # 시간 지연 처리를 위한 모듈 (웹 로딩/보안 우회 대기용)
from selenium import webdriver  # 셀레니움: 브라우저 자동 제어
from selenium.webdriver.chrome.service import Service  # ChromeDriver 서비스 관리
from selenium.webdriver.chrome.options import Options  # 브라우저 실행 옵션 설정
from bs4 import BeautifulSoup  # HTML 파싱 라이브러리
import mysql.connector  # MySQL DB 연결 라이브러리

# 1. 크롬 드라이버 설정
options = Options()  # 크롬 옵션 객체 생성
options.binary_location = "/Applications/Google Chrome Canary.app/Contents/MacOS/Google Chrome Canary"  # Chrome Canary 경로 설정
options.add_experimental_option("detach", True)  # 작업 완료 후 브라우저 자동 종료 방지
service = Service("/opt/homebrew/bin/chromedriver")  # ChromeDriver 실행 경로 지정
driver = webdriver.Chrome(service=service, options=options)  # 크롬 드라이버 실행

# 2. 투수 순위 페이지 접속
url = "https://mykbostats.com/stats/top/era"  # KBO 투수 순위 페이지 URL
driver.get(url)  # 웹 페이지 열기
time.sleep(10)  # Cloudflare 우회 및 데이터 완전 로딩 대기

# 3. 페이지 HTML 소스 가져오기
html = driver.page_source  # 현재 페이지 HTML 코드 가져오기
driver.quit()  # 브라우저 종료

# 4. BeautifulSoup으로 HTML 파싱
soup = BeautifulSoup(html, "html.parser")  # HTML 파싱 객체 생성
table = soup.find("table")  # 첫 번째 <table> 요소 찾기
if not table:  # 테이블이 없으면 종료
    print("테이블을 찾을 수 없습니다.")
    exit()

# 5. 테이블에서 행(row) 추출
rows = table.find("tbody").find_all("tr")  # <tbody> 내부의 모든 <tr> 수집
print(f"총 행 수: {len(rows)}개")  # 행 개수 출력

# 6. MySQL 데이터베이스 연결
conn = mysql.connector.connect(
    host="localhost",       # DB 호스트 주소
    user="root",            # DB 사용자명
    password="rootroot",    # DB 비밀번호
    database="Bumva"        # 사용할 DB 이름
)
cursor = conn.cursor()  # 커서 객체 생성

# 7. 기존 pitcher_rank_v2 테이블 데이터 모두 삭제
cursor.execute("DELETE FROM pitcher_rank_v2")

# 8. 각 행 데이터를 파싱하고 DB에 삽입
for row in rows:
    cols = [td.get_text(strip=True) for td in row.find_all("td")]  # 각 셀 텍스트 추출
    print(f"컬럼 개수: {len(cols)} → {cols}")  # 디버깅 출력

    if len(cols) == 11:  # 컬럼 수가 정확히 11개일 경우만 처리
        try:
            # ex: '1Cody Ponce#30 - SP - RHP'에서 순위와 이름 추출
            raw_name = cols[0]  # 첫 번째 셀 전체 문자열
            rank_str = ''.join(filter(str.isdigit, raw_name.split('#')[0]))  
            # 앞쪽 숫자만 추출 (순위)
            name = raw_name.replace(rank_str, '').split('#')[0].strip()  
            # 이름 부분만 추출
            pitcher_rank = int(rank_str)  # 문자열 숫자를 정수로 변환

            # 나머지 데이터 파싱
            team = cols[1]             # 팀명
            era = float(cols[2])      # ERA
            whip = float(cols[3])     # WHIP
            ip = cols[4]              # 이닝 (ex: '67⅓', 문자열 그대로 저장)
            h = int(cols[5])          # 피안타 수
            hr = int(cols[6])         # 피홈런 수
            bb = int(cols[7])         # 볼넷 수
            hbp = int(cols[8])        # 사구 수
            so = int(cols[9])         # 탈삼진 수
            r = int(cols[10])         # 실점
            er = int(cols[10])        # 자책점 (IndexError 방지용으로도 10 사용)

            # 추출 결과 출력
            print(f"저장: {pitcher_rank}위 {name} / ERA {era}, WHIP {whip}, 이닝 {ip}")

            # SQL INSERT문 작성
            sql = """
                INSERT INTO pitcher_rank_v2 (pitcher_rank, name, team, era, whip, ip, h, hr, bb, hbp, so, r, er)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            # INSERT 실행
            cursor.execute(sql, (pitcher_rank, name, team, era, whip, ip, h, hr, bb, hbp, so, r, er))

        except Exception as e:
            # 에러 발생 시 출력하고 건너뜀
            print("저장 실패:", e)
            continue

# 9. 커밋 및 연결 종료
conn.commit()  # DB에 변경사항 반영
print("투수 데이터 DB 저장 완료")  # 완료 메시지
cursor.close()  # 커서 닫기
conn.close()    # DB 연결 종료