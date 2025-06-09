import time  # 웹 로딩 대기용 모듈
from selenium import webdriver  # 셀레니움 웹 브라우저 제어
from selenium.webdriver.chrome.service import Service  # ChromeDriver 서비스
from selenium.webdriver.chrome.options import Options  # 크롬 옵션 설정
from bs4 import BeautifulSoup  # HTML 파싱 라이브러리
import mysql.connector  # MySQL 데이터베이스 연동

# 1. 크롬 드라이버 설정
options = Options()  # 크롬 옵션 객체 생성
options.binary_location = "/Applications/Google Chrome Canary.app/Contents/MacOS/Google Chrome Canary"  # Chrome Canary 실행 경로
options.add_experimental_option("detach", True)  # 브라우저 자동 종료 방지
service = Service("/opt/homebrew/bin/chromedriver")  # chromedriver 경로 지정
driver = webdriver.Chrome(service=service, options=options)  # 드라이버 실행

# 2. 타자 타율 순위 페이지 접속
url = "https://mykbostats.com/stats/top/ba"  # 타자 순위 페이지 URL
driver.get(url)  # 페이지 열기
time.sleep(10)  # 로딩 및 Cloudflare 우회 대기

# 3. 현재 페이지 HTML 가져오기
html = driver.page_source  # 페이지 HTML 코드 추출
soup = BeautifulSoup(html, "html.parser")  # HTML 파싱 준비
table = soup.find("table")  # 첫 번째 <table> 태그 탐색

if not table:  # 테이블이 없으면 오류 메시지 출력 후 종료
    print(" 테이블을 찾을 수 없습니다.")
    driver.quit()
    exit()

# 4. MySQL 데이터베이스 연결
conn = mysql.connector.connect(
    host="localhost",        # DB 호스트
    user="root",             # 사용자 이름
    password="rootroot",     # 비밀번호
    database="Bumva"         # 사용할 데이터베이스 이름
)
cursor = conn.cursor()  # SQL 실행용 커서 생성

# 5. 기존 batter_rank_v2 데이터 삭제
cursor.execute("DELETE FROM batter_rank_v2")

# 6. 테이블의 각 행 추출
rows = table.find("tbody").find_all("tr")  # tbody 안의 모든 <tr> 추출
print(f"총 행 수: {len(rows)}개")  # 행 개수 출력

# 7. 각 행 처리 및 DB 저장
for row in rows:
    cols = [td.get_text(strip=True) for td in row.find_all("td")]#모든 셀의 텍스트추출
    print(f"컬럼 개수: {len(cols)} → {cols}")  # 컬럼 내용 확인용 출력

    if len(cols) == 9:  # 9개의 컬럼이 있을 경우에만 처리
        try:
            raw = cols[0]  # ex: '1Kim Seong-yoon#39 - CF - LHH'

            # 순위 추출
            batter_rank = int(''.join(filter(str.isdigit, raw.split('#')[0].split()[0])))  # 앞쪽 숫자 추출
            # 이름 추출 (숫자 제거 후 공백 처리)
            batter_name = raw.split('#')[0].lstrip('0123456789').strip()

            team = cols[1]            # 팀명
            avg = float(cols[2])     # 타율 (AVG)
            g = int(cols[3])         # 경기 수 (G)
            hr = int(cols[4])        # 홈런 수 (HR)
            sb = int(cols[5])        # 도루 수 (SB)
            slg = float(cols[6])     # 장타율 (SLG)
            pa = int(cols[7])        # 타석 수 (PA)
            ab = int(cols[8])        # 타수 (AB)
            ops = slg + avg          # OPS 계산 (웹에 누락 시 계산 방식 사용)

            # 데이터 확인용 출력
            print(f"{batter_rank}, {batter_name}, {team}, AVG: {avg}, G: {g}, PA: {pa}, HR: {hr}, SB: {sb}, SLG: {slg}, AB: {ab}, OPS: {ops:.3f}")

            # SQL INSERT 쿼리 작성 및 실행
            sql = """
                INSERT INTO batter_rank_v2
                (batter_rank, batter_name, team, avg, g, pa, hr, sb, slg, ab, ops)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            cursor.execute(sql, (
                batter_rank, batter_name, team, avg, g, pa, hr, sb, slg, ab, ops
            ))
        except Exception as e:
            print(f"처리 오류: {e}")  # 에러 발생 시 로그 출력 후 다음 행 처리
            continue

# 8. 변경사항 커밋 및 종료
conn.commit()  # DB에 실제 반영
print("타자 데이터 DB 저장 완료")  # 완료 메시지 출력
cursor.close()  # 커서 닫기
conn.close()    # DB 연결 종료
driver.quit()   # 브라우저 종료