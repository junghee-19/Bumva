import time  # 시간 지연 처리를 위한 모듈 (웹 페이지 로딩을 기다리며 보안을 우회하거나 봇으로 판단되지 않기 위해서)
from selenium import webdriver  # Selenium 웹 드라이버 모듈
from selenium.webdriver.chrome.service import Service  # ChromeDriver 서비스 설정
from selenium.webdriver.chrome.options import Options  # Chrome 실행 옵션 설정
from bs4 import BeautifulSoup  # HTML 파싱을 위한 BeautifulSoup
import mysql.connector  # MySQL 데이터베이스 연결을 위한 모듈

# 1. 크롬 드라이버 설정
options = Options()  # 크롬 옵션 객체 생성
options.binary_location = "/Applications/Google Chrome Canary.app/Contents/MacOS/Google Chrome Canary"  # Chrome Canary 경로 설정( 최신 버전으로 바로 바로 버전 맞춰 주기 위해서 )
options.add_experimental_option("detach", True)  # 브라우저 자동 종료 방지
service = Service("/opt/homebrew/bin/chromedriver")  # 크롬 드라이버 경로 지정
driver = webdriver.Chrome(service=service, options=options)  # 설정을 반영하여 브라우저 실행

# 2. 웹사이트 접속
driver.get("https://mykbostats.com/")  # 웹 페이지 열기 (팀순위 나와 잇는 사이트)
time.sleep(10) 
# Cloudflare 우회 및 페이지 로딩 대기 (바로 들어 가게 되면 로봇이나 보안 창에 막혀서 대기를 두었다)

# 3. 현재 HTML 저장
html = driver.page_source  # 현재 페이지의 전체 HTML 코드 가져오기
with open("rank_dump.html", "w", encoding="utf-8") as f:  # 파일로 저장
    f.write(html)
driver.quit()  # 브라우저 종료

# 4. BeautifulSoup으로 HTML 파싱
soup = BeautifulSoup(html, "html.parser")  # HTML 파싱 준비
table = soup.find("table")  # HTML 내 첫 번째 <table> 요소 찾기
if not table:  # 테이블 없을 경우 종료(데이터가 없는 사이트도 많아서 요소 찾을려고 넣음)
    print("테이블을 찾을 수 없습니다.")
    exit()

# 5. MySQL 데이터베이스 연결
conn = mysql.connector.connect(
    host="localhost",       # 호스트 주소 (로컬)
    user="root",            # 사용자 이름
    password="rootroot",    # 비밀번호
    database="Bumva"        # 사용할 DB 이름
)
cursor = conn.cursor()  # SQL 실행을 위한 커서 객체 생성

# 6. 기존 데이터 삭제
cursor.execute("DELETE FROM team_rank_v2")  # 기존 테이블 데이터 전부 삭제( 새로 받아오는 데이터를 써야 하기 때문에 원래 있던  데이터는 삭제해준다)

# 7. 테이블 행 파싱 및 DB 저장
rows = table.find("tbody").find_all("tr")  
# tbody 내부의 모든 tr 요소(행) 추출(HTML안에 있는 가져오는 데이터는 저부분에 있다 )
print(f"총 행 수: {len(rows)}개")  # 총 팀 수 출력

for row in rows:  # 각 행에 대해 반복
    cols = [td.get_text(strip=True) for td in row.find_all("td")]  
    # 각 셀에서 텍스트 추출
    print(f"컬럼 개수: {len(cols)} → {cols}")  # 디버깅용 출력

    if len(cols) == 7:  # 7개의 데이터가 있을 때만 처리
        rank_team = cols[0]  # 첫 번째 컬럼: '1LG Twins' 형식

        # 순위와 팀명 분리
        team_rank = int(rank_team[:2].strip() if rank_team[1].isdigit() else rank_team[0])
        team_name = rank_team[2:].strip() if rank_team[1].isdigit() else rank_team[1:].strip()

        games = int(cols[1])         # 총 경기 수
        wins = int(cols[2])          # 승 수
        draws = int(cols[3])         # 무승부 수
        losses = games - wins - draws  # 패배 수 계산
        win_rate = float(cols[4])    # 승률
        game_diff = cols[5]          # 게임차
        streak = cols[6]             # 연승/연패 정보

        # 추출 결과 출력
        print(f" 추출: 순위 {team_rank}, 팀명 {team_name}, 경기수 {games}, 승 {wins}, 무 {draws}, 패 {losses}, 승률 {win_rate}, 게임차 {game_diff}, 연승/연패 {streak}")

        # INSERT 쿼리 작성 및 실행
        sql = """
            INSERT INTO team_rank_v2
            (team_rank, team_name, games, wins, draws, losses, win_rate, game_diff, streak)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (
            team_rank, team_name, games, wins, draws, losses, win_rate, game_diff, streak
        ))

# 8. 변경사항 커밋 및 종료
conn.commit()  # DB에 실제 반영
print(" DB 저장 완료")  # 저장 성공 메시지
cursor.close()  # 커서 종료
conn.close()    # DB 연결 종료