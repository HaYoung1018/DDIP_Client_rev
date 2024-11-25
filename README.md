# DDIP_Client 페이지 설명(로그인, 회원가입 제외)
activity_main.xml , MainActivity => 메인페이지

crewroom.xml , CrewRoomActivity => 크룰룸 페이지(하단바 문모양 클릭하면 나옴)

activity_alarm.xml , AlarmActivity.java => 알람페이지

activity_mypage.xml , MypageActivity.java => 마이페이지

calendar.xml ,CalendarActivity.java => 캘린더페이지

addwork.xml , AddWorkActivity.java => 근무추가 페이지 (캘린더-> +근무추가 누르면 나옴(메인페이지의 근무지 추가 눌러도나오긴함 (임시임))

activity_crewroom_userlist.xml + iem_user <br>
UserListActivity.java + User.java + UserAdapter.java => 유저리스트 페이지

activity_tradelist.xml, TradeListActivity.java -> 교환하기 리스트 보여줌<br>
item_trade.xml , TradeItem.java -> 교환하기 리스트에 올리는 아이템 <br>
activity_create_trade.xml , CreateTradeActivity.java -> 사용자가 자신의 근무시간 중 교환할 근무시간 선택(교환신청 버튼 누르면 여기로 이동) <br>
Tradeadaper.java -> 교환리스트와 교환 아이템 연결<br>
WorkTimeAdapter -> 사용자의 근무시간 관리? 사용자 근무시간을 리사이클뷰로 activity_create_trade.xml에띄울 수 있게함
