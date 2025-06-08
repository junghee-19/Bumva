import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from bs4 import BeautifulSoup
import mysql.connector

# 1. 크롬 설정
options = Options()
options.binary_location = "/Applications/Google Chrome Canary.app/Contents/MacOS/Google Chrome Canary"
options.add_experimental_option("detach", True)
service = Service("/opt/homebrew/bin/chromedriver")
driver = webdriver.Chrome(service=service, options=options)

# 2. 페이지 접근
driver.get("https://mykbostats.com/")
time.sleep(10)  # Cloudflare 우회 대기

# 3. HTML 저장
html = driver.page_source
with open("rank_dump.html", "w", encoding="utf-8") as f:
    f.write(html)
driver.quit()

# 4. BeautifulSoup으로 파싱
soup = BeautifulSoup(html, "html.parser")
table = soup.find("table")
if not table:
    print("테이블을 찾을 수 없습니다.")
    exit()

# 5. MySQL DB 연결
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="rootroot",
    database="Bumva"
)
cursor = conn.cursor()

# 6. 기존 데이터 삭제
cursor.execute("DELETE FROM team_rank_v2")

# 7. 행 데이터 파싱 및 DB 저장
rows = table.find("tbody").find_all("tr")
print(f"총 행 수: {len(rows)}개")

for row in rows:
    cols = [td.get_text(strip=True) for td in row.find_all("td")]
    print(f"컬럼 개수: {len(cols)} → {cols}")

    if len(cols) == 7:
        rank_team = cols[0]
        team_rank = int(rank_team[:2].strip() if rank_team[1].isdigit() else rank_team[0])
        team_name = rank_team[2:].strip() if rank_team[1].isdigit() else rank_team[1:].strip()

        games = int(cols[1])
        wins = int(cols[2])
        draws = int(cols[3])
        losses = games - wins - draws  # 패배 수 직접 계산
        win_rate = float(cols[4])
        game_diff = cols[5]
        streak = cols[6]

        print(f" 추출: 순위 {team_rank}, 팀명 {team_name}, 경기수 {games}, 승 {wins}, 무 {draws}, 패 {losses}, 승률 {win_rate}, 게임차 {game_diff}, 연승/연패 {streak}")

        sql = """
            INSERT INTO team_rank_v2
            (team_rank, team_name, games, wins, draws, losses, win_rate, game_diff, streak)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            team_rank, team_name, games, wins, draws, losses, win_rate, game_diff, streak
        ))

# 8. 커밋 및 종료
conn.commit()
print(" DB 저장 완료")
cursor.close()
conn.close()