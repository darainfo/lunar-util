# lunar-util
  음력 계산 util

# 개발환경
- java : 1.8
- eclipse : Version: 2020-03 (4.15.0)
- gradle : 7.3.3


# 사용방법 

- 음력 -> 양력
<pre><code>DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
Date checkDt = df.parse("2023-02-26");
String result = LunarUtils.getLunarToStringSolarDay(checkDt,true);
System.out.println("result : "+ result);</code></pre>


- 양력 -> 음력
<pre><code>DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
Date checkDt = df.parse("2023-04-16");
String result = LunarUtils.getSolarToStringLunarDay(checkDt,true);
System.out.println("result : "+ result);</code></pre>