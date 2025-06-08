import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from bs4 import BeautifulSoup
import mysql.connector

# 크롬 설정
options = Options()
options.binary_location = "/Applications/Google Chrome Canary.app/Contents/MacOS/Google Chrome Canary"
options.add_experimental_option("detach", True)
service = Service("/opt/homebrew/bin/chromedriver")
driver = webdriver.Chrome(service=service, options=options)

# 페이지 접속
url = "https://mykbostats.com/stats/top/ba"
driver.get(url)
time.sleep(10)

# HTML 파싱
html = driver.page_source
soup = BeautifulSoup(html, "html.parser")
table = soup.find("table")

if not table:
    print(" 테이블을 찾을 수 없습니다.")
    driver.quit()
    exit()

# MySQL DB 연결
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="rootroot",
    database="Bumva"
)
cursor = conn.cursor()

# 기존 데이터 삭제
cursor.execute("DELETE FROM batter_rank_v2")

# 테이블 행 추출
rows = table.find("tbody").find_all("tr")
print(f"총 행 수: {len(rows)}개")

for row in rows:
    cols = [td.get_text(strip=True) for td in row.find_all("td")]
    print(f"컬럼 개수: {len(cols)} → {cols}")

    if len(cols) == 9:
        try:
            # 1번째 열: '1Kim Seong-yoon#39 - CF - LHH'
            raw = cols[0]
            batter_rank = int(''.join(filter(str.isdigit, raw.split('#')[0].split()[0])))
            batter_name = raw.split('#')[0].lstrip('0123456789').strip()

            team = cols[1]
            avg = float(cols[2])
            g = int(cols[3])
            pa = int(cols[7])
            hr = int(cols[4])
            sb = int(cols[5])
            slg = float(cols[6])
            ab = int(cols[8])
            ops = slg + avg  # ops는 일부 누락되어 avg + slg로 대체

            # 디버깅 출력
            print(f"{batter_rank}, {batter_name}, {team}, AVG: {avg}, G: {g}, PA: {pa}, HR: {hr}, SB: {sb}, SLG: {slg}, AB: {ab}, OPS: {ops:.3f}")

            # DB 저장
            sql = """
                INSERT INTO batter_rank_v2
                (batter_rank, batter_name, team, avg, g, pa, hr, sb, slg, ab, ops)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            cursor.execute(sql, (
                batter_rank, batter_name, team, avg, g, pa, hr, sb, slg, ab, ops
            ))
        except Exception as e:
            print(f"처리 오류: {e}")
            continue

# 커밋 및 종료
conn.commit()
print("타자 데이터 DB 저장 완료")
cursor.close()
conn.close()
driver.quit()