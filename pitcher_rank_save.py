import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from bs4 import BeautifulSoup
import mysql.connector

# 크롬 드라이버 설정
options = Options()
options.binary_location = "/Applications/Google Chrome Canary.app/Contents/MacOS/Google Chrome Canary"
options.add_experimental_option("detach", True)
service = Service("/opt/homebrew/bin/chromedriver")
driver = webdriver.Chrome(service=service, options=options)

# 투수 순위 페이지 접근
url = "https://mykbostats.com/stats/top/era"
driver.get(url)
time.sleep(10)  # Cloudflare 우회 대기

html = driver.page_source
driver.quit()

# HTML 파싱
soup = BeautifulSoup(html, "html.parser")
table = soup.find("table")
if not table:
    print("테이블을 찾을 수 없습니다.")
    exit()

rows = table.find("tbody").find_all("tr")
print(f"총 행 수: {len(rows)}개")

# DB 연결
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="rootroot",
    database="Bumva"
)
cursor = conn.cursor()

# 기존 데이터 삭제
cursor.execute("DELETE FROM pitcher_rank_v2")

# 행마다 데이터 삽입
for row in rows:
    cols = [td.get_text(strip=True) for td in row.find_all("td")]
    print(f"컬럼 개수: {len(cols)} → {cols}")
    
    if len(cols) == 11:
        try:
            # '1Cody Ponce#30 - SP - RHP' → 순위, 이름
            raw_name = cols[0]
            rank_str = ''.join(filter(str.isdigit, raw_name.split('#')[0]))  # 숫자 추출
            name = raw_name.replace(rank_str, '').split('#')[0].strip()
            pitcher_rank = int(rank_str)

            team = cols[1]
            era = float(cols[2])
            whip = float(cols[3])
            ip = cols[4]  # 소수점 변환, 문자열로 그대로
            h = int(cols[5])
            hr = int(cols[6])
            bb = int(cols[7])
            hbp = int(cols[8])
            so = int(cols[9])
            r = int(cols[10])
            er = int(cols[11 - 1])  # 혹시 IndexError 방지

            print(f"저장: {pitcher_rank}위 {name} / ERA {era}, WHIP {whip}, 이닝 {ip}")

            sql = """
                INSERT INTO pitcher_rank_v2 (pitcher_rank, name, team, era, whip, ip, h, hr, bb, hbp, so, r, er)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            cursor.execute(sql, (pitcher_rank, name, team, era, whip, ip, h, hr, bb, hbp, so, r, er))

        except Exception as e:
            print("저장 실패:", e)
            continue

# 저장 후 종료
conn.commit()
print("투수 데이터 DB 저장 완료")
cursor.close()
conn.close()