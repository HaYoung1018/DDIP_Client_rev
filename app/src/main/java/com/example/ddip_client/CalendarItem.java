package com.example.ddip_client;

public class CalendarItem {
    private String date;        // 날짜
    private String dayOfWeek;   // 요일
    private String time;        // 시간
    private String salary;     // 급여
    private String userType;
    private String name;
    // 생성자
    public CalendarItem(String date, String dayOfWeek, String time, String salary, String userType, String name) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.salary = salary;
        this.userType = userType;
        this.name = name;
    }

    // Getter 메서드들
    public String getDate() {
        return date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public String getSalary() {
        return salary;
    }

    public String getUserType() { return userType; }

    public String getName() { return name; }
}